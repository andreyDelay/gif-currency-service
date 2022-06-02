package com.andrey.gifcurrencyservice.service.impl;

import com.andrey.gifcurrencyservice.config.CurrencyRateApiConfigurationProperties;
import com.andrey.gifcurrencyservice.config.GifApiConfigurationProperties;
import com.andrey.gifcurrencyservice.exception.CurrencyFeignClientResponseException;
import com.andrey.gifcurrencyservice.exception.GifFeignClientResponseException;
import com.andrey.gifcurrencyservice.feign.CurrencyFeignClientAPI;
import com.andrey.gifcurrencyservice.feign.GifFeignClientAPI;
import com.andrey.gifcurrencyservice.model.*;
import com.andrey.gifcurrencyservice.service.FeignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeignServiceImpl implements FeignService {

	private final CurrencyFeignClientAPI currencyFeignClientAPI;
	private final CurrencyRateApiConfigurationProperties rateApiConfigurationProperties;
	private final GifFeignClientAPI gifFeignClientAPI;
	private final GifApiConfigurationProperties gifApiConfigurationProperties;

	@Override
	@Cacheable(value = "rates")
	public ApiRates getRates() {
		log.info("Getting latest currency rates collection from API.");
		return currencyFeignClientAPI.getRates(
						rateApiConfigurationProperties.getLatest(),
						rateApiConfigurationProperties.getAppId(),
						rateApiConfigurationProperties.getObjectiveCurrencyCode())
				.orElseThrow(() -> {
					log.error("Error in class {}, during getting rates collection " +
									"from feign client currency API response.",
								this.getClass().getSimpleName());
					throw new CurrencyFeignClientResponseException("Couldn't get a valid response from API.");
				});
	}

	@Override
	@Cacheable(value = "ratesForDate")
	public ApiRates getRates(LocalDate specifiedDate) {
		log.info("Getting currency rates collection from API, for specified date: {}.", specifiedDate);
		return currencyFeignClientAPI.getRatesForSpecifiedData(
						rateApiConfigurationProperties.getHistorical(),
						getApiSpecifiedDateFormat(specifiedDate),
						rateApiConfigurationProperties.getAppId(),
						rateApiConfigurationProperties.getObjectiveCurrencyCode())
				.orElseThrow(() -> {
					log.error("Error in class {}, during getting rates collection for specified date:{}," +
									"from feign client currency API response.",
								this.getClass().getSimpleName(), specifiedDate);
					throw new CurrencyFeignClientResponseException(
							String.format("Couldn't get a response from API, for specified date %s .", specifiedDate));
				});
	}

	private String getApiSpecifiedDateFormat(LocalDate specifiedDate) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return specifiedDate.format(dateTimeFormatter) + ".json";
	}

	@Override
	@Cacheable(value = "positiveGifs")
	public GiphyResponseList getPositiveGiphyCollection(CurrencyRatesDynamic currencyDynamic) {
		log.info("Getting positive GIFS.");
		GiphyResponseList giphyCollection = requestGiphyCollectionWithFeignClient(currencyDynamic);
		return filterGiphyCollectionBySpecifiedImageObjectName(giphyCollection,
				gifApiConfigurationProperties.getSpecifiedImageObjectName());
	}

	@Override
	@Cacheable(value = "negativeGifs")
	public GiphyResponseList getNegativeGiphyCollection(CurrencyRatesDynamic currencyDynamic) {
		log.info("Getting negative GIFS.");
		GiphyResponseList giphyCollection = requestGiphyCollectionWithFeignClient(currencyDynamic);
		return filterGiphyCollectionBySpecifiedImageObjectName(giphyCollection,
				gifApiConfigurationProperties.getSpecifiedImageObjectName());
	}

	private GiphyResponseList requestGiphyCollectionWithFeignClient(CurrencyRatesDynamic currencyDynamic) {
		log.info("Requesting Giphy collection from API through feign, for search tag 'g='{}.", currencyDynamic);

		return gifFeignClientAPI.getGif(
						gifApiConfigurationProperties.getApiKey(),
						currencyDynamic.getCurrencyRatesRelationDynamicPerformance(),
						gifApiConfigurationProperties.getLimit())
				.orElseThrow(() -> {
					log.error("Error in class {}, during grabbing URL from feign client Giphy API response.",
							this.getClass().getSimpleName());
					throw new GifFeignClientResponseException("Cannot get a response from giphy API.");
				});
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
