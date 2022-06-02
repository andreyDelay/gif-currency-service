package com.andrey.gifcurrencyservice.service.impl;

import com.andrey.gifcurrencyservice.config.GifApiConfigurationProperties;
import com.andrey.gifcurrencyservice.exception.GifFeignClientResponseException;
import com.andrey.gifcurrencyservice.feign.GifFeignClientAPI;
import com.andrey.gifcurrencyservice.model.CurrencyDynamic;
import com.andrey.gifcurrencyservice.model.GiphyResponseList;
import com.andrey.gifcurrencyservice.service.GifService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
@RequiredArgsConstructor
public class GifServiceImpl implements GifService {
	private final GifFeignClientAPI gifFeignClientAPI;
	private final GifApiConfigurationProperties apiConfigurationProperties;

	@Override
	public String getGifUrlByCurrencyDynamic(CurrencyDynamic currencyDynamic) {
		log.info("Getting collection from Giphy through feign, for search tag 'g='{}.",
				currencyDynamic.getCurrencyRatesRelationDynamicPerformance());

		GiphyResponseList giphyResponseList = gifFeignClientAPI.getGif(
						apiConfigurationProperties.getApiKey(),
						currencyDynamic.getCurrencyRatesRelationDynamicPerformance(),
						apiConfigurationProperties.getLimit())
				.orElseThrow(() -> {
					log.error("Error in class {}, method {}, during feign client Giphy API response processing.",
							this.getClass().getSimpleName(), this.getClass().getEnclosingMethod().getName());
					throw new GifFeignClientResponseException("Cannot get a response from giphy API.");
				});

		return getRandomGifURL(giphyResponseList);
	}

	private String getRandomGifURL(GiphyResponseList giphyResponseList) {
		log.info("Grabbing random object from Giphy collection.");
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
		return ThreadLocalRandom.current().nextInt(1, gifsQty + 1);
	}
}
