package com.andrey.gifcurrencyservice.service.impl;

import com.andrey.gifcurrencyservice.config.CurrencyRateApiConfigurationProperties;
import com.andrey.gifcurrencyservice.exception.CurrencyFeignClientResponseException;
import com.andrey.gifcurrencyservice.exception.CurrencyNotFoundException;
import com.andrey.gifcurrencyservice.feign.CurrencyFeignClientAPI;
import com.andrey.gifcurrencyservice.model.ApiRates;
import com.andrey.gifcurrencyservice.model.CurrencyRate;
import com.andrey.gifcurrencyservice.service.RateService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@AllArgsConstructor
public class RateServiceImpl implements RateService {

	private final CurrencyFeignClientAPI currencyFeignClientAPI;
	private final CurrencyRateApiConfigurationProperties apiConfiguration;

	@Override
	public CurrencyRate getRateByCodeLatest(String currencyCode) {
		ApiRates apiRates = currencyFeignClientAPI.getRates(
						apiConfiguration.getLatest(),
						apiConfiguration.getAppId(),
						apiConfiguration.getObjectiveCurrencyCode())
				.orElseThrow(() -> new CurrencyFeignClientResponseException("Couldn't get a response from API."));

		return fetchCurrencyRateFromApiRates(apiRates, currencyCode);
	}

	@Override
	public CurrencyRate getRateByCodeForSpecifiedDate(String currencyCode, LocalDate specifiedDate) {
		ApiRates apiRates = currencyFeignClientAPI.getRatesForSpecifiedData(
						apiConfiguration.getHistorical(),
						getApiSpecifiedDateFormat(specifiedDate),
						apiConfiguration.getAppId(),
						apiConfiguration.getObjectiveCurrencyCode())
				.orElseThrow(() -> new CurrencyFeignClientResponseException(
						String.format("Couldn't get a response from API, for specified date %s .", specifiedDate)));

		return fetchCurrencyRateFromApiRates(apiRates, currencyCode);
	}

	private CurrencyRate fetchCurrencyRateFromApiRates(ApiRates apiRates, String currencyCode) {
		return apiRates.getRates().entrySet().stream()
				.filter(entry -> entry.getKey().equals(currencyCode))
				.map(entry -> new CurrencyRate(entry.getKey(), entry.getValue()))
				.findFirst()
				.orElseThrow(() -> new CurrencyNotFoundException(
						String.format("No currency was found with code %s", currencyCode)));
	}

	private String getApiSpecifiedDateFormat(LocalDate specifiedDate) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return specifiedDate.format(dateTimeFormatter) + ".json";
	}

}
