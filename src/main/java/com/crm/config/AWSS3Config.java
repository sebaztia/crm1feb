package com.crm.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSS3Config {
    // Access key id will be read from the application.properties file during the application intialization.
    @Value("${s3bucketAccessKey}")
    private String accessKeyId;
    // Secret access key will be read from the application.properties file during the application intialization.
    @Value("${s3bucketSecretKey}")
    private String secretAccessKey;
    // Region will be read from the application.properties file  during the application intialization.
   /* @Value("${aws.s3.region}")
    private String region;*/

    @Bean
    public AmazonS3 getAmazonS3Cient() {
        final BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);
        // Get AmazonS3 client and return the s3Client object.
        return AmazonS3ClientBuilder
                .standard()
                .withRegion(Regions.EU_WEST_2)
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                .build();
    }
}
