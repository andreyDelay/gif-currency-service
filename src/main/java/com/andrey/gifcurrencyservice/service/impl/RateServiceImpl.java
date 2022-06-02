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
	private final CurrencyRateApiConfigurationProperties apiConfiguration;

	@Override
	public CurrencyRate getRateByCodeLatest(String currencyCode) {
		log.info("Getting currency rates collection from feign.");
		ApiRates apiRates = currencyFeignClientAPI.getRates(
						apiConfiguration.getLatest(),
						apiConfiguration.getAppId(),
						apiConfiguration.getObjectiveCurrencyCode())
				.orElseThrow(() -> {
					log.error("Error in class {}, method {}, during feign client currency API response processing.",
							this.getClass().getSimpleName(), this.getClass().getEnclosingMethod().getName());
					throw new CurrencyFeignClientResponseException("Couldn't get a response from API.");
				});

		return fetchCurrencyRateFromApiRates(apiRates, currencyCode);
	}

	@Override
	public CurrencyRate getRateByCodeForSpecifiedDate(String currencyCode, LocalDate specifiedDate) {
		log.info("Getting currency rates collection from feign, for specified date: {}.", specifiedDate);
		ApiRates apiRates = currencyFeignClientAPI.getRatesForSpecifiedData(
						apiConfiguration.getHistorical(),
						getApiSpecifiedDateFormat(specifiedDate),
						apiConfiguration.getAppId(),
						apiConfiguration.getObjectiveCurrencyCode())
				.orElseThrow(() -> {
					log.error("Error in class {}, method {}, during feign client currency API response processing.",
							this.getClass().getSimpleName(), this.getClass().getEnclosingMethod().getName());
					throw new CurrencyFeignClientResponseException(
							String.format("Couldn't get a response from API, for specified date %s .", specifiedDate));
				});

		return fetchCurrencyRateFromApiRates(apiRates, currencyCode);
	}

	private CurrencyRate fetchCurrencyRateFromApiRates(ApiRates apiRates, String currencyCode) {
		log.info("Grabbing currency rate for code - {}, from collection of rates.", currencyCode);
		return apiRates.getRates().entrySet().stream()
				.filter(entry -> entry.getKey().equals(currencyCode))
				.map(entry -> new CurrencyRate(entry.getKey(), entry.getValue()))
				.findFirst()
				.orElseThrow(() -> {
					log.error("Error in class {}, method {}, currency code {} not found.",
							this.getClass().getSimpleName(),
							this.getClass().getEnclosingMethod().getName(),
							currencyCode);
					throw new CurrencyNotFoundException(String.format("No code %s was found.", currencyCode));
				});
	}

	private String getApiSpecifiedDateFormat(LocalDate specifiedDate) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return specifiedDate.format(dateTimeFormatter) + ".json";
	}

}
