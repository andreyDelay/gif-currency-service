package com.andrey.gifcurrencyservice.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "feign-client.currency-api.options")
@Getter
public class CurrencyRateApiConfiguration {
	private String latest;
	private String historical;
	private String appID;
}
