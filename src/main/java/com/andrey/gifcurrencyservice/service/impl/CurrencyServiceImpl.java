package com.andrey.gifcurrencyservice.service.impl;

import com.andrey.gifcurrencyservice.dto.GifImageDto;
import com.andrey.gifcurrencyservice.model.CurrencyRatesDynamic;
import com.andrey.gifcurrencyservice.model.GiphyImage;
import com.andrey.gifcurrencyservice.service.CurrencyService;
import com.andrey.gifcurrencyservice.service.GifService;
import com.andrey.gifcurrencyservice.service.RateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

	private final RateService rateService;
	private final GifService gifService;

	@Override
	public GifImageDto getGifImageOnCurrencyRatesDynamicCondition(String currencyCode) {
		log.info("Getting rates to determine currency rates dynamic.");
		LocalDate yesterday = LocalDate.now().minusDays(1);
		double currencyRateValueForToday =
				rateService.getCurrencyRate(currencyCode)
						.getCurrencyRate();

		double currencyRateValueForYesterday =
				rateService.getCurrencyRateForSpecifiedDate(currencyCode, yesterday)
						.getCurrencyRate();

		CurrencyRatesDynamic dynamic = determineDynamicType(currencyRateValueForToday, currencyRateValueForYesterday);
		GiphyImage giphyImage = gifService.getGifByCurrencyDynamic(dynamic);

		return GifImageDto.builder()
				.gifFormatURL(giphyImage.getGifFormatURL())
				.mp4FormatURL(giphyImage.getMp4FormatURL())
				.webpFormatURL(giphyImage.getWebpFormatURL())
				.build();
	}

	private CurrencyRatesDynamic determineDynamicType(double todayCurrencyRate, double specifiedDateCurrencyRate) {
		log.info("Determining a currency dynamic.");
		return (todayCurrencyRate - specifiedDateCurrencyRate) > 0 ? CurrencyRatesDynamic.NEGATIVE: CurrencyRatesDynamic.POSITIVE;
	}

}
