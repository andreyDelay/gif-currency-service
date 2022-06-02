package com.andrey.gifcurrencyservice.service.impl;

import com.andrey.gifcurrencyservice.dto.GifByteArrayHolder;
import com.andrey.gifcurrencyservice.exception.GifFetchingException;
import com.andrey.gifcurrencyservice.model.CurrencyDynamic;
import com.andrey.gifcurrencyservice.service.CurrencyService;
import com.andrey.gifcurrencyservice.service.GifService;
import com.andrey.gifcurrencyservice.service.RateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

	private final RateService rateService;
	private final GifService gifService;
	private final OkHttpClient okHttpClient = new OkHttpClient();

	@Override
	public GifByteArrayHolder getGifOnCurrencyRateCondition(String currencyCode) {
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

	private GifByteArrayHolder getMediaTypeResponseBodyByURL(String url) {
		log.info("Getting target image/gif byte array from http responseBody.");
		Request request = new Request.Builder().url(url).build();
		Call call = okHttpClient.newCall(request);
		try(Response response = call.execute()) {
			byte[] bytes = IOUtils.toByteArray(response.body().byteStream());
			return new GifByteArrayHolder(bytes);
		} catch (Exception e) {
			log.error("Error during getting target GIF image through http request for URL:{}, error:{}", url, e);
			throw new GifFetchingException(e.getMessage());
		}
	}

}
