package com.andrey.gifcurrencyservice.service;

import com.andrey.gifcurrencyservice.model.ApiRates;
import com.andrey.gifcurrencyservice.model.CurrencyRatesDynamic;
import com.andrey.gifcurrencyservice.model.GiphyResponseList;

import java.time.LocalDate;

public interface FeignService {
	ApiRates getRates();
	ApiRates getRates(LocalDate specifiedDate);
	GiphyResponseList getPositiveGiphyCollection(CurrencyRatesDynamic currencyDynamic);
	GiphyResponseList getNegativeGiphyCollection(CurrencyRatesDynamic currencyDynamic);
}
