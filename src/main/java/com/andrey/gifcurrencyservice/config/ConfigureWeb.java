package com.andrey.gifcurrencyservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ConfigureWeb implements WebMvcConfigurer {
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        WebMvcConfigurer.super.configureContentNegotiation(configurer);
        configurer.ignoreAcceptHeader(true)
                .defaultContentType(MediaType.APPLICATION_JSON, MediaType.IMAGE_GIF);
    }
}
