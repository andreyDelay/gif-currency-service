package com.andrey.gifcurrencyservice.service.impl;

import com.andrey.gifcurrencyservice.config.CurrencyRateApiConfigurationProperties;
import com.andrey.gifcurrencyservice.exception.CurrencyFeignClientResponseException;
import com.andrey.gifcurrencyservice.feign.CurrencyFeignClientAPI;
import com.andrey.gifcurrencyservice.model.ApiRates;
import com.andrey.gifcurrencyservice.service.FeignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeignServiceImpl implements FeignService {

	private final CurrencyFeignClientAPI currencyFeignClientAPI;
	private final CurrencyRateApiConfigurationProperties apiConfiguration;

	@Override
	@Cacheable(value = "rates")
	public ApiRates getRates() {
		log.info("Getting currency rates collection from feign.");
		return currencyFeignClientAPI.getRates(
						apiConfiguration.getLatest(),
						apiConfiguration.getAppId(),
						apiConfiguration.getObjectiveCurrencyCode())
				.orElseThrow(() -> {
					log.error("Error in class {}, during getting rates collection " +
									"from feign client currency API response.",
								this.getClass().getSimpleName());
					throw new CurrencyFeignClientResponseException("Couldn't get a valid response from API.");
				});
	}

	@Override
	@Cacheable(value = "ratesForDate")
	public ApiRates getRates(LocalDate specifiedDate) {
		log.info("Getting currency rates collection from feign, for specified date: {}.", specifiedDate);
		return currencyFeignClientAPI.getRatesForSpecifiedData(
						apiConfiguration.getHistorical(),
						getApiSpecifiedDateFormat(specifiedDate),
						apiConfiguration.getAppId(),
						apiConfiguration.getObjectiveCurrencyCode())
				.orElseThrow(() -> {
					log.error("Error in class {}, during getting rates collection for specified date:{}," +
									"from feign client currency API response.",
								this.getClass().getSimpleName(), specifiedDate);
					throw new CurrencyFeignClientResponseException(
							String.format("Couldn't get a response from API, for specified date %s .", specifiedDate));
				});
	}

	private String getApiSpecifiedDateFormat(LocalDate specifiedDate) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return specifiedDate.format(dateTimeFormatter) + ".json";
	}
}
