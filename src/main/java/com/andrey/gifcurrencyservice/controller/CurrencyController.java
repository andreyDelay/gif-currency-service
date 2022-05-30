package com.andrey.gifcurrencyservice.controller;

import com.andrey.gifcurrencyservice.service.CurrencyService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

@RestController
@RequestMapping(value = "/api/v1/currency/")
@AllArgsConstructor
@Validated
public class CurrencyController {

	private final CurrencyService currencyService;

	@GetMapping(value = "rate/{currencyCode}", produces = MediaType.IMAGE_GIF_VALUE)
	public String getGif(@Valid @Pattern(regexp = "[A-Z]{3}")
	                                     @PathVariable String currencyCode) {
		return currencyService.getGifUrlOnCurrencyRateCondition(currencyCode);
	}

}
