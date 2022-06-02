package com.andrey.gifcurrencyservice.service;

import com.andrey.gifcurrencyservice.model.CurrencyRate;

import java.time.LocalDate;

public interface RateService {
	CurrencyRate getCurrencyRate(String currencyCode);
	CurrencyRate getCurrencyRateForSpecifiedDate(String currencyCode, LocalDate specifiedDate);
}
