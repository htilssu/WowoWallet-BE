package com.wowo.wowo.repository;

import com.wowo.wowo.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByApplication_IdOrderByUpdatedDesc(Long applicationId, Pageable pageable);
}