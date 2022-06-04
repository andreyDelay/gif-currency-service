package com.andrey.gifcurrencyservice.controller;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(lazyInit = true)
@AutoConfigureMockMvc
class CurrencyControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@DynamicPropertySource
	static void overrideFeignClientBaseURL(DynamicPropertyRegistry dynamicPropertyRegistry) {
		dynamicPropertyRegistry.add("http://api.giphy.com/v1/gifs/", wireMockServer::baseUrl);
	}

	@Autowired
	private static WireMockServer wireMockServer;

	@BeforeAll
	static void startWireMock() {
		wireMockServer = new WireMockServer();
		wireMockServer.start();
	}

	@AfterAll
	static void stopWireMock() {
		wireMockServer.stop();
	}

	@Test
	void testWireMock() {
		System.out.println(wireMockServer.baseUrl());
		assertTrue(wireMockServer.isRunning());
	}

	@Test
	@Order(1)
	void shouldReturnImageGIF() throws Exception {
		StubMapping stubMapping = wireMockServer.stubFor(
				WireMock.get(urlMatching("/latest.*"))
						.willReturn(aResponse()
								.withHeader("Content-type", MediaType.APPLICATION_JSON_VALUE)
								.withBody("[]"))
		);

		System.out.println(stubMapping);

		mockMvc.perform(get("/api/v1/currency/rates/changes")
						.param("charCode", "GBP"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(Arrays::isArray);
	}

	@Test
	void getGif() {

	}
}