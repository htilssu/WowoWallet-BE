package com.wowo.wowo.controllers;

import com.wowo.wowo.data.dto.response.FundMemberDto;
import com.wowo.wowo.data.dto.response.GroupFundDto;
import com.wowo.wowo.data.dto.response.GroupFundTransactionDto;
import com.wowo.wowo.data.dto.response.ResponseMessage;
import com.wowo.wowo.models.FundMember;
import com.wowo.wowo.models.GroupFund;
import com.wowo.wowo.models.GroupFundTransaction;
import com.wowo.wowo.services.GroupFundService;
import lombok.AllArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("v1/group-fund")
public class GroupFundController {

    private final GroupFundService groupFundService;

    // Tạo quỹ nhóm
    @PostMapping
    public ResponseEntity<?> createGroupFund(@RequestBody GroupFundDto groupFundDto, Authentication authentication) {
        try {
            GroupFund createdFund = groupFundService.createGroupFund(groupFundDto,authentication );
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage("Tạo quỹ thành công!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("Có lỗi xảy ra khi tạo quỹ: " + e.getMessage()));
        }
    }

    // Thêm thành viên vào quỹ
    @PostMapping("/{id}/members")
    public ResponseEntity<?> addMemberToGroup(@PathVariable int id, @RequestBody FundMemberDto memberDto) {
        try {
            FundMember addedMember = groupFundService.addMemberToGroup(id, memberDto);
            return ResponseEntity.ok(new ResponseMessage("Thêm thành viên thành công!"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage("Quỹ không tìm thấy: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("Có lỗi xảy ra khi thêm thành viên: " + e.getMessage()));
        }
    }

    // Ghi nhận giao dịch
    @PostMapping("/{id}/transactions")
    public ResponseEntity<?> createTransaction(@PathVariable int id, @RequestBody GroupFundTransactionDto transactionDto) {
        try {
            GroupFundTransaction transaction = groupFundService.createTransaction(id, transactionDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage("Giao dịch thành công!"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage("Quỹ không tìm thấy: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("Có lỗi xảy ra khi ghi nhận giao dịch: " + e.getMessage()));
        }
    }

    // Lấy thông tin quỹ
    @GetMapping("/{id}")
    public ResponseEntity<?> getGroupFund(@PathVariable int id) {
        try {
            GroupFund groupFund = groupFundService.getGroupFund(id);
            return ResponseEntity.ok(groupFund);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage("Quỹ không tìm thấy: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("Có lỗi xảy ra khi lấy thông tin quỹ: " + e.getMessage()));
        }
    }

    // Lấy danh sách thành viên
    @GetMapping("/{id}/members")
    public ResponseEntity<?> getGroupMembers(@PathVariable int id) {
        try {
            List<FundMember> members = groupFundService.getGroupMembers(id);
            return ResponseEntity.ok( members);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage("Quỹ không tìm thấy: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("Có lỗi xảy ra khi lấy danh sách thành viên: " + e.getMessage()));
        }
    }

    // Lấy danh sách lịch sử quỹ
    @GetMapping("/{id}/transactions")
    public ResponseEntity<?> getTransactions(@PathVariable int id) {
        try {
            List<GroupFundTransaction> transactions = groupFundService.getTransactions(id);
            return ResponseEntity.ok(transactions);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage("Quỹ không tìm thấy: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("Có lỗi xảy ra khi lấy lịch sử giao dịch: " + e.getMessage()));
        }
    }
}
