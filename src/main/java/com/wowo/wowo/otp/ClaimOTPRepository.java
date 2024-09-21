package com.wowo.wowo.otp;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
@Service
public class ClaimOTPRepository {

    DynamoDbAsyncTable<ClaimOTPModel> claimTable;

    @Async
    public CompletableFuture<Void> save(ClaimOTPModel claimOTPModel) {
        return claimTable.putItem(claimOTPModel);
    }

    @Async
    public CompletableFuture<ClaimOTPModel> deleteByUserId(String id) {
        Key key = Key.builder().partitionValue(id).build();
        return claimTable.deleteItem(key);
    }

    @Async
    public CompletableFuture<ClaimOTPModel> findOtpByUserId(String userId) {
        Key key = Key.builder().partitionValue(userId).build();
        return claimTable.getItem(key);
    }

}
