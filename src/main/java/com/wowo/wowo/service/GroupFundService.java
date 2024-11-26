package com.wowo.wowo.service;

import com.wowo.wowo.data.dto.GroupFundDTO;
import com.wowo.wowo.data.dto.TransactionDTO;
import com.wowo.wowo.data.dto.UserDTO;
import com.wowo.wowo.data.mapper.GroupFundMapper;
import com.wowo.wowo.data.mapper.TransactionMapper;
import com.wowo.wowo.data.mapper.UserMapper;
import com.wowo.wowo.exception.BadRequest;
import com.wowo.wowo.exception.NotFoundException;
import com.wowo.wowo.model.*;
import com.wowo.wowo.repository.*;
import com.wowo.wowo.util.AuthUtil;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class GroupFundService {

    private final GroupFundWalletRepository groupFundWalletRepository;
    private final GroupFundRepository groupFundRepository;
    private final FundMemberRepository fundMemberRepository;
    private final GroupFundMapper groupFundMapper;
    private final TransferService transferService;
    private final WalletService walletService;
    private final UserService userService;
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;
    private UserMapper userMapper;

    public GroupFund createGroupFund(GroupFundDTO groupFundDTO, Authentication authentication) {
        String ownerId = (String) authentication.getPrincipal();
        User ownerUser = userService.getUserByIdOrElseThrow(ownerId);

        GroupFund groupFund = new GroupFund();

        try {
            GroupFundWallet groupFundWallet = new GroupFundWallet();
            validateGroupFundData(groupFundDTO);
            groupFundWallet.setGroupFund(groupFund);
            groupFund.setWallet(groupFundWallet);
            groupFund.setName(groupFundDTO.getName());
            groupFund.setImage(groupFundDTO.getImage());
            groupFund.setType(groupFundDTO.getType());
            groupFund.setDescription(groupFundDTO.getDescription());
            groupFund.setTarget(groupFundDTO.getTarget());
            groupFund.setTargetDate(groupFundDTO.getTargetDate());
            groupFund.setOwner(ownerUser);

            GroupFund savedGroupFund = groupFundRepository.save(groupFund);

            FundMember fundMember = new FundMember();
            FundMemberId fundMemberId = new FundMemberId();
            fundMemberId.setGroupId(savedGroupFund.getId());
            fundMemberId.setMemberId(ownerUser.getId());

            fundMember.setId(fundMemberId);
            fundMember.setGroup(savedGroupFund);
            fundMember.setMember(ownerUser);

            fundMemberRepository.save(fundMember);

            return savedGroupFund;
        } catch (Exception e) {
            throw new RuntimeException(
                    "Lỗi trong quá trình tạo quỹ nhóm hoặc ví: " + e.getMessage());
        }
    }

    private void validateGroupFundData(GroupFundDTO groupFundDTO) {
        if (groupFundDTO.getName() == null ||
                groupFundDTO.getName()
                        .trim()
                        .isEmpty()) {
            throw new IllegalArgumentException("Tên quỹ không được để trống");
        }

        if (groupFundDTO.getTarget() < 0) {
            throw new IllegalArgumentException("Số tiền mục tiêu không được là số âm");
        }

        if (groupFundDTO.getTargetDate() != null &&
                groupFundDTO.getTargetDate()
                        .isBefore(
                                LocalDate.now())) {
            throw new IllegalArgumentException("Ngày hạn không được là ngày trong quá khứ");
        }
    }

    public FundMember addMemberToGroup(Long groupId, String memberId) {
        GroupFund groupFund = groupFundRepository.findById(groupId)
                .orElseThrow(
                        () -> new NotFoundException("Quỹ nhóm không tồn tại"));

        if (groupFund.isLocked()) {
            throw new IllegalStateException("Quỹ này đã bị khóa.");
        }

        User member = userService.getUserByIdOrElseThrow(memberId);

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

    /// Rời khỏi quỹ
    public Map<String, Object> leaveGroupFund(Long groupId, String memberId) {
        groupFundRepository.findById(groupId)
                .orElseThrow(
                        () -> new NotFoundException("Quỹ nhóm không tồn tại"));

        userService.getUserByIdOrElseThrow(memberId);

        FundMemberId fundMemberId = new FundMemberId();
        fundMemberId.setGroupId(groupId);
        fundMemberId.setMemberId(memberId);

        FundMember fundMember = fundMemberRepository.findById(fundMemberId)
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "Thành viên không tồn tại trong quỹ này"));

        fundMemberRepository.delete(fundMember);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Rời quỹ thành công.");

        return response;
    }

    public GroupFundDTO getGroupFund(Long id) {
        GroupFund groupFund = groupFundRepository.findById(id)
                .orElseThrow(
                        () -> new NotFoundException("Quỹ nhóm không tồn tại"));

        return groupFundMapper.toDto(groupFund);
    }

    public List<UserDTO> getGroupMembers(Long groupId) {
        GroupFund groupFund = groupFundRepository.findById(groupId)
                .orElseThrow(
                        () -> new NotFoundException("Quỹ nhóm không tồn tại"));

        List<FundMember> members = fundMemberRepository.findByGroupId(groupId);

        return members.stream()
                .map(fundMember -> userMapper.toDto(fundMember.getMember()))
                .toList();
    }

    public Map<String, List<GroupFundDTO>> getUserGroupFunds(Authentication authentication) {

        String userId = (String) authentication.getPrincipal();
        userService.getUserByIdOrElseThrow(userId);

        List<FundMember> fundMembers = fundMemberRepository.findByMemberId(userId);

        List<GroupFundDTO> createdFunds = new ArrayList<>();
        List<GroupFundDTO> joinedFunds = new ArrayList<>();

        fundMembers.forEach(fundMember -> {
            GroupFund groupFund = fundMember.getGroup();
            var owner_id = groupFund.getOwner()
                    .getId();
            if (owner_id.equals(userId)) {
                createdFunds.add(groupFundMapper.toDto(groupFund));
            }
            else {
                joinedFunds.add(groupFundMapper.toDto(groupFund));
            }
        });

        Map<String, List<GroupFundDTO>> result = new HashMap<>();
        result.put("createdFunds", createdFunds);
        result.put("joinedFunds", joinedFunds);

        return result;
    }

    public GroupFundDTO updateGroupFund(Long groupId,
            GroupFundDTO groupFundDTO,
            Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        GroupFund groupFund = groupFundRepository.findById(groupId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Quỹ nhóm không tồn tại"));

        if (groupFund.isLocked()) {
            throw new IllegalStateException("Quỹ này đã bị khóa.");
        }

        if (!groupFund.getOwner()
                .getId()
                .equals(userId)) {
            throw new AccessDeniedException("Bạn không có quyền cập nhật quỹ nhóm này");
        }

        validateGroupFundData(groupFundDTO);

        groupFund.setName(groupFundDTO.getName());
        groupFund.setImage(groupFundDTO.getImage());
        groupFund.setType(groupFundDTO.getType());
        groupFund.setDescription(groupFundDTO.getDescription());
        groupFund.setTarget(groupFundDTO.getTarget());
        groupFund.setTargetDate(groupFundDTO.getTargetDate());

        GroupFund updatedGroupFund = groupFundRepository.save(groupFund);

        return groupFundMapper.toDto(updatedGroupFund);
    }

    /**
     * Nạp tiền vào quỹ
     *
     * @param groupId  id của quỹ
     * @param memberId id của thành viên
     * @param amount   số tiền nạp
     *
     * @return {@link Transaction} chứa thông tin giao dịch
     */
    public Transaction topUp(Long groupId,
            String memberId,
            Long amount,
            String message) {
        GroupFund groupFund = groupFundRepository.findById(groupId)
                .orElseThrow(
                        () -> new NotFoundException("Quỹ nhóm không tồn tại"));

        // Kiểm tra xem quỹ có bị khóa không
        if (groupFund.isLocked()) {
            throw new IllegalStateException("Quỹ này đã bị khóa.");
        }

        User user = userService.getUserByIdOrElseThrow(memberId);


        Optional<FundMember> optionalFundMember = groupFund.getFundMembers()
                .stream()
                .filter(
                        member -> member.getMember()
                                .getId()
                                .equals(memberId))
                .findFirst();

        if (optionalFundMember.isEmpty()) {
            throw new NotFoundException("Thành viên không tồn tại trong quỹ");
        }


        var userWallet = walletService.getUserWallet(user.getId());

        final Transaction transaction = transferService.transferMoney(userWallet,
                groupFund.getWallet(), amount);

        transaction.setSenderName(user.getFullName());
        transaction.setReceiverName(groupFund.getName());
        transaction.setMessage(message);
        transaction.setFlowType(FlowType.TOP_UP_GROUP_FUND);

        transactionService.save(transaction);

        optionalFundMember.get()
                .setMoney(optionalFundMember.get()
                        .getMoney() + amount);

        groupFundRepository.save(groupFund);

        return transaction;
    }

    public List<TransactionDTO> getTransactionHistory(Long groupId,
            @Min(0) @NotNull Integer offset,
            @Min(0) @NotNull Integer page) {

        groupFundRepository.findById(groupId)
                .orElseThrow(
                        () -> new NotFoundException("Quỹ nhóm không tồn tại"));

        List<Transaction> transactions =
                transactionService.getGroupFundTransaction(groupId, offset, page);

        return transactions.stream()
                .map(transactionMapper::toDto)
                .toList();
    }

    public Transaction withdraw(Long groupFundId, Long amount, String message) {
        final GroupFund groupFund = groupFundRepository.findById(groupFundId)
                .orElseThrow(
                        () -> new NotFoundException("Không tìm thấy quỹ"));

        var authId = AuthUtil.getId();
        if (!groupFund.getOwner()
                .getId()
                .equals(authId)) {
            throw new AccessDeniedException("Bạn không có quyền rút tiền từ quỹ này");
        }

        var ownerWallet = walletService.getUserWallet(authId);


        final Transaction transaction = transferService.transferMoney(
                groupFund.getWallet(), ownerWallet, amount);

        transaction.setReceiverName(ownerWallet.getUser().getFullName());
        transaction.setSenderName(groupFund.getName());
        transaction.setFlowType(FlowType.WITHDRAW_GROUP_FUND);
        transaction.setMessage(message);

        return transactionService.save(transaction);
    }

    //Khóa quỹ
    public GroupFund lockGroupFund(Long groupId, Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        GroupFund groupFund = groupFundRepository.findById(groupId)
                .orElseThrow(
                        () -> new NotFoundException("Quỹ không tồn tại"));

        // Kiểm tra quyền sở hữu quỹ
        if (!groupFund.getOwner()
                .getId()
                .equals(userId)) {
            throw new AccessDeniedException("Bạn không có quyền khóa quỹ này");
        }

        // Kiểm tra nếu quỹ đã bị khóa rồi
        if (groupFund.isLocked()) {
            throw new BadRequest("Quỹ đã bị khóa trước đó");
        }

        // Cập nhật trạng thái quỹ và thời gian khóa
        groupFund.setLocked(true);

        // Lưu thay đổi vào cơ sở dữ liệu
        return groupFundRepository.save(groupFund);
    }

    // Mở quỹ
    public GroupFund unlockGroupFund(Long groupId, Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        GroupFund groupFund = groupFundRepository.findById(groupId)
                .orElseThrow(
                        () -> new NotFoundException("Quỹ không tồn tại"));

        // Kiểm tra quyền sở hữu quỹ
        if (!groupFund.getOwner()
                .getId()
                .equals(userId)) {
            throw new AccessDeniedException("Bạn không có quyền mở quỹ này");
        }

        // Kiểm tra nếu quỹ chưa bị khóa
        if (!groupFund.isLocked()) {
            throw new BadRequest("Quỹ chưa bị khóa");
        }

        // Cập nhật trạng thái quỹ
        groupFund.setLocked(false);

        // Lưu thay đổi vào cơ sở dữ liệu
        return groupFundRepository.save(groupFund);
    }

}
