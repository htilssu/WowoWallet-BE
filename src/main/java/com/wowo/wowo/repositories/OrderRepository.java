package com.wowo.wowo.repositories;

import com.wowo.wowo.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}