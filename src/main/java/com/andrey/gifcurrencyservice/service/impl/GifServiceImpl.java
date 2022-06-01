package com.andrey.gifcurrencyservice.service.impl;

import com.andrey.gifcurrencyservice.config.GifApiConfigurationProperties;
import com.andrey.gifcurrencyservice.exception.GifFeignClientResponseException;
import com.andrey.gifcurrencyservice.feign.GifFeignClientAPI;
import com.andrey.gifcurrencyservice.model.CurrencyDynamic;
import com.andrey.gifcurrencyservice.model.GiphyResponseList;
import com.andrey.gifcurrencyservice.service.GifService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class GifServiceImpl implements GifService {
	private final GifFeignClientAPI gifFeignClientAPI;
	private final GifApiConfigurationProperties apiConfigurationProperties;

	@Override
	public String getGifUrlByCurrencyDynamic(CurrencyDynamic currencyDynamic) {
		GiphyResponseList giphyResponseList = gifFeignClientAPI.getGif(
						apiConfigurationProperties.getApiKey(),
						currencyDynamic.getCurrencyRatesRelationDynamicPerformance(),
						apiConfigurationProperties.getLimit())
				.orElseThrow(() -> new GifFeignClientResponseException("Cannot get a response from giphy API."));

		return getRandomGifURL(giphyResponseList);
	}

	private String getRandomGifURL(GiphyResponseList giphyResponseList) {
		int gifsQty = giphyResponseList.getRootCollection().size();
		int randomGifIndex = getRandomImageIndex(gifsQty);

		return giphyResponseList
				.getRootCollection()
				.get(randomGifIndex)
				.getImages()
				.get(apiConfigurationProperties.getImageObjectName())
				.getGifFormatURL();
	}

	private int getRandomImageIndex(int gifsQty) {
		return ThreadLocalRandom.current().nextInt(1, gifsQty);
	}
}
