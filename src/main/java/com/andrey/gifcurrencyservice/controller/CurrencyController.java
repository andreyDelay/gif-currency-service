package com.andrey.gifcurrencyservice.controller;

import com.andrey.gifcurrencyservice.service.CurrencyService;
import lombok.AllArgsConstructor;
import okhttp3.ResponseBody;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping(value = "/api/v1/currency/rates")
@AllArgsConstructor
@Validated
public class CurrencyController {

	private final CurrencyService currencyService;

	@GetMapping(value = "/changes")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> getGif(@Valid @Pattern(regexp = "[A-Z]{3}")
										@RequestParam("charCode") String currencyCode) throws IOException {
		ResponseBody gifBytesOnCurrencyRateCondition = currencyService.getGifOnCurrencyRateCondition(currencyCode);
		InputStream inputStream = gifBytesOnCurrencyRateCondition.byteStream();
		byte[] bytes = IOUtils.toByteArray(inputStream);
		return new ResponseEntity<>(bytes, HttpStatus.OK);
	}

}
