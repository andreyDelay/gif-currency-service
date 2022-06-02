package com.andrey.gifcurrencyservice.service.impl;

import com.andrey.gifcurrencyservice.config.CurrencyRateApiConfigurationProperties;
import com.andrey.gifcurrencyservice.exception.CurrencyFeignClientResponseException;
import com.andrey.gifcurrencyservice.exception.CurrencyNotFoundException;
import com.andrey.gifcurrencyservice.feign.CurrencyFeignClientAPI;
import com.andrey.gifcurrencyservice.model.ApiRates;
import com.andrey.gifcurrencyservice.model.CurrencyRate;
import com.andrey.gifcurrencyservice.service.RateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class RateServiceImpl implements RateService {
	
	private final CurrencyFeignClientAPI currencyFeignClientAPI;
	private final CurrencyRateApiConfigurationProperties rateApiConfigurationProperties;

	@Override
	public CurrencyRate getCurrencyRate(String currencyCode) {
		ApiRates rates = getRates();
		return fetchCurrencyRateFromApiRates(rates, currencyCode);
	}

	@Override
	public CurrencyRate getCurrencyRateForSpecifiedDate(String currencyCode, LocalDate specifiedDate) {
		ApiRates rates = getRates(specifiedDate);
		return fetchCurrencyRateFromApiRates(rates, currencyCode);
	}

	private ApiRates getRates() {
		log.info("Getting latest currency rates collection from API.");
		return currencyFeignClientAPI.getRates(
						rateApiConfigurationProperties.getLatest(),
						rateApiConfigurationProperties.getAppId(),
						rateApiConfigurationProperties.getObjectiveCurrencyCode())
				.orElseThrow(() -> {
					log.error("Error in class {}, during getting rates collection " +
									"from feign client currency API response.",
							this.getClass().getSimpleName());
					throw new CurrencyFeignClientResponseException("Couldn't get a valid response from API.");
				});
	}

	private ApiRates getRates(LocalDate specifiedDate) {
		log.info("Getting currency rates collection from API, for specified date: {}.", specifiedDate);
		return currencyFeignClientAPI.getRatesForSpecifiedData(
						rateApiConfigurationProperties.getHistorical(),
						getApiSpecifiedDateFormat(specifiedDate),
						rateApiConfigurationProperties.getAppId(),
						rateApiConfigurationProperties.getObjectiveCurrencyCode())
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

	private CurrencyRate fetchCurrencyRateFromApiRates(ApiRates apiRates, String currencyCode) {
		log.info("Grabbing currency rate of code - {}, from collection of rates.", currencyCode);
		return apiRates.getRates().entrySet().stream()
				.filter(entry -> entry.getKey().equals(currencyCode))
				.map(entry -> new CurrencyRate(entry.getKey(), entry.getValue()))
				.findFirst()
				.orElseThrow(() -> {
					log.error("Error in class {}, during grabbing currency code {}, it was not found in collection.",
							this.getClass().getSimpleName(), currencyCode);
					throw new CurrencyNotFoundException(String.format("No code %s was found.", currencyCode));
				});
	}

}
