package com.wowo.wowo.controllers;

import com.wowo.wowo.annotations.authorized.IsUser;
import com.wowo.wowo.data.dto.*;
import com.wowo.wowo.exceptions.ReceiverNotFoundException;
import com.wowo.wowo.exceptions.UserNotFoundException;
import com.wowo.wowo.kafka.messages.ApiResponse;
import com.wowo.wowo.models.FundMember;
import com.wowo.wowo.models.GroupFund;
import com.wowo.wowo.services.GroupFundService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@IsUser
@AllArgsConstructor
@RequestMapping(value = "v1/group-fund", produces = "application/json; charset=utf-8")
public class GroupFundController {

    private final GroupFundService groupFundService;

    // Tạo quỹ nhóm
    @PostMapping
    public ResponseEntity<?> createGroupFund(@RequestBody GroupFundDto groupFundDto, Authentication authentication) {
        try {
            GroupFund createdFund = groupFundService.createGroupFund(groupFundDto, authentication);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage("Tạo quỹ thành công!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("Có lỗi xảy ra khi tạo quỹ: " + e.getMessage()));
        }
    }

    // Thêm thành viên vào quỹ
    @PostMapping("/members")
    public ResponseEntity<?> addMemberToGroup(@RequestBody FundMemberDto memberDto) {
        try {
            FundMember addedMember = groupFundService.addMemberToGroup(memberDto.getGroupId(), memberDto.getMemberId());
            return ResponseEntity.ok(new ResponseMessage("Thêm thành viên thành công!"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage("Quỹ không tìm thấy: " + e.getMessage()));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("Có lỗi xảy ra khi thêm thành viên: " + e.getMessage()));
        }
    }

    // Rời khỏi quỹ
    @PostMapping("/members/leave")
    public ResponseEntity<?> leaveGroup(@RequestBody FundMemberDto memberDto) {
        try {
            Map<String, Object> message = groupFundService.leaveGroupFund(memberDto.getGroupId(), memberDto.getMemberId());
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage()); // Thông báo lỗi
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // Người dùng không tồn tại
        } catch (ReceiverNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // Quỹ không tồn tại
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi hệ thống: " + e.getMessage()); // Lỗi không xác định
        }
    }

    // Lấy thông tin quỹ
    @GetMapping("/{id}")
    public ResponseEntity<?> getGroupFund(@PathVariable Long id) {
        try {
            GroupFundDto groupFundDto = groupFundService.getGroupFund(id);
            return ResponseEntity.ok(groupFundDto);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage("Quỹ không tìm thấy: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("Có lỗi xảy ra khi lấy thông tin quỹ: " + e.getMessage()));
        }
    }

    // Lấy danh sách member user của 1 quỹ
    @GetMapping("/{id}/members")
    public ResponseEntity<?> getGroupMembers(@PathVariable Long id) {
        try {
            List<UserDto> members = groupFundService.getGroupMembers(id);
            return ResponseEntity.ok(members);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage("Quỹ không tìm thấy: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("Có lỗi xảy ra khi lấy danh sách thành viên quỹ: " + e.getMessage()));
        }
    }

    // Lấy danh sách quỹ nhóm đang tham gia của một user
    @GetMapping("/user")
    public ResponseEntity<?> getUserGroupFunds(Authentication authentication) {
        try {
            Map<String, List<GroupFundDto>> userFunds = groupFundService.getUserGroupFunds(authentication);
            if (userFunds.get("createdFunds").isEmpty() && userFunds.get("joinedFunds").isEmpty()) {
                return ResponseEntity.ok(Map.of("message", "Người dùng không có quỹ nào tham gia hoặc tạo ra."));
            }

            return ResponseEntity.ok(userFunds);

        } catch (Exception e) {
            // Xử lý ngoại lệ chung và trả về lỗi 500
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Đã xảy ra lỗi: " + e.getMessage()));
        }
    }

    // Cập nhật quỹ nhóm
    @PostMapping("/{groupId}")
    public ResponseEntity<?> updateGroupFund(
            @PathVariable Long groupId,
            @RequestBody GroupFundDto groupFundDto,
            Authentication authentication) {
        try {

            GroupFundDto updatedFundDto = groupFundService.updateGroupFund(groupId, groupFundDto, authentication);

            ApiResponse<GroupFundDto> response = new ApiResponse<>("Cập nhật quỹ thành công!", updatedFundDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Có lỗi xảy ra khi cập nhật quỹ: " + e.getMessage()));
        }
    }

    // Lấy danh sách lịch sử giao dịch quỹ
    @GetMapping("/{id}/transactions")
    public ResponseEntity<?> getTransactions(@PathVariable Long id) {
        try {
            List<GroupFundTransactionDto> transactions = groupFundService.getTransactionHistory(id);
            return ResponseEntity.ok(transactions);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage("Quỹ không tìm thấy: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("Có lỗi xảy ra khi lấy lịch sử giao dịch: " + e.getMessage()));
        }
    }


}