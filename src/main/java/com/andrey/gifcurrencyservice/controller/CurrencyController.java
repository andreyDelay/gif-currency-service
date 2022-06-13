package com.andrey.gifcurrencyservice.controller;

import com.andrey.gifcurrencyservice.dto.GifImageDto;
import com.andrey.gifcurrencyservice.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/rates/dynamic/currency")
public class CurrencyController {

	private final CurrencyService currencyService;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public GifImageDto getGifImage(@Valid @Pattern(regexp = "[A-Z]{3}")
									@RequestParam("charCode") String currencyCode) {
		return currencyService.getGifImageOnCurrencyRatesDynamicCondition(currencyCode);
	}

}
