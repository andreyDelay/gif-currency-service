package com.andrey.gifcurrencyservice.controller;

import com.andrey.gifcurrencyservice.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.io.IOException;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/currency/rates")
public class CurrencyController {

	private final CurrencyService currencyService;

	@GetMapping(value = "/changes", produces = MediaType.IMAGE_GIF_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public byte [] getGif(@Valid @Pattern(regexp = "[A-Z]{3}")
										@RequestParam("charCode") String currencyCode) throws IOException {
		return IOUtils.toByteArray(
				currencyService.getGifOnCurrencyRateCondition(currencyCode)
				.byteStream());
	}
}
