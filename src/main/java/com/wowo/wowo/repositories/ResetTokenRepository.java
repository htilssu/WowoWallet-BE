package com.wowo.wowo.repositories;

import com.wowo.wowo.ForgotPassword.PasswordResetToken;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.util.concurrent.CompletableFuture;

@Service
public class ResetTokenRepository {
    DynamoDbAsyncTable<PasswordResetToken> resetTable;
    @Async
    public CompletableFuture<Void> save(PasswordResetToken PasswordResetToken) {
        return resetTable.putItem(PasswordResetToken);
    }

    @Async
    public CompletableFuture<PasswordResetToken> deleteByUserId(String id) {
        Key key = Key.builder().partitionValue(id).build();
        return resetTable.deleteItem(key);
    }

    @Async
    public CompletableFuture<PasswordResetToken> findTokenByToken(String token) {
        Key key = Key.builder().partitionValue(token).build();
        return resetTable.getItem(key);
    }

}
