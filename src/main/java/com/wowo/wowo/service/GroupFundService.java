package com.wowo.wowo.service;

import com.wowo.wowo.data.dto.GroupFundDTO;
import com.wowo.wowo.data.dto.GroupFundTransactionDTO;
import com.wowo.wowo.data.dto.UserDTO;
import com.wowo.wowo.data.mapper.GroupFundMapper;
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
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class GroupFundService {

    private final TransactionRepository transactionRepository;

    private final GroupFundRepository groupFundRepository;
    private final FundMemberRepository fundMemberRepository;
    private final GroupFundTransactionRepository groupFundTransactionRepository;
    private final GroupFundMapper groupFundMapper;
    private final WalletRepository walletRepository;
    private final TransferService transferService;
    private final WalletService walletService;
    private final UserService userService;
    private UserMapper userMapper;

    // Tạo quỹ nhóm
    public GroupFund createGroupFund(GroupFundDTO groupFundDTO, Authentication authentication) {
        String ownerId = (String) authentication.getPrincipal();
        User ownerUser = userService.getUserByIdOrElseThrow(ownerId);

        // Tạo quỹ nhóm mới
        GroupFund groupFund = new GroupFund();

        try {
            Wallet wallet = new Wallet();
            wallet.setOwnerType(WalletOwnerType.GROUP_FUND);
            wallet.setBalance(0L);
            wallet.setCurrency("VND");
            // Lưu ví vào cơ sở dữ liệu

            // Kiểm tra tính hợp lệ của các trường dữ liệu
            validateGroupFundData(groupFundDTO);

            // Liên kết ví với quỹ nhóm
            groupFund.setWallet(wallet);
            groupFund.setName(groupFundDTO.getName());
            groupFund.setImage(groupFundDTO.getImage());
            groupFund.setType(groupFundDTO.getType());
            groupFund.setDescription(groupFundDTO.getDescription());
            groupFund.setTarget(groupFundDTO.getTarget());
            groupFund.setTargetDate(groupFundDTO.getTargetDate());
            groupFund.setOwner(ownerUser);

            // Lưu quỹ nhóm vào cơ sở dữ liệu
            GroupFund savedGroupFund = groupFundRepository.save(groupFund);
            savedGroupFund.getWallet()
                    .setOwnerId(savedGroupFund.getId()
                            .toString());
            walletRepository.save(savedGroupFund.getWallet());


            // Thêm người tạo vào danh sách thành viên của quỹ (FundMember)
            FundMember fundMember = new FundMember();
            FundMemberId fundMemberId = new FundMemberId();
            fundMemberId.setGroupId(savedGroupFund.getId());
            fundMemberId.setMemberId(ownerUser.getId());

            fundMember.setId(fundMemberId);
            fundMember.setGroup(savedGroupFund);
            fundMember.setMember(ownerUser);
            fundMember.setMoney(0L);

            fundMemberRepository.save(fundMember);

            return savedGroupFund;
        } catch (Exception e) {
            throw new RuntimeException(
                    "Lỗi trong quá trình tạo quỹ nhóm hoặc ví: " + e.getMessage());
        }
    }

    // Thêm thành viên vào quỹ
    public FundMember addMemberToGroup(Long groupId, String memberId) {
        GroupFund groupFund = groupFundRepository.findById(groupId)
                .orElseThrow(
                        () -> new NotFoundException("Quỹ nhóm không tồn tại"));

        // Kiểm tra xem quỹ có bị khóa không
        if (groupFund.isLocked()) {
            throw new IllegalStateException("Quỹ này đã bị khóa.");
        }

        User member = userService.getUserByIdOrElseThrow(memberId);

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

    /// Rời khỏi quỹ
    public Map<String, Object> leaveGroupFund(Long groupId, String memberId) {
        // Kiểm tra quỹ có tồn tại không
        groupFundRepository.findById(groupId)
                .orElseThrow(
                        () -> new NotFoundException("Quỹ nhóm không tồn tại"));

        userService.getUserByIdOrElseThrow(memberId);

        // Kiểm tra xem thành viên có trong quỹ không
        FundMemberId fundMemberId = new FundMemberId();
        fundMemberId.setGroupId(groupId);
        fundMemberId.setMemberId(memberId);

        FundMember fundMember = fundMemberRepository.findById(fundMemberId)
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "Thành viên không tồn tại trong quỹ này"));

        // Xóa thành viên khỏi quỹ
        fundMemberRepository.delete(fundMember);
        String successMessage = "Rời quỹ thành công.";
        Map<String, Object> response = new HashMap<>();
        response.put("message", successMessage);

        return response;
    }

    // Lấy thông tin quỹ
    public GroupFundDTO getGroupFund(Long id) {
        GroupFund groupFund = groupFundRepository.findById(id)
                .orElseThrow(
                        () -> new NotFoundException("Quỹ nhóm không tồn tại"));

        return groupFundMapper.toDto(groupFund);
    }

    // Lấy danh sách các thành viên của một quỹ
    public List<UserDTO> getGroupMembers(Long groupId) {
        GroupFund groupFund = groupFundRepository.findById(groupId)
                .orElseThrow(
                        () -> new NotFoundException("Quỹ nhóm không tồn tại"));

        // Tìm tất cả các thành viên tham gia quỹ nhóm có groupId
        List<FundMember> members = fundMemberRepository.findByGroupId(groupId);

        // Chuyển đổi danh sách FundMember sang danh sách UserDTO
        return members.stream()
                .map(fundMember -> userMapper.toDto(fundMember.getMember()))
                .toList();
    }

    // Lấy danh sách các quỹ nhóm mà một user đang tham gia
    public Map<String, List<GroupFundDTO>> getUserGroupFunds(Authentication authentication) {

        String userId = (String) authentication.getPrincipal();
        userService.getUserByIdOrElseThrow(userId);

        List<FundMember> fundMembers = fundMemberRepository.findByMemberId(userId);

        List<GroupFundDTO> createdFunds = new ArrayList<>();
        List<GroupFundDTO> joinedFunds = new ArrayList<>();

        // Duyệt qua danh sách quỹ mà người dùng tham gia
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

        // Tạo một Map để trả về hai danh sách
        Map<String, List<GroupFundDTO>> result = new HashMap<>();
        result.put("createdFunds", createdFunds);
        result.put("joinedFunds", joinedFunds);

        return result;
    }

    // Cập nhật quỹ nhóm
    public GroupFundDTO updateGroupFund(Long groupId,
            GroupFundDTO groupFundDTO,
            Authentication authentication) {
        String ownerId = (String) authentication.getPrincipal();
        GroupFund groupFund = groupFundRepository.findById(groupId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Quỹ nhóm không tồn tại"));

        // Kiểm tra xem quỹ có bị khóa không
        if (groupFund.isLocked()) {
            throw new IllegalStateException("Quỹ này đã bị khóa.");
        }

        // Kiểm tra quyền sở hữu
        if (!groupFund.getOwner()
                .getId()
                .equals(ownerId)) {
            throw new AccessDeniedException("Bạn không có quyền cập nhật quỹ nhóm này");
        }

        // Kiểm tra tính hợp lệ của các trường dữ liệu
        validateGroupFundData(groupFundDTO);

        // Cập nhật thông tin quỹ nhóm
        groupFund.setName(groupFundDTO.getName());
        groupFund.setImage(groupFundDTO.getImage());
        groupFund.setType(groupFundDTO.getType());
        groupFund.setDescription(groupFundDTO.getDescription());
        groupFund.setTarget(groupFundDTO.getTarget());
        groupFund.setTargetDate(groupFundDTO.getTargetDate());

        // Lưu lại quỹ nhóm
        GroupFund updatedGroupFund = groupFundRepository.save(groupFund);

        // Chuyển đổi quỹ nhóm đã cập nhật sang DTO và trả về
        return groupFundMapper.toDto(updatedGroupFund);
    }

    private void validateGroupFundData(GroupFundDTO groupFundDTO) {
        // Kiểm tra tên quỹ không để trống
        if (groupFundDTO.getName() == null ||
                groupFundDTO.getName()
                        .trim()
                        .isEmpty()) {
            throw new IllegalArgumentException("Tên quỹ không được để trống");
        }

        // Kiểm tra số tiền mục tiêu không âm
        if (groupFundDTO.getTarget() < 0) {
            throw new IllegalArgumentException("Số tiền mục tiêu không được là số âm");
        }

        // Kiểm tra ngày hạn không phải là ngày quá khứ
        if (groupFundDTO.getTargetDate() != null &&
                groupFundDTO.getTargetDate()
                        .isBefore(
                                LocalDate.now())) {
            throw new IllegalArgumentException("Ngày hạn không được là ngày trong quá khứ");
        }
    }

    /**
     * Nạp tiền vào quỹ
     *
     * @param groupId  id của quỹ
     * @param memberId id của thành viên
     * @param amount   số tiền nạp
     *
     * @return {@link GroupFundTransaction} chứa thông tin giao dịch
     */
    public GroupFundTransaction topUp(Long groupId,
            String memberId,
            Long amount,
            String description) {
        GroupFund groupFund = groupFundRepository.findById(groupId)
                .orElseThrow(
                        () -> new NotFoundException("Quỹ nhóm không tồn tại"));

        // Kiểm tra xem quỹ có bị khóa không
        if (groupFund.isLocked()) {
            throw new IllegalStateException("Quỹ này đã bị khóa.");
        }

        User user = userService.getUserByIdOrElseThrow(memberId);

        var fundMember = groupFund.getFundMembers()
                .stream()
                .filter(f -> f.getMember()
                        .equals(user))
                .findFirst()
                .orElseThrow(() -> new BadRequest("Thành viên không tham gia quỹ"));

        var userWallet = walletService.getUserWallet(user.getId())
                .orElseThrow(
                        () -> new NotFoundException("Không tìm thấy ví"));

        final WalletTransaction walletTransaction = transferService.transferMoney(userWallet,
                groupFund.getWallet(), amount);

        walletTransaction.getTransaction()
                .setVariant(TransactionVariant.GROUP_FUND);
        walletTransaction.getTransaction()
                .setReceiverName(groupFund.getName());

        transactionRepository.save(walletTransaction.getTransaction());
        fundMember.setMoney(fundMember.getMoney() + amount);

        GroupFundTransaction groupFundTransaction = new GroupFundTransaction();
        groupFundTransaction.setGroup(groupFund);
        groupFundTransaction.setMember(user);
        groupFundTransaction.setTransaction(walletTransaction.getTransaction());
        groupFundTransaction.setType(TransactionType.TOP_UP);
        groupFundTransaction.setDescription(description);

        groupFundTransactionRepository.save(groupFundTransaction);

        return groupFundTransaction;
    }

    public List<GroupFundTransactionDTO> getTransactionHistory(Long groupId,
            @Min(0) @NotNull Integer offset,
            @Min(0) @NotNull Integer page) {

        groupFundRepository.findById(groupId)
                .orElseThrow(
                        () -> new NotFoundException("Quỹ nhóm không tồn tại"));

        List<GroupFundTransaction> transactions =
                groupFundTransactionRepository.findByGroup_IdOrderByTransaction_CreatedDesc(groupId,
                        Pageable.ofSize(offset)
                                .withPage(page));

        return transactions.stream()
                .map(groupFundMapper::toTransactionDTO)
                .toList();
    }

    public GroupFundTransaction withdraw(Long groupFundId, Long amount, String description) {
        final GroupFund groupFund = groupFundRepository.findById(groupFundId)
                .orElseThrow(
                        () -> new NotFoundException("Không tìm thấy quỹ"));

        var authId = AuthUtil.getId();
        if (!groupFund.getOwner()
                .getId()
                .equals(authId)) {
            throw new AccessDeniedException("Bạn không có quyền rút tiền từ quỹ này");
        }

        var ownerWallet = walletService.getUserWallet(authId)
                .orElseThrow(
                        () -> new NotFoundException("Không tìm thấy ví"));


        final WalletTransaction walletTransaction = transferService.transferMoney(
                groupFund.getWallet(), ownerWallet, amount);
        GroupFundTransaction groupFundTransaction = new GroupFundTransaction();
        groupFundTransaction.setGroup(groupFund);
        groupFundTransaction.setMember(groupFund.getOwner());
        groupFundTransaction.setType(TransactionType.WITHDRAW);
        groupFundTransaction.setTransaction(walletTransaction.getTransaction());
        groupFundTransaction.setDescription(description);
        return groupFundTransactionRepository.save(groupFundTransaction);
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
