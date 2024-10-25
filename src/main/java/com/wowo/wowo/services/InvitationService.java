package com.wowo.wowo.services;

import com.wowo.wowo.exceptions.NotFoundException;
import com.wowo.wowo.exceptions.ReceiverNotFoundException;
import com.wowo.wowo.exceptions.UserNotFoundException;
import com.wowo.wowo.models.*;
import com.wowo.wowo.repositories.FundMemberRepository;
import com.wowo.wowo.repositories.GroupFundRepository;
import com.wowo.wowo.repositories.InvitationRepository;
import com.wowo.wowo.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class InvitationService {
    private final InvitationRepository invitationRepository;
    private final GroupFundRepository groupFundRepository;
    private final FundMemberRepository fundMemberRepository;
    private final UserRepository userRepository;

    // Gửi lời mời tham gia quỹ
    public Map<String, Object> sendInvitation(Long groupId, String senderId, String recipientId) {
        if (senderId.equals(recipientId)) {
            throw new IllegalArgumentException("Người gửi không thể trùng với người nhận.");
        }

        GroupFund groupFund = groupFundRepository.findById(groupId)
                .orElseThrow(() -> new ReceiverNotFoundException("Quỹ nhóm không tồn tại"));

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new UserNotFoundException("Người gửi không tồn tại"));

        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new UserNotFoundException("Người nhận không tồn tại"));

        if (invitationRepository.existsByGroupFundAndRecipientAndStatus(groupFund, recipient, InvitationStatus.PENDING)) {
            throw new IllegalArgumentException("Đã gửi lời mời cho người nhận này và đang chờ xác nhận.");
        }

        if (fundMemberRepository.existsByGroupIdAndMemberId(groupId, recipientId)) {
            throw new IllegalArgumentException("Thành viên đã tham gia quỹ này.");
        }

        // Tạo lời mời mới
        GroupFundInvitation groupFundInvitation = new GroupFundInvitation();
        groupFundInvitation.setGroupFund(groupFund);
        groupFundInvitation.setSender(sender);
        groupFundInvitation.setRecipient(recipient);
        groupFundInvitation.setStatus(InvitationStatus.PENDING);
        GroupFundInvitation savedGroupFundInvitation = invitationRepository.save(
                groupFundInvitation);

        String successMessage = String.format("Lời mời tham gia quỹ nhóm đã được gửi thành công tới %s.", recipient.getId());
        Map<String, Object> response = new HashMap<>();
        response.put("invitation", savedGroupFundInvitation);
        response.put("message", successMessage);

        return response;
    }

    // Lấy tất cả lời mời đã gửi đi
    public List<GroupFundInvitation> getSentInvitations(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        User sender = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Người dùng không tồn tại"));

        List<GroupFundInvitation> groupFundInvitations = invitationRepository.findBySender(sender);
        if (groupFundInvitations.isEmpty()) {
            throw new NotFoundException("Không có lời mời nào được gửi.");
        }

        return groupFundInvitations;
    }

    // Lấy tất cả lời mời được nhận
    public List<GroupFundInvitation> getReceivedInvitations(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        User recipient = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Người dùng không tồn tại"));

        List<GroupFundInvitation> groupFundInvitations = invitationRepository.findByRecipient(recipient);
        if (groupFundInvitations.isEmpty()) {
            throw new NotFoundException("Không có lời mời nào được nhận.");
        }

        return groupFundInvitations;
    }

    // Xác nhận lời mời
    public void acceptInvitation(Long invitationId) {
        GroupFundInvitation groupFundInvitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new NotFoundException("Lời mời không tồn tại"));

        GroupFund groupFund = groupFundRepository.findById(groupFundInvitation.getGroupFund().getId())
                .orElseThrow(() -> new ReceiverNotFoundException("Quỹ nhóm không tồn tại"));

        Optional<User> userOptional = userRepository.findById(groupFundInvitation.getRecipient().getId());
        User user = userOptional.orElseThrow(
                () -> new UserNotFoundException("Người dùng không tồn tại"));

        if (fundMemberRepository.existsByGroupIdAndMemberId(groupFundInvitation.getGroupFund().getId(), groupFundInvitation.getRecipient().getId())) {
            throw new IllegalArgumentException("Thành viên đã tham gia quỹ này");
        }

        var fundMemberId = new FundMemberId();
        fundMemberId.setGroupId(groupFundInvitation.getGroupFund().getId());
        fundMemberId.setMemberId(groupFundInvitation.getRecipient().getId());

        FundMember fundMember = new FundMember();
        fundMember.setId(fundMemberId);
        fundMember.setMember(user);
        fundMember.setGroup(groupFund);
        fundMember.setMoney(0L);
        fundMemberRepository.save(fundMember);

        // Cập nhật trạng thái lời mời
//        invitation.setStatus(InvitationStatus.ACCEPTED);
//        invitationRepository.save(invitation);

        // Xóa lời mời sau khi đã chấp nhận
        invitationRepository.delete(groupFundInvitation);
    }

    // Từ chối lời mời
    public void rejectInvitation(Long invitationId) {
        GroupFundInvitation groupFundInvitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new NotFoundException("Lời mời không tồn tại"));

        // Cập nhật trạng thái lời mời
//        invitation.setStatus(InvitationStatus.REJECTED);
//        invitationRepository.save(invitation);

        // Xóa lời mời khỏi cơ sở dữ liệu
        invitationRepository.delete(groupFundInvitation);
    }
}