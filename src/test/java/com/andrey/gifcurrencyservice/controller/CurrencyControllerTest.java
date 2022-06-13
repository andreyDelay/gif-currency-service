package com.andrey.gifcurrencyservice.controller;

import com.andrey.gifcurrencyservice.config.GifApiConfigurationProperties;
import com.andrey.gifcurrencyservice.config.WireMockInitializer;
import com.andrey.gifcurrencyservice.model.CurrencyRatesDynamic;
import com.andrey.gifcurrencyservice.service.GifService;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ContextConfiguration(initializers = {WireMockInitializer.class})
class CurrencyControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WireMockServer wireMockServer;

	@Autowired
	private GifApiConfigurationProperties gifApiProperties;

	@SpyBean
	private GifService gifService;

	@Value("${json.stubs.gif.broke}")
	private String brokeGifsResponseJSON;

	@Value("${json.stubs.gif.rich}")
	private String richGifsResponseJSON;

	@Value("${json.stubs.currency.latest}")
	private String latestRatesResponseJSON;

	@Value("${json.stubs.currency.specified-date}")
	private String specifiedDateRatesResponseJSON;

	private final String CURRENCY_LATEST_URL = "/currency/api/latest.*";

	private final String CURRENCY_FOR_DATE_URL = "/currency/api/historical/.*";

	private final String GIF_URL = "/v1/gifs/search.*";

	@Test
	@Order(1)
	void setUpWireMockAndAssertItsWorking() throws IOException {
		wireMockServer.stubFor(
				WireMock.get(urlMatching(CURRENCY_LATEST_URL))
						.willReturn(aResponse()
								.withHeader("Content-type", "application/json")
								.withBody(Files.readAllBytes(Paths.get(latestRatesResponseJSON)))));
		wireMockServer.stubFor(
				WireMock.get(urlMatching(CURRENCY_FOR_DATE_URL))
						.willReturn(aResponse()
								.withHeader("Content-type", "application/json")
								.withBody(Files.readAllBytes(Paths.get(specifiedDateRatesResponseJSON)))));
		wireMockServer.stubFor(
				WireMock.get(urlMatching(GIF_URL))
						.withQueryParam("q", equalTo("rich"))
						.willReturn(aResponse()
								.withHeader("Content-type", "application/json")
								.withBody(Files.readAllBytes(Paths.get(richGifsResponseJSON)))));
		wireMockServer.stubFor(
				WireMock.get(urlMatching(GIF_URL))
						.withQueryParam("q", equalTo("broke"))
						.willReturn(aResponse()
								.withHeader("Content-type", "application/json")
								.withBody(Files.readAllBytes(Paths.get(brokeGifsResponseJSON)))));
		assertTrue(wireMockServer.isRunning());
	}

	@Test
	@Order(2)
	void shouldReturnPositiveGifImage() throws Exception {
		mockMvc.perform(get("/api/v1/rates/dynamic/currency")
						.param("charCode", "AED"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.gifFormatURL")
						.value(anyOf(containsString("LdOyjZ7io5Msw"),
									containsString("lptjRBxFKCJmFoibP3"))));

		Mockito.verify(gifService).getGifByCurrencyDynamic(CurrencyRatesDynamic.POSITIVE);
	}

	@Test
	@Order(3)
	void shouldReturnNegativeGifImage() throws Exception {
		mockMvc.perform(get("/api/v1/rates/dynamic/currency")
						.param("charCode", "AFN"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.gifFormatURL")
						.value(anyOf(containsString("ZGH8VtTZMmnwzsYYMf"),
								containsString("WmWKxyQaUGknJf5B3r"))));
	}

	@Test
	@Order(4)
	void shouldReturnErrorDtoWhenFeignServiceUnavailable() throws Exception {
		wireMockServer.resetAll();
		String message = "Currently the service cannot be reached!";

		wireMockServer.stubFor(
				WireMock.get(anyUrl())
						.willReturn(aResponse()
								.withHeader("Content-type", "application/json")
								.withStatus(HttpStatus.SERVICE_UNAVAILABLE.value())
								.withStatusMessage(message)));

		mockMvc.perform(get("/api/v1/rates/dynamic/currency")
						.param("charCode", "ANY"))
				.andDo(print())
				.andExpect(status().isServiceUnavailable())
				.andExpect(jsonPath("$.message")
						.value(containsString(message)))
				.andExpect(jsonPath("$.code")
						.value(Matchers.equalTo("INTERNAL_SERVER_ERROR")));
	}

}