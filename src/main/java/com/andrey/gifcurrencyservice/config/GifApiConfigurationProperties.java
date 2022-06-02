package com.andrey.gifcurrencyservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "feign-client.gif-api.options")
public class GifApiConfigurationProperties {
    private String apiKey;
    private int limit;
    private String specifiedImageObjectName;
    private String imageTypeName;
    private String rootElementName;
    private String targetObjectsCollectionName;
}
