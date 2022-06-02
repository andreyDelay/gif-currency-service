package com.andrey.gifcurrencyservice.service;

import com.andrey.gifcurrencyservice.model.ApiRates;

import java.time.LocalDate;

public interface FeignService {
	ApiRates getRates();
	ApiRates getRates(LocalDate specifiedDate);
}
