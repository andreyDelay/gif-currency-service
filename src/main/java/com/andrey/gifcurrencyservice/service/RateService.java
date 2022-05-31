package com.andrey.gifcurrencyservice.service;

import com.andrey.gifcurrencyservice.model.CurrencyRate;

import java.time.LocalDate;

public interface RateService {
	CurrencyRate getRateByCodeLatest(String currencyCode);
	CurrencyRate getRateByCodeForSpecifiedDate(String currencyCode, LocalDate specifiedDate);
}
