package com.wowo.wowo.repository;

import com.wowo.wowo.model.Order;
import com.wowo.wowo.model.PaymentStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface StatisticRepository extends CrudRepository<Order, Long> {

    @Query("SELECT COALESCE(SUM(o.money), 0) FROM Order o WHERE o.application.id = :applicationId AND o.status = :status")
    Long getTotalMoneyByStatus(Long applicationId, PaymentStatus status);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.application.id = :applicationId AND o.status = :status")
    Long getOrderCountByStatus(Long applicationId, PaymentStatus status);
}
