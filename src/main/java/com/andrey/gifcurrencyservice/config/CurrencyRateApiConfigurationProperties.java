package com.andrey.gifcurrencyservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "feign-client.currency-api.options")
public class CurrencyRateApiConfigurationProperties {
	private String latest;
	private String historical;
	private String appId;
	private String objectiveCurrencyCode;
}
