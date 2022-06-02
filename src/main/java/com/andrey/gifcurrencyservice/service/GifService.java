package com.andrey.gifcurrencyservice.service;

import com.andrey.gifcurrencyservice.model.CurrencyRatesDynamic;

public interface GifService {
    String getGifUrlByCurrencyDynamic(CurrencyRatesDynamic currencyDynamic);
}
