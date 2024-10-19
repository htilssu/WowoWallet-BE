package com.wowo.wowo.models.transaction;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

public class TransactionIdGenerator implements IdentifierGenerator {

    private static final AtomicLong counter = new AtomicLong(0);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    @Override
    public Serializable generate(SharedSessionContractImplementor session,
            Object object) {
        String timestamp = dateFormat.format(new Date());

        long sequenceNumber = counter.incrementAndGet();

        if (sequenceNumber > 100) {
            counter.set(0);
        }

        return timestamp + String.format("%04d", sequenceNumber);
    }
}
