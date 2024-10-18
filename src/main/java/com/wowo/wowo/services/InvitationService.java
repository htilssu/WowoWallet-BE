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

        User sender = userRepository.findById(Long.valueOf(senderId))
                .orElseThrow(() -> new UserNotFoundException("Người gửi không tồn tại"));

        User recipient = userRepository.findById(Long.valueOf(recipientId))
                .orElseThrow(() -> new UserNotFoundException("Người nhận không tồn tại"));

        if (invitationRepository.existsByGroupIdAndRecipientIdAndStatus(groupId, recipientId, InvitationStatus.PENDING)) {
            throw new IllegalArgumentException("Đã gửi lời mời cho người nhận này và đang chờ xác nhận.");
        }

        if (fundMemberRepository.existsByGroupIdAndMemberId(groupId, recipientId)) {
            throw new IllegalArgumentException("Thành viên đã tham gia quỹ này.");
        }

        // Tạo lời mời mới
        Invitation invitation = new Invitation();
        invitation.setGroupId(groupId);
        invitation.setSenderId(senderId);
        invitation.setRecipientId(recipientId);
        invitation.setStatus(InvitationStatus.PENDING);
        Invitation savedInvitation = invitationRepository.save(invitation);

        String successMessage = String.format("Lời mời tham gia quỹ nhóm đã được gửi thành công tới %s.", recipient.getId());
        Map<String, Object> response = new HashMap<>();
        response.put("invitation", savedInvitation);
        response.put("message", successMessage);

        return response;
    }

    // Lấy tất cả lời mời đã gửi đi
    public List<Invitation> getSentInvitations(String senderId) {
        Optional<User> sender = userRepository.findById(Long.valueOf(senderId));
        if (sender.isEmpty()) {
            throw new UserNotFoundException("Người gửi không tồn tại");
        }

        List<Invitation> invitations = invitationRepository.findBySenderId(senderId);
        if (invitations.isEmpty()) {
            throw new NotFoundException("Không có lời mời nào được gửi.");
        }

        return invitations;
    }

    // Lấy tất cả lời mời được nhận
    public List<Invitation> getReceivedInvitations(String recipientId) {
        Optional<User> recipient = userRepository.findById(Long.valueOf(recipientId));
        if (recipient.isEmpty()) {
            throw new UserNotFoundException("Người nhận không tồn tại");
        }

        List<Invitation> invitations = invitationRepository.findByRecipientId(recipientId);
        if (invitations.isEmpty()) {
            throw new NotFoundException("Không có lời mời nào được nhận.");
        }

        return invitations;
    }

    // Xác nhận lời mời
    public void acceptInvitation(Long invitationId) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new NotFoundException("Lời mời không tồn tại"));

        GroupFund groupFund = groupFundRepository.findById(invitation.getGroupId())
                .orElseThrow(() -> new ReceiverNotFoundException("Quỹ nhóm không tồn tại"));

        Optional<User> userOptional = userRepository.findById(Long.valueOf(invitation.getRecipientId()));
        User user = userOptional.orElseThrow(
                () -> new UserNotFoundException("Người dùng không tồn tại"));

        if (fundMemberRepository.existsByGroupIdAndMemberId(invitation.getGroupId(), invitation.getRecipientId())) {
            throw new IllegalArgumentException("Thành viên đã tham gia quỹ này");
        }

        var fundMemberId = new FundMemberId();
        fundMemberId.setGroupId(invitation.getGroupId());
        fundMemberId.setMemberId(invitation.getRecipientId());

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
        invitationRepository.delete(invitation);
    }

    // Từ chối lời mời
    public void rejectInvitation(Long invitationId) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new NotFoundException("Lời mời không tồn tại"));

        // Cập nhật trạng thái lời mời
//        invitation.setStatus(InvitationStatus.REJECTED);
//        invitationRepository.save(invitation);

        // Xóa lời mời khỏi cơ sở dữ liệu
        invitationRepository.delete(invitation);
    }
}