package com.wowo.wowo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wowo.wowo.models.SupportTicket;

public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {
    List<SupportTicket> findByCustomer_Id(String customerId);
}
