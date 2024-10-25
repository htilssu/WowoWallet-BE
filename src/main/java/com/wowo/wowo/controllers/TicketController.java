package com.wowo.wowo.controllers;

import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wowo.wowo.models.SupportTicket;
import com.wowo.wowo.services.SupportTicketService;



@RestController
@RequestMapping(value = "v1/ticket", produces = "application/json; charset=utf-8")
public class TicketController {

    @Autowired
    private SupportTicketService supportTicketService;

    @PostMapping("/create")
    public ResponseEntity<String> createTicket(@RequestBody SupportTicket ticket) {
        try {
            supportTicketService.createTicket(ticket);
            return ResponseEntity.ok("Yêu cầu hỗ trợ của bạn đã được tạo. Vui lòng chờ");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra trong quá trình tạo yêu cầu hỗ trợ.");
        }
    }

    @GetMapping("/user/{customerId}")
    public ResponseEntity<List<SupportTicket>> getUserTicket(@PathVariable String customerId) {
        return ResponseEntity.ok(supportTicketService.getUserTicket(customerId));
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateTicketStatus(@PathVariable String id, @RequestParam String status) {
        supportTicketService.updateTicketStatus(id, status);
        return ResponseEntity.ok("Trạng thái yêu cầu đã được cập nhật");
    }
}


