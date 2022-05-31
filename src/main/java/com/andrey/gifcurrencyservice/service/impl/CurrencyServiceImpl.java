package com.andrey.gifcurrencyservice.service.impl;

import com.andrey.gifcurrencyservice.model.CurrencyRate;
import com.andrey.gifcurrencyservice.service.CurrencyService;
import com.andrey.gifcurrencyservice.service.GifService;
import com.andrey.gifcurrencyservice.service.RateService;
import lombok.AllArgsConstructor;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;

@Service
@AllArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

	private final RateService rateService;
	private final GifService gifService;

	@Override
	public ResponseBody getGifOnCurrencyRateCondition(String currencyCode) {
		LocalDate yesterday = LocalDate.now().minusDays(1);
		CurrencyRate todayCurrencyRate = rateService.getRateByCodeLatest(currencyCode);
		CurrencyRate yesterdayCurrencyRate = rateService.getRateByCodeForSpecifiedDate(currencyCode, yesterday);

		double todayRateValue = todayCurrencyRate.getCurrencyRate();
		double yesterdayRateValue = yesterdayCurrencyRate.getCurrencyRate();

		String gifUrl;
		if ((todayRateValue - yesterdayRateValue) > 0) {
			gifUrl = gifService.getNegativeGifUrl();
		} else {
			gifUrl = gifService.getPositiveGifUrl();
		}

		return getGifByURL(gifUrl);
	}

	private ResponseBody getGifByURL(String url) {
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
				.url(url)
				.build();
		Call call = client.newCall(request);
		try {
			return call.execute().body();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
