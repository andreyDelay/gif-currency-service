package com.andrey.gifcurrencyservice.service;

import com.andrey.gifcurrencyservice.dto.GifImageDto;

public interface CurrencyService {
	GifImageDto getGifImageOnCurrencyRatesDynamicCondition(String currencyCode);
}
