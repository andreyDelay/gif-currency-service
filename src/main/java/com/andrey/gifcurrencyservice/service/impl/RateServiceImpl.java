package com.andrey.gifcurrencyservice.service.impl;

import com.andrey.gifcurrencyservice.exception.CurrencyNotFoundException;
import com.andrey.gifcurrencyservice.model.ApiRates;
import com.andrey.gifcurrencyservice.model.CurrencyRate;
import com.andrey.gifcurrencyservice.service.FeignService;
import com.andrey.gifcurrencyservice.service.RateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class RateServiceImpl implements RateService {

	private final FeignService feignService;
	private final CacheManager cacheManager;

	@Override
	public CurrencyRate getCurrencyRate(String currencyCode) {
		ApiRates rates = feignService.getRates();
		return fetchCurrencyRateFromApiRates(rates, currencyCode);
	}

	@Override
	public CurrencyRate getCurrencyRateForSpecifiedDate(String currencyCode, LocalDate specifiedDate) {
		ApiRates rates = feignService.getRates(specifiedDate);
		return fetchCurrencyRateFromApiRates(rates, currencyCode);
	}

	private CurrencyRate fetchCurrencyRateFromApiRates(ApiRates apiRates, String currencyCode) {
		log.info("Grabbing currency rate for code - {}, from collection of rates.", currencyCode);
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
