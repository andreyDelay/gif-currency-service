package com.andrey.gifcurrencyservice.service.impl;

import com.andrey.gifcurrencyservice.exception.GifFetchingException;
import com.andrey.gifcurrencyservice.model.CurrencyRatesDynamic;
import com.andrey.gifcurrencyservice.model.GiphyImage;
import com.andrey.gifcurrencyservice.model.GiphyResponseList;
import com.andrey.gifcurrencyservice.service.FeignService;
import com.andrey.gifcurrencyservice.service.GifService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
@RequiredArgsConstructor
public class GifServiceImpl implements GifService {

	private final FeignService feignService;

	@Override
	public String getGifUrlByCurrencyDynamic(CurrencyRatesDynamic currencyDynamic) {
		log.info("Grabbing random object from Giphy collection for currency dynamic:{}.", currencyDynamic);
		GiphyResponseList giphyResponseList;
		switch (currencyDynamic) {
			case POSITIVE -> giphyResponseList = feignService.getPositiveGiphyCollection(currencyDynamic);
			case NEGATIVE -> giphyResponseList = feignService.getNegativeGiphyCollection(currencyDynamic);
			default -> throw new GifFetchingException(
					String.format("Unexpected behaviour, invalid currency dynamic value %s.",
							currencyDynamic.getCurrencyRatesRelationDynamicPerformance()));
		}

		int gifsQty = giphyResponseList.getGiphyRootCollection().size();
		int randomGifObjectIndex = getRandomImageIndex(gifsQty);

		return giphyResponseList
				.getGiphyRootCollection()
				.get(randomGifObjectIndex)
				.getImagesMap()
				.values().stream()
				.map(GiphyImage::getGifFormatURL)
				.findFirst()
				.orElseThrow(() -> new RuntimeException(""));
	}

	private int getRandomImageIndex(int gifsQty) {
		return ThreadLocalRandom.current().nextInt(0, gifsQty);
	}
}
