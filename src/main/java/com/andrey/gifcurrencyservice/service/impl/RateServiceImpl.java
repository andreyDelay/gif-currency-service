package com.andrey.gifcurrencyservice.service.impl;

import com.andrey.gifcurrencyservice.config.CurrencyRateApiConfiguration;
import com.andrey.gifcurrencyservice.exception.CurrencyFeignClientResponseException;
import com.andrey.gifcurrencyservice.exception.CurrencyNotFoundException;
import com.andrey.gifcurrencyservice.feign.CurrencyFeignClientAPI;
import com.andrey.gifcurrencyservice.model.ApiRates;
import com.andrey.gifcurrencyservice.model.CurrencyRate;
import com.andrey.gifcurrencyservice.service.RateService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Service
@AllArgsConstructor
public class RateServiceImpl implements RateService {

	private final CurrencyFeignClientAPI currencyFeignClientAPI;
	private final CurrencyRateApiConfiguration apiConfiguration;

	@Override
	public CurrencyRate getRateByCodeLatest(String currencyCode) {
		ApiRates apiRates =
				currencyFeignClientAPI.getRates(apiConfiguration.getLatest(), apiConfiguration.getAppID())
				.orElseThrow(() -> new CurrencyFeignClientResponseException("Couldn't get a response from API."));

		return fetchCurrencyRateFromApiRates(apiRates, currencyCode);
	}

	@Override
	public CurrencyRate getRateByCodeForSpecifiedDate(String currencyCode, Date specifiedDate) {
		ApiRates apiRates = currencyFeignClientAPI.getRatesForSpecifiedData(
						apiConfiguration.getHistorical(),
						getApiSpecifiedDateFormat(specifiedDate),
						apiConfiguration.getAppID())
				.orElseThrow(() -> new CurrencyFeignClientResponseException(
						String.format("Couldn't get a response from API, for specified date %s .", specifiedDate)));

		return fetchCurrencyRateFromApiRates(apiRates, currencyCode);
	}

	private CurrencyRate fetchCurrencyRateFromApiRates(ApiRates apiRates, String currencyCode) {
		return apiRates.getRates().stream()
				.filter(currentRate -> currentRate.getCurrencyCode().equals(currencyCode))
				.findFirst()
				.orElseThrow(() -> new CurrencyNotFoundException(
						String.format("No currency was found with code %s", currencyCode)));
	}

	private String getApiSpecifiedDateFormat(Date specifiedDate) {
		LocalDate localDate = specifiedDate.toInstant()
				.atZone(ZoneId.systemDefault())
				.toLocalDate();
		StringBuilder sb = new StringBuilder();

		return sb
				.append(localDate.getYear())
				.append(localDate.getMonth())
				.append(localDate.getDayOfMonth())
				.append(".json")
				.toString();
	}

}
