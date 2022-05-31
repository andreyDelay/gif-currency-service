package com.andrey.gifcurrencyservice.service;

import com.andrey.gifcurrencyservice.model.CurrencyDynamic;

public interface GifService {
    String getGifUrlByCurrencyDynamic(CurrencyDynamic currencyDynamic);
}
