package com.wowo.wowo.configs;

import com.wowo.wowo.otp.ClaimOTPModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticAttributeTags;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticTableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DynamoDbConfiguration {

    @Value("${aws.dynamodb.endpoint}")
    private String endpoint;
    @Value("${aws.secret.access.key}")
    private String accessKey;
    @Value("${aws.access.key.id}")
    private String keyId;

    public DynamoDbClient dynamoDbClient() throws URISyntaxException {
        return DynamoDbClient.builder()
                .region(Region.AP_SOUTHEAST_1)
                .credentialsProvider(this::amazonAWSCredentials)
                .endpointOverride(new URI(endpoint))
                .build();
    }

    public AwsCredentials amazonAWSCredentials() {
        return AwsBasicCredentials.create(getKeyId(), getAccessKey());
    }

    @Bean
    public DynamoDbAsyncTable<ClaimOTPModel> claimOTPModelDynamoDbAsyncTable() throws
                                                                               URISyntaxException {
        var claimOtpModel = dynamoDbEnhancedAsyncClient().table("claim_otp",
                claimOTPModelTableSchema()
        );
        claimOtpModel.createTable().thenAccept(unused -> {
            System.out.println("Table created");
        });

        return claimOtpModel;
    }

    @Bean
    public DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient() throws URISyntaxException {
        return DynamoDbEnhancedAsyncClient.builder().dynamoDbClient(dynamoDbAsyncClient()).build();
    }

    @Bean
    public TableSchema<ClaimOTPModel> claimOTPModelTableSchema() {
        return StaticTableSchema.builder(ClaimOTPModel.class)
                .newItemSupplier(ClaimOTPModel::new)
                .addAttribute(String.class, a -> {
                    a.name("userId")
                            .getter(ClaimOTPModel::getUserId)
                            .setter(ClaimOTPModel::setUserId).tags(
                                    StaticAttributeTags.primaryPartitionKey());
                })
                .addAttribute(String.class, a -> {
                    a.name("otp")
                            .getter(ClaimOTPModel::getOtp)
                            .setter(ClaimOTPModel::setOtp);
                })
                .addAttribute(String.class, a -> {
                    a.name("expiredAt")
                            .getter(ClaimOTPModel::getExpiredAt)
                            .setter(ClaimOTPModel::setExpiredAt);
                }).build();
    }

    public DynamoDbAsyncClient dynamoDbAsyncClient() throws URISyntaxException {
        return DynamoDbAsyncClient.builder()
                .region(Region.AP_SOUTHEAST_1)
                .credentialsProvider(this::amazonAWSCredentials)
                .endpointOverride(new URI(endpoint))
                .build();
    }

}
