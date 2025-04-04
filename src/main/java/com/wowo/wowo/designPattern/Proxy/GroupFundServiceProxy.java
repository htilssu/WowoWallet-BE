package com.wowo.wowo.designPattern.Proxy;

import com.wowo.wowo.data.dto.GroupFundDTO;
import com.wowo.wowo.data.dto.TransactionDTO;
import com.wowo.wowo.data.dto.UserDTO;
import com.wowo.wowo.exception.BadRequest;
import com.wowo.wowo.exception.NotFoundException;
import com.wowo.wowo.model.FundMember;
import com.wowo.wowo.model.GroupFund;
import com.wowo.wowo.model.Transaction;
import com.wowo.wowo.repository.FundMemberRepository;
import com.wowo.wowo.repository.GroupFundRepository;
import com.wowo.wowo.util.AuthUtil;
import lombok.AllArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Service
public class GroupFundServiceProxy extends AbstractGroupFundService {
    private final ConcreteGroupFundService groupFundService;
    private final GroupFundRepository groupFundRepository;
    private final FundMemberRepository fundMemberRepository;


    @Override
    public GroupFund createGroupFund(GroupFundDTO groupFundDTO, Authentication authentication) {
        validateGroupFundData(groupFundDTO);
        return groupFundService.createGroupFund(groupFundDTO, authentication);
    }

    @Override
    public FundMember addMemberToGroup(Long groupId, String memberId) {
        GroupFund groupFund = groupFundRepository.findById(groupId)
                .orElseThrow(
                        () -> new NotFoundException("Quỹ nhóm không tồn tại"));
        if (groupFund.isLocked()) {
            throw new IllegalStateException("Quỹ này đã bị khóa.");
        }
        if (fundMemberRepository.existsByGroupIdAndMemberId(groupId, memberId)) {
            throw new IllegalArgumentException("Thành viên đã tham gia quỹ này");
        }
        return groupFundService.addMemberToGroup(groupFund, memberId);
    }

    @Override
    public FundMember addMemberToGroup(GroupFund groupFund, String memberId) {
        if (groupFund.isLocked()) {
            throw new IllegalStateException("Quỹ này đã bị khóa.");
        }
        if (fundMemberRepository.existsByGroupIdAndMemberId(groupFund.getId(), memberId)) {
            throw new IllegalArgumentException("Thành viên đã tham gia quỹ này");
        }
        return groupFundService.addMemberToGroup(groupFund, memberId);
    }

    @Override
    public Map<String, Object> leaveGroupFund(Long groupId, String memberId) {
        return groupFundService.leaveGroupFund(groupId, memberId);
    }

    @Override
    public GroupFundDTO getGroupFund(Long id) {
        return groupFundService.getGroupFund(id);
    }

    @Override
    public List<UserDTO> getGroupMembers(Long groupId) {
        return groupFundService.getGroupMembers(groupId);
    }

    @Override
    public Map<String, List<GroupFundDTO>> getUserGroupFunds(Authentication authentication) {
        return groupFundService.getUserGroupFunds(authentication);
    }

    @Override
    public GroupFundDTO updateGroupFund(Long groupId, GroupFundDTO groupFundDTO, Authentication authentication) {
        GroupFund groupFund = groupFundRepository.findById(groupId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Quỹ nhóm không tồn tại"));
        if (groupFund.isLocked()) {
            throw new IllegalStateException("Quỹ này đã bị khóa.");
        }
        var userId = AuthUtil.getId();
        if (!groupFund.getOwner().getId().equals(userId)) {
            throw new AccessDeniedException("Khong co quyen.Chi co chu quy moi cap nhat QUY duoc!");
        }
        validateGroupFundData(groupFundDTO);
        return groupFundService.updateGroupFund(groupId, groupFundDTO, authentication);
    }

    @Override
    public Transaction topUp(Long groupId, String memberId, Long amount, String message) {
        GroupFund groupFund = groupFundRepository.findById(groupId)
                .orElseThrow(
                        () -> new NotFoundException("Quỹ nhóm không tồn tại"));

        // Kiểm tra xem quỹ có bị khóa không
        if (groupFund.isLocked()) {
            throw new IllegalStateException("Quỹ này đã bị khóa.");
        }

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

        return groupFundService.topUp(groupId, memberId, amount, message);
    }

    @Override
    public List<TransactionDTO> getTransactionHistory(Long groupId, Integer offset, Integer page) {
        groupFundRepository.findById(groupId)
                .orElseThrow(
                        () -> new NotFoundException("Quỹ nhóm không tồn tại"));
        return groupFundService.getTransactionHistory(groupId, offset, page);
    }

    @Override
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

        return groupFundService.withdraw(groupFundId, amount, message);
    }

    @Override
    public GroupFund lockGroupFund(Long groupId, Authentication authentication) {
        GroupFund groupFund = groupFundRepository.findById(groupId)
                .orElseThrow(
                        () -> new NotFoundException("Quỹ không tồn tại"));

        var userId = AuthUtil.getId();
        if (!groupFund.getOwner().getId().equals(userId)) {
            throw new AccessDeniedException("Khong co quyen.Chi co chu quy moi khoa QUY duoc!");
        }
        // Kiểm tra nếu quỹ đã bị khóa rồi
        if (groupFund.isLocked()) {
            throw new BadRequest("Quỹ đã bị khóa trước đó");
        }
        return groupFundService.lockGroupFund(groupId, authentication);
    }

    @Override
    public GroupFund unlockGroupFund(Long groupId, Authentication authentication) {
        GroupFund groupFund = groupFundRepository.findById(groupId)
                .orElseThrow(
                        () -> new NotFoundException("Quỹ không tồn tại"));
        var userId = AuthUtil.getId();
        if (!groupFund.getOwner().getId().equals(userId)) {
            throw new AccessDeniedException("Khong co quyen.Chi co chu quy moi mo khoa QUY duoc!");
        }

        // Kiểm tra nếu quỹ chưa bị khóa
        if (!groupFund.isLocked()) {
            throw new BadRequest("Quỹ chưa bị khóa");
        }
        return groupFundService.unlockGroupFund(groupId, authentication);
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
}
