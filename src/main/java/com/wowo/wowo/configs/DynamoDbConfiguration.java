package com.wowo.wowo.configs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;
import java.net.URISyntaxException;

//@Configuration
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
    public DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient() throws URISyntaxException {
        return DynamoDbEnhancedAsyncClient.builder().dynamoDbClient(dynamoDbAsyncClient()).build();
    }

    public DynamoDbAsyncClient dynamoDbAsyncClient() throws URISyntaxException {
        return DynamoDbAsyncClient.builder()
                .region(Region.AP_SOUTHEAST_1)
                .credentialsProvider(this::amazonAWSCredentials)
                .endpointOverride(new URI(endpoint))
                .build();
    }

}
