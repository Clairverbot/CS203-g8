package com.G2T8.CS203WebApp.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class AwsConfig {
  @Value("${aws.access.key}")
  private String awsAccessKey;

  @Value("${aws.secret.key}")
  private String awsSecretKey;
    public AWSStaticCredentialsProvider awsCredentials() {
        BasicAWSCredentials credentials =
                new BasicAWSCredentials(awsAccessKey, awsSecretKey);
        return new AWSStaticCredentialsProvider(credentials);
    }

    @Bean
    public AmazonSimpleEmailService getAmazonSimpleEmailService() {
        return AmazonSimpleEmailServiceClientBuilder.standard().withCredentials(awsCredentials())
                .withRegion("ap-southeast-1").build();
    }
}