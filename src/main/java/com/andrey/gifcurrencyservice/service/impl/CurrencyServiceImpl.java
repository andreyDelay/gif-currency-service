package com.andrey.gifcurrencyservice.service.impl;

import com.andrey.gifcurrencyservice.dto.GifDto;
import com.andrey.gifcurrencyservice.model.CurrencyRate;
import com.andrey.gifcurrencyservice.service.CurrencyService;
import com.andrey.gifcurrencyservice.service.GifService;
import com.andrey.gifcurrencyservice.service.RateService;
import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

	private final RateService rateService;
	private final GifService gifService;

	@Override
	public GifDto getGifBytesOnCurrencyRateCondition(String currencyCode) {
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

		GifDto response;

		try {
			response = getHTMLResponse(gifUrl);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return response;
	}

	private GifDto getHTMLResponse(String gifUrl) throws IOException {
		HttpURLConnection connection = createConnection(gifUrl);
		Map<String, String> headers = connection.getHeaderFields().entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().get(0)));

		String body;

		try(InputStream in = connection.getInputStream()) {
			body = IOUtils.toString(in, "UTF-8");
			if (Objects.isNull(body)) {
				throw new IOException("Response body is empty, no content available.");
			}
		}

		return new GifDto(headers, body);
	}

	private HttpURLConnection createConnection(String gifUrl) throws IOException {
		URL url = new URL(gifUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		return connection;
	}
}
