package com.wowo.wowo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wowo.wowo.model.SupportTicket;

public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {
    List<SupportTicket> findByCustomer_Id(String customerId);
}
