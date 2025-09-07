package com.wowo.wowo.domain.order.valueobjects;

import lombok.Value;
import java.util.UUID;

/**
 * Order ID value object
 */
@Value
public class OrderId {
    Long value;
    
    public static OrderId generate() {
        return new OrderId(Math.abs(UUID.randomUUID().getMostSignificantBits()));
    }
    
    public static OrderId of(Long id) {
        return new OrderId(id);
    }
}