package com.andrey.gifcurrencyservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "feign-client.gif-api.options")
@Getter
@Setter
public class GifApiConfigurationProperties {
    private String apiKey;
    private int limit;
    private String positiveSearchQuery;
    private String negativeSearchQuery;
    private String imageObjectName;
    private String imageTypeName;
}
