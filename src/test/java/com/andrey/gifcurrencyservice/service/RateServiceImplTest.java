package com.andrey.gifcurrencyservice.service;

import com.andrey.gifcurrencyservice.exception.CurrencyNotFoundException;
import com.andrey.gifcurrencyservice.feign.CurrencyFeignClientAPI;
import com.andrey.gifcurrencyservice.model.ApiRates;
import com.andrey.gifcurrencyservice.model.CurrencyRate;
import com.andrey.gifcurrencyservice.stub.StubsProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
@ActiveProfiles("test")
@ComponentScan(lazyInit = true)
class RateServiceImplTest {

	@Autowired
	private StubsProvider stubsProvider;

	@Autowired
	private RateService rateService;

	@MockBean
	private CurrencyFeignClientAPI currencyFeignClientAPI;

	@Test
	void shouldReturnLatestCurrencyRate() throws IOException {
		//given
		String currencyCode = "RUB";
		Optional<ApiRates> latestRates = stubsProvider.getLatestRates();
		//when
		doReturn(latestRates)
				.when(currencyFeignClientAPI).getRates(anyString(), anyString(), anyString());
		CurrencyRate currencyRate = rateService.getCurrencyRate(currencyCode);
		//then
		assertThat(currencyRate).isNotNull();
		assertThat(currencyRate.getCurrencyRate()).isGreaterThan(0);
		assertThat(currencyCode).isEqualTo(currencyCode);
	}

	@Test
	void shouldThrowExceptionWhenTargetCurrencyRateNotFound() throws IOException {
		//given
		String fakeCurrencyCode = "TEST";
		Optional<ApiRates> latestRates = stubsProvider.getLatestRates();
		//when
		doReturn(latestRates)
				.when(currencyFeignClientAPI).getRates(anyString(), anyString(), anyString());
		//then
		assertThrows(CurrencyNotFoundException.class, () -> rateService.getCurrencyRate(fakeCurrencyCode));
	}

	@Test
	void shouldReturnCurrencyRateFoSpecifiedDate() throws IOException {
		//given
		String currencyCode = "EUR";
		LocalDate anyDateStub = LocalDate.now();
		Optional<ApiRates> specifiedDateRates = stubsProvider.getSpecifiedDateRates();
		//when
		doReturn(specifiedDateRates)
				.when(currencyFeignClientAPI)
				.getRatesForSpecifiedData(anyString(), anyString(), anyString(), anyString());
		CurrencyRate currencyRate = rateService.getCurrencyRateForSpecifiedDate(currencyCode, anyDateStub);
		//then
		assertThat(currencyRate).isNotNull();
		assertThat(currencyRate.getCurrencyRate()).isGreaterThan(0);
		assertThat(currencyCode).isEqualTo(currencyCode);
	}

	@Test
	void getCurrencyRateForSpecifiedDate() {
	}
}