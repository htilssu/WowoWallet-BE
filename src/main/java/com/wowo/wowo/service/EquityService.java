package com.wowo.wowo.service;

import com.wowo.wowo.model.Transaction;
import com.wowo.wowo.repository.EquityRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class EquityService {

    private final EquityRepository equityRepository;

    @Async
    public CompletableFuture<Void> updateEquity(Transaction transaction) {

        return null;
    }

}
