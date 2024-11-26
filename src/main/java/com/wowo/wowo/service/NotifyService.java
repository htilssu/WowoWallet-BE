package com.wowo.wowo.service;

import com.pusher.rest.Pusher;
import com.pusher.rest.PusherAsync;
import com.pusher.rest.data.Result;
import com.wowo.wowo.data.dto.NotifyDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@AllArgsConstructor
public class NotifyService {

    private final Pusher pusher;
    private final PusherAsync asyncPusher;

    public CompletableFuture<Result> notifyReceiveMoney(String userId, NotifyDTO notifyDTO) throws
                                                                                            ExecutionException,
                                                                                            InterruptedException {
        final CompletableFuture<Result> trigger = asyncPusher.trigger(userId,
                "notification",
                notifyDTO);

        return CompletableFuture.completedFuture(trigger.get());
    }
}