package com.wowo.wowo.otp;

import com.wowo.wowo.util.DateTimeUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbIgnore;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@DynamoDbBean
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaimOTPModel {

    private String otp;
    @Getter(onMethod_ = {@DynamoDbPartitionKey})
    private String userId;
    private String expiredAt = DateTimeUtil.convertToString(
            Instant.now().plus(1, ChronoUnit.HOURS));

    @DynamoDbIgnore
    public boolean isExpired() {
        return Instant.now().isAfter(DateTimeUtil.convertToDate(expiredAt).toInstant());
    }
}
