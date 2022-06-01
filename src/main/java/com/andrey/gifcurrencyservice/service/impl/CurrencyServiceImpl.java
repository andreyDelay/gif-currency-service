package com.andrey.gifcurrencyservice.service.impl;

import com.andrey.gifcurrencyservice.exception.GifFetchingException;
import com.andrey.gifcurrencyservice.model.CurrencyDynamic;
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
	private final OkHttpClient okHttpClient = new OkHttpClient();

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

	private CurrencyDynamic determineDynamicType(double todayCurrencyRate, double specifiedDateCurrencyRate) {
		return (todayCurrencyRate - specifiedDateCurrencyRate) > 0 ? CurrencyDynamic.NEGATIVE: CurrencyDynamic.POSITIVE;
	}

	private ResponseBody getMediaTypeResponseBodyByURL(String url) {
		Request request = new Request.Builder().url(url).build();
		Call call = okHttpClient.newCall(request);
		try(Response response = call.execute()) {
			return response.body();
		} catch (IOException e) {
			throw new GifFetchingException(e.getMessage());
		}
	}

}
