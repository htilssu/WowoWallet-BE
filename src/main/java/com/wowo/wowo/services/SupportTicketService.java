package com.wowo.wowo.services;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wowo.wowo.data.dto.SupportTicketDto;
import com.wowo.wowo.exceptions.UserNotFoundException;
import com.wowo.wowo.models.SupportTicket;
import com.wowo.wowo.models.SupportTicketStatus;
import com.wowo.wowo.models.User;
import com.wowo.wowo.repositories.SupportTicketRepository;
import com.wowo.wowo.repositories.UserRepository;

@Service
public class SupportTicketService {

    @Autowired
    private SupportTicketRepository supportTicketRepository;

    @Autowired
    private UserRepository userRepository;

    public void createTicket(SupportTicketDto supportTicketDto) {
        String customerId = supportTicketDto.getCustomer().getId(); 

        User user = userRepository.findById(customerId)
            .orElseThrow(() -> new UserNotFoundException("Không tìm thấy người dùng!"));

        SupportTicket ticket = new SupportTicket();
        ticket.setCustomer(user);
        ticket.setTitle(supportTicketDto.getTitle());
        ticket.setContent(supportTicketDto.getContent());
        ticket.setStatus(SupportTicketStatus.OPEN);

        supportTicketRepository.save(ticket);
    }

    public List<SupportTicket> getUserTicket(String customerId) {
        User user = userRepository.findById(customerId)
            .orElseThrow(() -> new UserNotFoundException("Không tìm thấy người dùng!"));
        return supportTicketRepository.findByCustomer_Id(user.getId());
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
