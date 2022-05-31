package com.andrey.gifcurrencyservice.service;

import okhttp3.ResponseBody;

public interface CurrencyService {
	ResponseBody getGifOnCurrencyRateCondition(String currencyCode);
}
