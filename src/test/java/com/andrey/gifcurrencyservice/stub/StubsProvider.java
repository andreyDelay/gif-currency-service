package com.andrey.gifcurrencyservice.stub;

import com.andrey.gifcurrencyservice.model.ApiRates;
import com.andrey.gifcurrencyservice.model.GiphyResponseList;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Component
public class StubsProvider {

	@Value("${json.stubs.gif.broke}")
	private String pathToBrokeGifsJSONFile;

	@Value("${json.stubs.gif.rich}")
	private String pathToRichGifsJSONFile;

	@Value("${json.stubs.currency.latest}")
	private String pathToLatestRatesJSONFile;

	@Value("${json.stubs.currency.specified-date}")
	private String pathToSpecifiedDatesRatesJSONFile;

	public Optional<GiphyResponseList> requestPositiveGIFs() throws IOException {
		return Optional.of(getTargetGiphyCollection(pathToRichGifsJSONFile, GiphyResponseList.class));
	}

	public Optional<GiphyResponseList> requestNegativeGIFs() throws IOException {
		return Optional.of(getTargetGiphyCollection(pathToBrokeGifsJSONFile, GiphyResponseList.class));
	}

	public Optional<ApiRates> getLatestRates() throws IOException {
		return Optional.of(getTargetGiphyCollection(pathToLatestRatesJSONFile, ApiRates.class));
	}

	public Optional<ApiRates> getSpecifiedDateRates() throws IOException {
		return Optional.of(getTargetGiphyCollection(pathToSpecifiedDatesRatesJSONFile, ApiRates.class));
	}

	private <T> T getTargetGiphyCollection(String pathToJSON, Class<T> type) throws IOException {
		byte[] bytes = Files.readAllBytes(Path.of(pathToJSON));
		ObjectMapper objectMapper = new ObjectMapper()
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		JsonNode jsonNode = objectMapper
				.getFactory()
				.createParser(bytes)
				.readValueAsTree();

		return objectMapper.treeToValue(jsonNode, type);
	}

}
