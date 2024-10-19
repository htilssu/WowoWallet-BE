package com.wowo.wowo.controllers;

import com.wowo.wowo.data.dto.InvitationDto;
import com.wowo.wowo.exceptions.NotFoundException;
import com.wowo.wowo.models.GroupFundInvitation;
import com.wowo.wowo.services.InvitationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/invitations")
@AllArgsConstructor
public class InvitationController {
    private final InvitationService invitationService;

    // Gửi lời mời
    @PostMapping
    public ResponseEntity<?> sendInvitation(@RequestBody InvitationDto invitationDto) {
        try {
            Map<String, Object> invitation = invitationService.sendInvitation(
                    invitationDto.getGroupId(),
                    invitationDto.getSenderId(),
                    invitationDto.getRecipientId()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(invitation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi hệ thống: " + e.getMessage());
        }
    }

    // Xác nhận lời mời
    @PutMapping("/{invitationId}/accept")
    public ResponseEntity<?> acceptInvitation(@PathVariable Long invitationId) {
        try {
            invitationService.acceptInvitation(invitationId);
            return ResponseEntity.ok("Tham gia thành công.");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi hệ thống: " + e.getMessage());
        }
    }

    // Từ chối lời mời
    @PutMapping("/{invitationId}/reject")
    public ResponseEntity<?> rejectInvitation(@PathVariable Long invitationId) {
        try {
            invitationService.rejectInvitation(invitationId);
            return ResponseEntity.ok("Đã từ chối tham gia.");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi hệ thống: " + e.getMessage());
        }
    }

    // Lấy tất cả lời mời đã gửi đi
    @GetMapping("/sent/{senderId}")
    public ResponseEntity<?> getSentInvitations(@PathVariable String senderId) {
        try {
            List<GroupFundInvitation> groupFundInvitations = invitationService.getSentInvitations(senderId);
            return ResponseEntity.ok(groupFundInvitations);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error: " + e.getMessage());
        }
    }

    // Lấy tất cả lời mời được nhận
    @GetMapping("/received/{recipientId}")
    public ResponseEntity<?> getReceivedInvitations(@PathVariable String recipientId) {
        try {
            List<GroupFundInvitation> groupFundInvitations = invitationService.getReceivedInvitations(recipientId);
            return ResponseEntity.ok(groupFundInvitations);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error: " + e.getMessage());
        }
    }
}
