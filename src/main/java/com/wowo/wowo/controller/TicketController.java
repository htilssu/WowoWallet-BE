package com.wowo.wowo.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wowo.wowo.data.dto.SupportTicketDTO;
import com.wowo.wowo.exception.UserNotFoundException;
import com.wowo.wowo.model.SupportTicket;
import com.wowo.wowo.model.SupportTicketStatus;
import com.wowo.wowo.repository.UserRepository;
import com.wowo.wowo.service.SupportTicketService;

@RestController
@RequestMapping(value = "v1/ticket", produces = "application/json; charset=utf-8")
public class TicketController {

    @Autowired
    private SupportTicketService supportTicketService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<String> createTicket(@RequestBody SupportTicketDTO supportTicketDTO) {
        try {
            String customerId = supportTicketDTO.getCustomer().getId();

            if (customerId == null || customerId.isEmpty()) {
                return ResponseEntity.badRequest().body("Không tìm thấy thông tin khách hàng.");
            }

            userRepository.findById(customerId).orElseThrow(() -> new UserNotFoundException("Không tìm thấy người dùng!"));

            List<SupportTicket> openTickets = supportTicketService.getUserTicket(customerId);

            boolean hasOpenTicket = openTickets.stream().anyMatch(ticket -> ticket.getStatus() == SupportTicketStatus.OPEN);

            if (hasOpenTicket) {
                return ResponseEntity.badRequest().body("Bạn đã gửi một yêu cầu hỗ trợ chưa được phản hồi.");
            }

            supportTicketService.createTicket(supportTicketDTO);
            return ResponseEntity.ok("Yêu cầu hỗ trợ của bạn đã được tạo. Vui lòng chờ.");
        } catch (DataAccessException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .body("Có lỗi xảy ra trong cơ sở dữ liệu khi tạo yêu cầu hỗ trợ.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .body("Có lỗi xảy ra trong quá trình tạo yêu cầu hỗ trợ.");
        }
    }

    @GetMapping("/user/{customerId}")
    public ResponseEntity<List<SupportTicketDTO>> getUserTickets(@PathVariable String customerId) {
        List<SupportTicket> tickets = supportTicketService.getUserTicket(customerId);

        List<SupportTicketDTO> ticketDTOs = tickets.stream()
            .map(ticket -> new SupportTicketDTO(
                ticket.getId(),
                new SupportTicketDTO.Customer(ticket.getCustomer() != null ? ticket.getCustomer().getId() : null),
                ticket.getTitle(),
                ticket.getContent(),
                ticket.getStatus().name()
            ))
            .collect(Collectors.toList());

        return ResponseEntity.ok(ticketDTOs);
    }

    @GetMapping("/all")
    public ResponseEntity<List<SupportTicketDTO>> getAllTickets() {
        List<SupportTicket> tickets = supportTicketService.getAllTickets();
        List<SupportTicketDTO> ticketDTOs = tickets.stream()
            .map(ticket -> new SupportTicketDTO(
                ticket.getId(),
                new SupportTicketDTO.Customer(ticket.getCustomer() != null ? ticket.getCustomer().getId() : null),
                ticket.getTitle(),
                ticket.getContent(),
                ticket.getStatus().name()
            ))
            .collect(Collectors.toList());
        return ResponseEntity.ok(ticketDTOs);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateTicketStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            supportTicketService.updateTicketStatus(id, status);
            return ResponseEntity.ok("Trạng thái yêu cầu đã được cập nhật");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST)
                    .body("Trạng thái không hợp lệ");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/{id}/again")
    public ResponseEntity<String> requestTicketAgain(@PathVariable Long id, @RequestBody SupportTicketDTO supportTicketDTO) {
        try {

            String customerId = supportTicketDTO.getCustomer().getId();

            if (customerId == null || customerId.isEmpty()) {
                return ResponseEntity.badRequest().body("Không tìm thấy thông tin khách hàng.");
            }

            userRepository.findById(customerId).orElseThrow(() -> new UserNotFoundException("Không tìm thấy người dùng!"));
            
            Optional<SupportTicket> optionalTicket = supportTicketService.getTicketById(id);

            List<SupportTicket> openTickets = supportTicketService.getUserTicket(customerId);

            boolean hasOpenTicket = openTickets.stream().anyMatch(ticket -> ticket.getStatus() == SupportTicketStatus.OPEN);

            if (hasOpenTicket) {
                return ResponseEntity.badRequest().body("Bạn đã gửi một yêu cầu hỗ trợ chưa được phản hồi.");
            }

            if (!optionalTicket.isPresent()) {
                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND)
                    .body("Không tìm thấy yêu cầu hỗ trợ.");
            }

            SupportTicket ticket = optionalTicket.get();

            if (ticket.getStatus() == SupportTicketStatus.OPEN) {
                return ResponseEntity.badRequest().body("Yêu cầu hỗ trợ này bạn đã gửi");
            }

            if (ticket.getStatus() == SupportTicketStatus.RESOLVED) {   
                return ResponseEntity.badRequest().body("Yêu cầu hỗ trợ này đã được xử lý");
            }

            SupportTicketDTO newTicketDTO = new SupportTicketDTO(
                null, 
                new SupportTicketDTO.Customer(ticket.getCustomer().getId()), 
                ticket.getTitle(), 
                ticket.getContent(), 
                SupportTicketStatus.OPEN.name() 
            );

            supportTicketService.createTicket(newTicketDTO);

            return ResponseEntity.ok("Yêu cầu hỗ trợ đã được gửi lại.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .body("Có lỗi xảy ra trong quá trình mở lại yêu cầu hỗ trợ.");
        }
    }
        
}
