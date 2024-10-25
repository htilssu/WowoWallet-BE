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
    SupportTicketRepository  supportTicketRepository;
    
    public void createTicket(SupportTicket ticket){
        supportTicketRepository.save(ticket);
    }

    public List<SupportTicket> getUserTicket(String customerId){
        return supportTicketRepository.findByCustomer_Id(customerId);
    }

    public Optional<SupportTicket> getTicketById(String id) {
        return supportTicketRepository.findById(id);
    }

    public void updateTicketStatus(String id, String status){
        SupportTicket ticket = supportTicketRepository.findById(id).orElseThrow(()  -> new RuntimeException("Không tìm thấy ticket hõ trợ!!!"));
        ticket.setStatus(SupportTicketStatus.valueOf(status));
        supportTicketRepository.save(ticket);
    } 
}
