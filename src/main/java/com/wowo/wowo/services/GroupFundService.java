package com.wowo.wowo.services;

import com.wowo.wowo.data.dto.*;
import com.wowo.wowo.data.mapper.GroupFundMapper;
import com.wowo.wowo.data.mapper.UserMapper;
import com.wowo.wowo.data.mapper.WalletMapper;
import com.wowo.wowo.exceptions.ReceiverNotFoundException;
import com.wowo.wowo.exceptions.UserNotFoundException;
import com.wowo.wowo.models.*;
import com.wowo.wowo.repositories.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class GroupFundService {
    private final GroupFundRepository groupFundRepository;
    private final FundMemberRepository fundMemberRepository;
    private final GroupFundTransactionRepository groupFundTransactionRepository;
    private final GroupFundMapper groupFundMapper;
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WalletMapper walletMapper;

    // Tạo quỹ nhóm
    public GroupFund createGroupFund(GroupFundDto groupFundDto, Authentication authentication) {
        //kiem tra nguoi dung
        // Lấy thông tin người gửi request từ Authentication
//        String ownerId = (String) authentication.getPrincipal();
        //chưa có người dùng nên lấy dữ liệu ảo là 1
        String ownerId = "1";
        Optional<User> userOptional = userRepository.findById(ownerId);

        // Kiểm tra người dùng có tồn tại không
        User owner = userOptional.orElseThrow(
                () -> new UserNotFoundException("Người dùng không tồn tại"));

        // Tạo quỹ nhóm mới
        GroupFund groupFund = new GroupFund();

        try {
            Wallet wallet = new Wallet();
            wallet.setOwnerType("group_fund");
            wallet.setBalance(0L);
            wallet.setCurrency("VND");
            wallet.setOwnerId(owner.getId());
            // Lưu ví vào cơ sở dữ liệu
            Wallet savedWallet = walletRepository.save(wallet);

            // Liên kết ví với quỹ nhóm
            groupFund.setWallet(savedWallet);
            groupFund.setName(groupFundDto.getName());
            groupFund.setImage(groupFundDto.getImage());
            groupFund.setType(groupFundDto.getType());
            groupFund.setDescription(groupFundDto.getDescription());
            groupFund.setBalance(0L);
            groupFund.setTarget(groupFundDto.getTarget());
            groupFund.setTargetDate(groupFundDto.getTargetDate());
            groupFund.setOwner(owner);

            // Lưu quỹ nhóm vào cơ sở dữ liệu
            GroupFund savedGroupFund = groupFundRepository.save(groupFund);

            // Thêm người tạo vào danh sách thành viên của quỹ (FundMember)
            FundMember fundMember = new FundMember();
            FundMemberId fundMemberId = new FundMemberId();
            fundMemberId.setGroupId(savedGroupFund.getId());
            fundMemberId.setMemberId(owner.getId());

            fundMember.setId(fundMemberId);
            fundMember.setGroup(savedGroupFund);
            fundMember.setMember(owner);
            fundMember.setMoney(0L);

            fundMemberRepository.save(fundMember);

            return savedGroupFund;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi trong quá trình tạo quỹ nhóm hoặc ví: " + e.getMessage());
        }
    }

    // Thêm thành viên vào quỹ
    public FundMember addMemberToGroup(Long groupId, String memberId) {
        GroupFund groupFund = groupFundRepository.findById(groupId)
                .orElseThrow(() -> new ReceiverNotFoundException("Quỹ nhóm không tồn tại"));

        Optional<User> userOptional = userRepository.findById(memberId);
        User member = userOptional.orElseThrow(
                () -> new UserNotFoundException("Thành viên không tồn tại"));

        //Kiểm tra xem thành viên đã có trong nhóm chưa
        if (fundMemberRepository.existsByGroupIdAndMemberId(groupId, memberId)) {
            throw new IllegalArgumentException("Thành viên đã tham gia quỹ này");
        }

        var fundMemberId = new FundMemberId();
        fundMemberId.setGroupId(groupId);
        fundMemberId.setMemberId(memberId);

        FundMember fundMember = new FundMember();
        fundMember.setId(fundMemberId);
        fundMember.setMember(member);
        fundMember.setGroup(groupFund);
        fundMember.setMoney(0L);
        fundMemberRepository.save(fundMember);

        return fundMember;
    }

    // Lấy thông tin quỹ
    public GroupFundDto getGroupFund(Long id) {
        GroupFund groupFund = groupFundRepository.findById(id)
                .orElseThrow(() -> new ReceiverNotFoundException("Quỹ nhóm không tồn tại"));

        // Chuyển đổi từ GroupFund sang GroupFundDto
        GroupFundDto dto = new GroupFundDto();
        dto.setId(groupFund.getId());
        dto.setName(groupFund.getName());
        dto.setDescription(groupFund.getDescription());
        dto.setBalance(groupFund.getBalance());
        dto.setTarget(groupFund.getTarget());
        dto.setTargetDate(groupFund.getTargetDate());
        dto.setCreatedDate(groupFund.getCreatedDate());
        dto.setImage(groupFund.getImage());
        dto.setType(groupFund.getType());

        // Nếu có owner (người quản lý quỹ), lấy thông tin owner
        if (groupFund.getOwner() != null) {
            dto.setOwner(userMapper.toDto(groupFund.getOwner()));
        }

        // Nếu có wallet (ví), lấy thông tin ví
        if (groupFund.getWallet() != null) {
            dto.setWallet(walletMapper.toDto(groupFund.getWallet()));
        }

        return dto;
    }

    // Lấy danh sách các thành viên của một quỹ
    public List<UserDto> getGroupMembers(Long groupId) {
        // Tìm tất cả các thành viên tham gia quỹ nhóm có groupId
        List<FundMember> members = fundMemberRepository.findByGroupId(groupId);

        // Chuyển đổi danh sách FundMember sang danh sách UserDto
        return members.stream()
                .map(fundMember -> {
                    User member = fundMember.getMember();
                    return new UserDto(
                            member.getId(),
                            member.getIsActive(),
                            member.getIsVerified(),
                            member.getJob()
                    );
                })
                .toList();
    }

    // Lấy danh sách các quỹ nhóm mà một user đang tham gia
    public Map<String, List<GroupFundDto>> getUserGroupFunds(String memberId) {
        List<FundMember> fundMembers = fundMemberRepository.findByMemberId(memberId);

        List<GroupFundDto> createdFunds = new ArrayList<>();
        List<GroupFundDto> joinedFunds = new ArrayList<>();

        // Duyệt qua danh sách quỹ mà người dùng tham gia
        fundMembers.forEach(fundMember -> {
            GroupFund groupFund = fundMember.getGroup();
            var owner_id = groupFund.getOwner().getId();
            if (owner_id.equals(memberId)) {
                createdFunds.add(groupFundMapper.toDto(groupFund));
            } else {
                joinedFunds.add(groupFundMapper.toDto(groupFund));
            }
        });

        // Tạo một Map để trả về hai danh sách
        Map<String, List<GroupFundDto>> result = new HashMap<>();
        result.put("createdFunds", createdFunds);
        result.put("joinedFunds", joinedFunds);

        return result;
    }

    // Ghi nhận giao dịch cho quỹ nhóm
    public GroupFundTransaction createTransaction(Long groupId, String memberId, Long amount) {
        GroupFund groupFund = groupFundRepository.findById(groupId)
                .orElseThrow(() -> new ReceiverNotFoundException("Quỹ nhóm không tồn tại"));

        Optional<User> userOptional = userRepository.findById(memberId);
        User member = userOptional.orElseThrow(
                () -> new UserNotFoundException("Thành viên không tồn tại"));

        // Tạo giao dịch
        GroupFundTransaction transaction = new GroupFundTransaction();
        transaction.setGroup(groupFund);
        transaction.setMember(member);

        // Lưu giao dịch vào bảng `transaction`
        Transaction tx = new Transaction();
        tx.setMoney(amount);
        tx.setDescription("Giao dịch đóng góp vào quỹ nhóm");
        tx.setCreated(Instant.from(java.time.LocalDateTime.now()));
        tx.setUpdated(Instant.from(java.time.LocalDateTime.now()));

        groupFundTransactionRepository.save(transaction);

        // Cập nhật số dư của quỹ nhóm
        groupFund.setBalance((long) (groupFund.getBalance() + amount));
        groupFundRepository.save(groupFund);

        return transaction;
    }

    // Lấy danh sách lịch sử giao dịch quỹ
    public List<GroupFundTransactionDto> getTransactionHistory(Long groupId) {
        GroupFund groupFund = groupFundRepository.findById(groupId)
                .orElseThrow(() -> new ReceiverNotFoundException("Quỹ nhóm không tồn tại"));

        List<GroupFundTransaction> transactions = groupFundTransactionRepository.findByGroupId(groupId);

        return transactions.stream()
                .map(groupFundMapper::toTransactionDto)
                .toList();
    }

}
