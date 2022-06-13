package com.andrey.gifcurrencyservice.service;

import com.andrey.gifcurrencyservice.model.CurrencyRatesDynamic;
import com.andrey.gifcurrencyservice.model.GiphyImage;

public interface GifService {
    GiphyImage getGifByCurrencyDynamic(CurrencyRatesDynamic currencyDynamic);
}
