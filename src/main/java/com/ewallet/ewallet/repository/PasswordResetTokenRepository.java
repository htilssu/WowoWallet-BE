package com.ewallet.ewallet.repository;

import com.ewallet.ewallet.ForgotPassword.PasswordResetToken;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
@Repository
public class PasswordResetTokenRepository {
    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;

    public PasswordResetTokenRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        this.dynamoDbEnhancedClient = dynamoDbEnhancedClient;
    }

    public void saveToken(PasswordResetToken token) {
        DynamoDbTable<PasswordResetToken> table = dynamoDbEnhancedClient.table("PasswordResetTokens", PasswordResetToken.class);
        table.putItem(token);
    }

    public PasswordResetToken findByUserId(String userId) {
        DynamoDbTable<PasswordResetToken> table = dynamoDbEnhancedClient.table("PasswordResetTokens", PasswordResetToken.class);
        return table.getItem(Key.builder().partitionValue(userId).build());
    }

    public void deleteToken(String userId) {
        DynamoDbTable<PasswordResetToken> table = dynamoDbEnhancedClient.table("PasswordResetTokens", PasswordResetToken.class);
        table.deleteItem(Key.builder().partitionValue(userId).build());
    }
}
