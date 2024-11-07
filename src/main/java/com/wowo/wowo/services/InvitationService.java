package com.wowo.wowo.services;

import com.wowo.wowo.data.dto.GroupFundInvitationDto;
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
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InvitationService {
    private final InvitationRepository invitationRepository;
    private final GroupFundRepository groupFundRepository;
    private final FundMemberRepository fundMemberRepository;
    private final UserRepository userRepository;

    // Gửi lời mời tham gia quỹ
    public Map<String, Object> sendInvitation(Long groupId, String senderId, String recipientId) {
        // Kiểm tra nếu người gửi và người nhận là cùng một người
        if (senderId.equals(recipientId)) {
            String errorMessage = "Không thể gửi cho chính mình.";
            Map<String, Object> response = new HashMap<>();
            response.put("message", errorMessage);
            return response;
        }

        GroupFund groupFund = groupFundRepository.findById(groupId)
                .orElseThrow(() -> new ReceiverNotFoundException("Quỹ nhóm không tồn tại"));

        // Kiểm tra xem quỹ có bị khóa không
        if (groupFund.getIsLocked()) {
            throw new IllegalStateException("Quỹ này đã bị khóa.");
        }

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new UserNotFoundException("Người gửi không tồn tại"));

        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new UserNotFoundException("Người nhận không tồn tại"));

        // Kiểm tra nếu đã gửi lời mời cho người nhận này
        if (invitationRepository.existsByGroupFundAndRecipientAndStatus(groupFund, recipient, InvitationStatus.PENDING)) {
            String warningMessage = "Đã gửi lời mời trước đó và đang chờ xác nhận.";
            Map<String, Object> response = new HashMap<>();
            response.put("message", warningMessage);
            return response;
        }
        // Kiểm tra nếu người nhận đã là thành viên của quỹ
        if (fundMemberRepository.existsByGroupIdAndMemberId(groupId, recipientId)) {
            String warningMessage = "Thành viên đã tham gia quỹ này.";
            Map<String, Object> response = new HashMap<>();
            response.put("message", warningMessage);
            return response;
        }

        // Tạo lời mời mới
        GroupFundInvitation groupFundInvitation = new GroupFundInvitation();
        groupFundInvitation.setGroupFund(groupFund);
        groupFundInvitation.setSender(sender);
        groupFundInvitation.setRecipient(recipient);
        groupFundInvitation.setStatus(InvitationStatus.PENDING);
        GroupFundInvitation savedGroupFundInvitation = invitationRepository.save(
                groupFundInvitation);

        String successMessage = String.format("Gửi lời mời thành công tới %s.", recipient.getUsername());
        Map<String, Object> response = new HashMap<>();

        response.put("message", successMessage);

        return response;
    }

    // Lấy tất cả lời mời đã gửi đi
    public List<GroupFundInvitationDto> getSentInvitations(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        User sender = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Người dùng không tồn tại"));

        List<GroupFundInvitation> groupFundInvitations = invitationRepository.findBySender(sender);

        return mapToDtoList(groupFundInvitations);
    }

    // Lấy tất cả lời mời được nhận
    public List<GroupFundInvitationDto> getReceivedInvitations(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        User recipient = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Người dùng không tồn tại"));

        List<GroupFundInvitation> groupFundInvitations = invitationRepository.findByRecipient(recipient);

        // Chuyển đổi sang DTO
        return mapToDtoList(groupFundInvitations);
    }
    // map entity list to DTO list
    private List<GroupFundInvitationDto> mapToDtoList(List<GroupFundInvitation> invitations) {
        return invitations.stream()
                .map(invitation -> new GroupFundInvitationDto(
                        invitation.getId(),
                        invitation.getGroupFund().getName(),
                        invitation.getGroupFund().getDescription(),
                        invitation.getSender().getUsername(),
                        invitation.getSender().getEmail(),
                        invitation.getStatus(),
                        invitation.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    // Xác nhận lời mời
    public void acceptInvitation(Long invitationId) {
        GroupFundInvitation groupFundInvitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new NotFoundException("Lời mời không tồn tại"));

        GroupFund groupFund = groupFundRepository.findById(groupFundInvitation.getGroupFund().getId())
                .orElseThrow(() -> new ReceiverNotFoundException("Quỹ nhóm không tồn tại"));

        // Kiểm tra xem quỹ có bị khóa không
        if (groupFund.getIsLocked()) {
            throw new IllegalStateException("Quỹ này đã bị khóa.");
        }

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