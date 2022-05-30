package com.andrey.gifcurrencyservice.service.impl;

import com.andrey.gifcurrencyservice.model.CurrencyRate;
import com.andrey.gifcurrencyservice.service.CurrencyService;
import com.andrey.gifcurrencyservice.service.GifService;
import com.andrey.gifcurrencyservice.service.RateService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

	private final RateService rateService;
	private final GifService gifService;

	@Override
	public String getGifUrlOnCurrencyRateCondition(String currencyCode) {
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
		return gifUrl;
	}
}
