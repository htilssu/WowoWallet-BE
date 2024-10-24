package com.wowo.wowo.mongo.repositories;

import com.wowo.wowo.mongo.documents.OrderItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;

public interface OrderItemRepository extends MongoRepository<OrderItem, String> {

    Collection<OrderItem> findByOrderId(Long orderId);
}
