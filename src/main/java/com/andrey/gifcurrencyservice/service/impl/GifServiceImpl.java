package com.andrey.gifcurrencyservice.service.impl;

import com.andrey.gifcurrencyservice.config.GifApiConfigurationProperties;
import com.andrey.gifcurrencyservice.feign.GifFeignClientAPI;
import com.andrey.gifcurrencyservice.model.GiphyResponseList;
import com.andrey.gifcurrencyservice.service.GifService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
@AllArgsConstructor
public class GifServiceImpl implements GifService {
	private final GifFeignClientAPI gifFeignClientAPI;
	private final GifApiConfigurationProperties apiConfigurationProperties;

	@Override
	public String getPositiveGifUrl() {
		GiphyResponseList giphyResponseList = gifFeignClientAPI.getGif(
						apiConfigurationProperties.getApiKey(),
						apiConfigurationProperties.getPositiveSearchQuery(),
						apiConfigurationProperties.getLimit())
				.orElseThrow(() -> new RuntimeException(""));

		return getRandomGifURL(giphyResponseList);
	}

	@Override
	public String getNegativeGifUrl() {
		GiphyResponseList giphyResponseList = gifFeignClientAPI.getGif(
						apiConfigurationProperties.getApiKey(),
						apiConfigurationProperties.getNegativeSearchQuery(),
						apiConfigurationProperties.getLimit())
				.orElseThrow(() -> new RuntimeException(""));

		return getRandomGifURL(giphyResponseList);
	}

	private String getRandomGifURL(GiphyResponseList giphyResponseList) {
		int gifsQty = giphyResponseList.getData().size();
		int randomGifIndex = getRandomImageIndex(gifsQty);

		return giphyResponseList
				.getData()
				.get(randomGifIndex)
				.getImages()
				.get(apiConfigurationProperties.getImageObjectName())
				.getUrl();
	}

	private int getRandomImageIndex(int gifsQty) {
		return ThreadLocalRandom.current().nextInt(1, gifsQty);
	}
}
