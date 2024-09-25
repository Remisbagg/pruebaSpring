package com.olympus.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "firebase")
public class FirebaseConfig {
    private String bucketName;
    private String imageUrl;
}
