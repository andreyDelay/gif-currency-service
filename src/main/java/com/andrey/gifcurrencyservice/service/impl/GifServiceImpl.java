package com.andrey.gifcurrencyservice.service.impl;

import com.andrey.gifcurrencyservice.config.GifApiConfigurationProperties;
import com.andrey.gifcurrencyservice.exception.GifFeignClientResponseException;
import com.andrey.gifcurrencyservice.exception.GifFetchingException;
import com.andrey.gifcurrencyservice.feign.GifFeignClientAPI;
import com.andrey.gifcurrencyservice.model.CurrencyRatesDynamic;
import com.andrey.gifcurrencyservice.model.GiphyData;
import com.andrey.gifcurrencyservice.model.GiphyImage;
import com.andrey.gifcurrencyservice.model.GiphyResponseList;
import com.andrey.gifcurrencyservice.service.GifService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GifServiceImpl implements GifService {
	private final GifFeignClientAPI gifFeignClientAPI;
	private final GifApiConfigurationProperties gifApiConfigurationProperties;

	@Override
	public GiphyImage getGifByCurrencyDynamic(CurrencyRatesDynamic currencyDynamic) {
		log.info("Grabbing random object from Giphy collection for currency dynamic:{}.", currencyDynamic);
		GiphyResponseList giphyResponseList;
		switch (currencyDynamic) {
			case POSITIVE -> giphyResponseList = getPositiveGiphyCollection(currencyDynamic);
			case NEGATIVE -> giphyResponseList = getNegativeGiphyCollection(currencyDynamic);
			default -> throw new GifFetchingException(
					String.format("Unexpected behaviour, invalid currency dynamic value %s.",
							currencyDynamic.getCurrencyRatesRelationDynamicPerformance()));
		}

		int gifsQty = giphyResponseList.getGiphyRootCollection().size();
		int randomGifObjectIndex = getRandomImageIndex(gifsQty);

		GiphyImage giphyImage = giphyResponseList
				.getGiphyRootCollection()
				.get(randomGifObjectIndex)
				.getImagesMap()
				.values().stream()
				.findFirst()
				.orElseThrow(() -> new RuntimeException(""));

		return giphyImage;
	}

	private int getRandomImageIndex(int gifsQty) {
		return ThreadLocalRandom.current().nextInt(0, gifsQty);
	}

	private GiphyResponseList getPositiveGiphyCollection(CurrencyRatesDynamic currencyDynamic) {
		log.info("Getting positive GIFS.");
		GiphyResponseList giphyCollection = gifFeignClientAPI.requestPositiveGIFs(
						gifApiConfigurationProperties.getApiKey(),
						currencyDynamic.getCurrencyRatesRelationDynamicPerformance(),
						gifApiConfigurationProperties.getLimit())
				.orElseThrow(() -> {
					log.error("Error in class {}, during grabbing URL from feign client Giphy API response.",
							this.getClass().getSimpleName());
					throw new GifFeignClientResponseException("Cannot get a response from giphy API.");
				});
		return filterGiphyCollectionBySpecifiedImageObjectName(giphyCollection,
				gifApiConfigurationProperties.getSpecifiedImageObjectName());
	}

	private GiphyResponseList getNegativeGiphyCollection(CurrencyRatesDynamic currencyDynamic) {
		log.info("Getting negative GIFS.");
		GiphyResponseList giphyCollection = gifFeignClientAPI.requestNegativeGIFs(
						gifApiConfigurationProperties.getApiKey(),
						currencyDynamic.getCurrencyRatesRelationDynamicPerformance(),
						gifApiConfigurationProperties.getLimit())
				.orElseThrow(() -> {
					log.error("Error in class {}, during grabbing URL from feign client Giphy API response.",
							this.getClass().getSimpleName());
					throw new GifFeignClientResponseException("Cannot get a response from giphy API.");
				});
		return filterGiphyCollectionBySpecifiedImageObjectName(giphyCollection,
				gifApiConfigurationProperties.getSpecifiedImageObjectName());

	}

	private GiphyResponseList filterGiphyCollectionBySpecifiedImageObjectName(GiphyResponseList giphyResponseList,
																			  String targetImageName) {
		log.info("Filtering Giphy collection to grab specified image object, specified object:{}", targetImageName);
		List<GiphyData> filteredGiphyData = giphyResponseList
				.getGiphyRootCollection().stream()
				.peek(giphyDataObject -> {
					Map<String, GiphyImage> filteredImageMap = giphyDataObject.getImagesMap().entrySet().stream()
							.filter(entry -> entry.getKey().equals(targetImageName))
							.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
					giphyDataObject.setImagesMap(filteredImageMap);
				}).toList();
		return new GiphyResponseList(filteredGiphyData);
	}
}
