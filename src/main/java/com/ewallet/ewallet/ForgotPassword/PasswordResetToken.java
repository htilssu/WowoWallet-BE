package com.ewallet.ewallet.ForgotPassword;

import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Data
@DynamoDbBean
public class PasswordResetToken { private String userId;
    private String token;
    private Long expirationTime; // TTL để tự động hết hạn token

    @DynamoDbPartitionKey
    public String getUserId() {
        return userId;
    }

}
