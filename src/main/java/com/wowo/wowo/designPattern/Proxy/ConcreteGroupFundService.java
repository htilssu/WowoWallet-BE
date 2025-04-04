package com.wowo.wowo.designPattern.Proxy;

import com.wowo.wowo.data.dto.GroupFundDTO;
import com.wowo.wowo.data.dto.TransactionDTO;
import com.wowo.wowo.data.dto.UserDTO;
import com.wowo.wowo.data.mapper.GroupFundMapper;
import com.wowo.wowo.data.mapper.TransactionMapper;
import com.wowo.wowo.data.mapper.UserMapper;
import com.wowo.wowo.exception.NotFoundException;
import com.wowo.wowo.model.*;
import com.wowo.wowo.repository.FundMemberRepository;
import com.wowo.wowo.repository.GroupFundRepository;
import com.wowo.wowo.repository.GroupFundWalletRepository;
import com.wowo.wowo.service.TransactionService;
import com.wowo.wowo.service.TransferService;
import com.wowo.wowo.service.UserService;
import com.wowo.wowo.service.WalletService;
import com.wowo.wowo.util.AuthUtil;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class ConcreteGroupFundService extends AbstractGroupFundService {

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

    @Override
    public GroupFund createGroupFund(GroupFundDTO groupFundDTO, Authentication authentication) {
        String ownerId = (String) authentication.getPrincipal();
        User ownerUser = userService.getUserByIdOrElseThrow(ownerId);

        GroupFund groupFund = new GroupFund();

        try {
            GroupFundWallet groupFundWallet = new GroupFundWallet();
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

    @Override
    public FundMember addMemberToGroup(Long groupId, String memberId) {
        GroupFund groupFund = groupFundRepository.findById(groupId)
                .orElseThrow(
                        () -> new NotFoundException("Quỹ nhóm không tồn tại"));

        User member = userService.getUserByIdOrElseThrow(memberId);

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

    @Override
    public FundMember addMemberToGroup(GroupFund groupFund, String memberId) {

        User member = userService.getUserByIdOrElseThrow(memberId);

        var fundMemberId = new FundMemberId();
        fundMemberId.setGroupId(groupFund.getId());
        fundMemberId.setMemberId(memberId);

        FundMember fundMember = new FundMember();
        fundMember.setId(fundMemberId);
        fundMember.setMember(member);
        fundMember.setGroup(groupFund);
        fundMember.setMoney(0L);
        fundMemberRepository.save(fundMember);

        return fundMember;
    }

    @Override
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

    @Override
    public GroupFundDTO getGroupFund(Long id) {
        GroupFund groupFund = groupFundRepository.findById(id)
                .orElseThrow(
                        () -> new NotFoundException("Quỹ nhóm không tồn tại"));

        return groupFundMapper.toDto(groupFund);
    }

    @Override
    public List<UserDTO> getGroupMembers(Long groupId) {
        GroupFund groupFund = groupFundRepository.findById(groupId)
                .orElseThrow(
                        () -> new NotFoundException("Quỹ nhóm không tồn tại"));

        List<FundMember> members = fundMemberRepository.findByGroupId(groupId);

        return members.stream()
                .map(fundMember -> userMapper.toDto(fundMember.getMember()))
                .toList();
    }

    @Override
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
            } else {
                joinedFunds.add(groupFundMapper.toDto(groupFund));
            }
        });

        Map<String, List<GroupFundDTO>> result = new HashMap<>();
        result.put("createdFunds", createdFunds);
        result.put("joinedFunds", joinedFunds);

        return result;
    }

    @Override
    public GroupFundDTO updateGroupFund(Long groupId,
                                        GroupFundDTO groupFundDTO,
                                        Authentication authentication) {
        GroupFund groupFund = groupFundRepository.findById(groupId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Quỹ nhóm không tồn tại"));

        groupFund.setName(groupFundDTO.getName());
        groupFund.setImage(groupFundDTO.getImage());
        groupFund.setType(groupFundDTO.getType());
        groupFund.setDescription(groupFundDTO.getDescription());
        groupFund.setTarget(groupFundDTO.getTarget());
        groupFund.setTargetDate(groupFundDTO.getTargetDate());

        GroupFund updatedGroupFund = groupFundRepository.save(groupFund);

        return groupFundMapper.toDto(updatedGroupFund);
    }

    @Override
    public Transaction topUp(Long groupId,
                             String memberId,
                             Long amount,
                             String message) {
        GroupFund groupFund = groupFundRepository.findById(groupId)
                .orElseThrow(
                        () -> new NotFoundException("Quỹ nhóm không tồn tại"));

        User user = userService.getUserByIdOrElseThrow(memberId);

        Optional<FundMember> optionalFundMember = groupFund.getFundMembers()
                .stream()
                .filter(
                        member -> member.getMember()
                                .getId()
                                .equals(memberId))
                .findFirst();


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

    @Override
    public List<TransactionDTO> getTransactionHistory(Long groupId,
                                                      @Min(0) @NotNull Integer offset,
                                                      @Min(0) @NotNull Integer page) {

        List<Transaction> transactions =
                transactionService.getGroupFundTransaction(groupId, offset, page);

        return transactions.stream()
                .map(transactionMapper::toDto)
                .toList();
    }

    @Override
    public Transaction withdraw(Long groupFundId, Long amount, String message) {
        final GroupFund groupFund = groupFundRepository.findById(groupFundId)
                .orElseThrow(
                        () -> new NotFoundException("Không tìm thấy quỹ"));

        var authId = AuthUtil.getId();

        var ownerWallet = walletService.getUserWallet(authId);


        final Transaction transaction = transferService.transferMoney(
                groupFund.getWallet(), ownerWallet, amount);

        transaction.setReceiverName(ownerWallet.getUser().getFullName());
        transaction.setSenderName(groupFund.getName());
        transaction.setFlowType(FlowType.WITHDRAW_GROUP_FUND);
        transaction.setMessage(message);

        return transactionService.save(transaction);
    }

    @Override
    public GroupFund lockGroupFund(Long groupId, Authentication authentication) {
        GroupFund groupFund = groupFundRepository.findById(groupId)
                .orElseThrow(
                        () -> new NotFoundException("Quỹ không tồn tại"));

        // Cập nhật trạng thái quỹ và thời gian khóa
        groupFund.setLocked(true);

        // Lưu thay đổi vào cơ sở dữ liệu
        return groupFundRepository.save(groupFund);
    }

    @Override
    public GroupFund unlockGroupFund(Long groupId, Authentication authentication) {
        GroupFund groupFund = groupFundRepository.findById(groupId)
                .orElseThrow(
                        () -> new NotFoundException("Quỹ không tồn tại"));

        // Cập nhật trạng thái quỹ
        groupFund.setLocked(false);

        // Lưu thay đổi vào cơ sở dữ liệu
        return groupFundRepository.save(groupFund);
    }
}
