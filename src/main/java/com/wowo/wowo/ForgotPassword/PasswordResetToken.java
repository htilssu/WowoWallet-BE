package com.wowo.wowo.ForgotPassword;
import lombok.Getter;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
@Getter
@Setter
public class PasswordResetToken {
    private String userId;
    private String token;
    private long expirationTime;
    @DynamoDbPartitionKey
    public String getToken() {
        return token;
    }


}
