package com.wowo.wowo.controller;

import com.wowo.wowo.annotation.authorized.IsUser;
import com.wowo.wowo.data.dto.*;
import com.wowo.wowo.exception.ReceiverNotFoundException;
import com.wowo.wowo.exception.UserNotFoundException;
import com.wowo.wowo.kafka.message.ApiResponse;
import com.wowo.wowo.model.FundMember;
import com.wowo.wowo.model.GroupFund;
import com.wowo.wowo.service.GroupFundService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
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
    public ResponseEntity<?> createGroupFund(@RequestBody GroupFundDTO groupFundDTO,
            Authentication authentication) {
        try {
            GroupFund createdFund = groupFundService.createGroupFund(groupFundDTO, authentication);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new ResponseMessage("Tạo quỹ thành công!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseMessage("Có lỗi xảy ra khi tạo quỹ: " + e.getMessage()));
        }
    }

    // Thêm thành viên vào quỹ
    @PostMapping("/members")
    public ResponseEntity<?> addMemberToGroup(@RequestBody FundMemberDTO memberDTO) {
        try {
            FundMember addedMember = groupFundService.addMemberToGroup(memberDTO.getGroupId(),
                    memberDTO.getMemberId());
            return ResponseEntity.ok(new ResponseMessage("Thêm thành viên thành công!"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseMessage("Quỹ không tìm thấy: " + e.getMessage()));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseMessage("Có lỗi xảy ra khi thêm thành viên: " + e.getMessage()));
        }
    }

    // Rời khỏi quỹ
    @PostMapping("/members/leave")
    public ResponseEntity<?> leaveGroup(@RequestBody FundMemberDTO memberDTO) {
        try {
            Map<String, Object> message = groupFundService.leaveGroupFund(memberDTO.getGroupId(),
                    memberDTO.getMemberId());
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage()); // Thông báo lỗi
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    e.getMessage()); // Người dùng không tồn tại
        } catch (ReceiverNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    e.getMessage()); // Quỹ không tồn tại
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    "Lỗi hệ thống: " + e.getMessage()); // Lỗi không xác định
        }
    }

    // Lấy thông tin quỹ
    @GetMapping("/{id}")
    public ResponseEntity<?> getGroupFund(@PathVariable Long id) {
        try {
            GroupFundDTO groupFundDTO = groupFundService.getGroupFund(id);
            return ResponseEntity.ok(groupFundDTO);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseMessage("Quỹ không tìm thấy: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseMessage("Có lỗi xảy ra khi lấy thông tin quỹ: " + e.getMessage()));
        }
    }

    // Lấy danh sách member user của 1 quỹ
    @GetMapping("/{id}/members")
    public ResponseEntity<?> getGroupMembers(@PathVariable Long id) {
        try {
            List<UserDTO> members = groupFundService.getGroupMembers(id);
            return ResponseEntity.ok(members);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseMessage("Quỹ không tìm thấy: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(
                    "Có lỗi xảy ra khi lấy danh sách thành viên quỹ: " + e.getMessage()));
        }
    }

    // Lấy danh sách quỹ nhóm đang tham gia của một user
    @GetMapping("/user")
    public ResponseEntity<?> getUserGroupFunds(Authentication authentication) {
        try {
            Map<String, List<GroupFundDTO>> userFunds = groupFundService.getUserGroupFunds(
                    authentication);
            if (userFunds.get("createdFunds").isEmpty() && userFunds.get("joinedFunds").isEmpty()) {
                return ResponseEntity.ok(
                        Map.of("message", "Người dùng không có quỹ nào tham gia hoặc tạo ra."));
            }

            return ResponseEntity.ok(userFunds);

        } catch (Exception e) {
            // Xử lý ngoại lệ chung và trả về lỗi 500
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("message", "Đã xảy ra lỗi: " + e.getMessage()));
        }
    }

    // Cập nhật quỹ nhóm
    @PostMapping("/{groupId}")
    public ResponseEntity<?> updateGroupFund(
            @PathVariable Long groupId,
            @RequestBody GroupFundDTO groupFundDTO,
            Authentication authentication) {
        try {

            GroupFundDTO updatedFundDTO = groupFundService.updateGroupFund(groupId, groupFundDTO,
                    authentication);

            ApiResponse<GroupFundDTO> response = new ApiResponse<>("Cập nhật quỹ thành công!",
                    updatedFundDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Có lỗi xảy ra khi cập nhật quỹ: " + e.getMessage()));
        }
    }

    // Lấy danh sách lịch sử giao dịch quỹ
    @GetMapping("/{id}/transactions")
    public ResponseEntity<?> getTransactions(@PathVariable Long id,
            @ModelAttribute @Validated PagingDTO searchParams) {
        List<TransactionDTO> transactions = groupFundService.getTransactionHistory(id,
                searchParams.getOffset(), searchParams.getPage());
        return ResponseEntity.ok(transactions);
    }

    //  khóa quỹ
    @PostMapping("/{groupId}/lock")
    public ResponseEntity<?> lockGroupFund(@PathVariable Long groupId, Authentication authentication) {
        try {
            // Khóa quỹ bằng service
            GroupFund lockedFund = groupFundService.lockGroupFund(groupId, authentication);
            return ResponseEntity.ok(new ResponseMessage("Khóa quỹ thành công!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseMessage("Có lỗi xảy ra khi khóa quỹ: " + e.getMessage()));
        }
    }

    //  mở quỹ
    @PostMapping("/{groupId}/unlock")
    public ResponseEntity<?> unlockGroupFund(@PathVariable Long groupId, Authentication authentication) {
        try {
            // Mở quỹ bằng service
            GroupFund unlockedFund = groupFundService.unlockGroupFund(groupId, authentication);
            return ResponseEntity.ok(new ResponseMessage("Mở khóa quỹ thành công!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseMessage("Có lỗi xảy ra khi mở quỹ: " + e.getMessage()));
        }
    }

}