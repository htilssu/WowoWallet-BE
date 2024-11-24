package com.wowo.wowo.controller;

import com.wowo.wowo.data.dto.GroupFundInvitationDto;
import com.wowo.wowo.data.dto.InvitationDto;
import com.wowo.wowo.exception.NotFoundException;
import com.wowo.wowo.service.InvitationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
            Map<String, Object> invitationResponse = invitationService.sendInvitation(
                    invitationDto.getGroupId(),
                    invitationDto.getSenderId(),
                    invitationDto.getRecipientId()
            );

            return ResponseEntity.ok(invitationResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi hệ thống: " + e.getMessage());
        }
    }

    // Xác nhận lời mời
    @PostMapping("/{invitationId}/accept")
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
    @PostMapping("/{invitationId}/reject")
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
    @GetMapping("/sent")
    public ResponseEntity<?> getSentInvitations(Authentication authentication) {
        try {
            List<GroupFundInvitationDto> groupFundInvitations = invitationService.getSentInvitations(authentication);
            return ResponseEntity.ok(groupFundInvitations);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error: " + e.getMessage());
        }
    }

    // Lấy tất cả lời mời được nhận
    @GetMapping("/received")
    public ResponseEntity<?> getReceivedInvitations(Authentication authentication) {
        try {
            List<GroupFundInvitationDto> receivedInvitations = invitationService.getReceivedInvitations(authentication);
            return ResponseEntity.ok(receivedInvitations);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi hệ thống: " + e.getMessage());
        }
    }
}
