package com.andrey.gifcurrencyservice.service.impl;

import com.andrey.gifcurrencyservice.config.GifApiConfigurationProperties;
import com.andrey.gifcurrencyservice.feign.GifFeignClientAPI;
import com.andrey.gifcurrencyservice.service.GifService;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

@Service
@AllArgsConstructor
public class GifServiceImpl implements GifService {

	private final GifFeignClientAPI gifFeignClientAPI;
	private final GifApiConfigurationProperties apiConfigurationProperties;

	@Override
	public String getPositiveGifUrl() {
		String response = gifFeignClientAPI.getGif(
				apiConfigurationProperties.getApiKey(),
				apiConfigurationProperties.getPositiveSearchQuery(),
				apiConfigurationProperties.getLimit())
				.orElseThrow(() -> new RuntimeException(""));

		return retrieveTargetUrl(response);
	}

	@Override
	public String getNegativeGifUrl() {
		String response = gifFeignClientAPI.getGif(
				apiConfigurationProperties.getApiKey(),
				apiConfigurationProperties.getNegativeSearchQuery(),
				apiConfigurationProperties.getLimit())
				.orElseThrow(() -> new RuntimeException(""));

		return retrieveTargetUrl(response);
	}

	private String retrieveTargetUrl(String jsonResponse) {
		try (JsonParser parser = new JsonFactory().createParser(jsonResponse)) {
			int randomImageIndex = getRandomImageIndex(jsonResponse);
			parser.setCodec(new ObjectMapper());

			return parser.readValueAsTree()
					.get("data")
					.get(randomImageIndex)
					.get("images")
					.get("original")
					.get("webp")
					.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private int getRandomImageIndex(String response) {
		int imagesQty = JsonPath.read(response, "$.data.size()");
		return ThreadLocalRandom.current().nextInt(1, imagesQty);
	}
}
