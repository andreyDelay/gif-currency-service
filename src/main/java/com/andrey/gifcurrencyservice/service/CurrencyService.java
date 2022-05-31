package com.andrey.gifcurrencyservice.service;

import com.andrey.gifcurrencyservice.dto.GifDto;

import java.util.Map;

public interface CurrencyService {
	GifDto getGifBytesOnCurrencyRateCondition(String currencyCode);
}
