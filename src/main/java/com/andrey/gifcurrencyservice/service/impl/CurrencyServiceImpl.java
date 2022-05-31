package com.andrey.gifcurrencyservice.service.impl;

import com.andrey.gifcurrencyservice.exception.GifFetchingException;
import com.andrey.gifcurrencyservice.model.CurrencyDynamic;
import com.andrey.gifcurrencyservice.service.CurrencyService;
import com.andrey.gifcurrencyservice.service.GifService;
import com.andrey.gifcurrencyservice.service.RateService;
import lombok.AllArgsConstructor;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
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
		double currencyRateValueForToday =
				rateService.getRateByCodeLatest(currencyCode).getCurrencyRate();
		double currencyRateValueForYesterday =
				rateService.getRateByCodeForSpecifiedDate(currencyCode, yesterday).getCurrencyRate();

		CurrencyDynamic dynamic = determineDynamicType(currencyRateValueForToday, currencyRateValueForYesterday);
		String targetGifUrl = gifService.getGifUrlByCurrencyDynamic(dynamic);

		return getMediaTypeResponseBodyByURL(targetGifUrl);
	}

	private CurrencyDynamic determineDynamicType(double currencyRateValueForToday, double currencyRateValueForYesterday) {
		double dynamic = currencyRateValueForToday - currencyRateValueForYesterday;
		if (dynamic > 0) {
			return CurrencyDynamic.NEGATIVE;
		}

		return CurrencyDynamic.POSITIVE;
	}

	private ResponseBody getMediaTypeResponseBodyByURL(String url) {
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
				.url(url)
				.build();
		Call call = client.newCall(request);
		try {
			return call.execute().body();
		} catch (IOException e) {
			throw new GifFetchingException(e.getMessage());
		}
	}

}
