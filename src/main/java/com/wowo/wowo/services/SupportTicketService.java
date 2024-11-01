package com.wowo.wowo.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wowo.wowo.models.SupportTicket;
import com.wowo.wowo.models.SupportTicketStatus;
import com.wowo.wowo.repositories.SupportTicketRepository;

@Service
public class SupportTicketService {

    @Autowired
    private SupportTicketRepository supportTicketRepository;

    public void createTicket(SupportTicket ticket) {
        supportTicketRepository.save(ticket);
    }

    public List<SupportTicket> getUserTicket(String customerId) {
        return supportTicketRepository.findByCustomer_Id(customerId);
    }

    public Optional<SupportTicket> getTicketById(Long id) {
        return supportTicketRepository.findById(id);
    }

    public List<SupportTicket> getAllTickets() {
        return supportTicketRepository.findAll();
    }
    
    public void updateTicketStatus(Long id, String status) {
        SupportTicket ticket = supportTicketRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy ticket hỗ trợ!"));
        
        try {
            SupportTicketStatus newStatus = SupportTicketStatus.valueOf(status);
            ticket.setStatus(newStatus);
            supportTicketRepository.save(ticket);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Trạng thái không hợp lệ");
        }
    }
}
