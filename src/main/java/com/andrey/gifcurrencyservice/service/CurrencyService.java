package com.andrey.gifcurrencyservice.service;

import com.andrey.gifcurrencyservice.dto.GifByteArrayHolder;

public interface CurrencyService {
	GifByteArrayHolder getGifImageOnCurrencyRatesDynamicCondition(String currencyCode);
}
