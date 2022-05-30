package com.andrey.gifcurrencyservice.controller;

import com.andrey.gifcurrencyservice.service.CurrencyService;
import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.support.ServletContextResource;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

@RestController
@RequestMapping(value = "/api/v1/currency/rates/")
@AllArgsConstructor
@Validated
public class CurrencyController {

	private final CurrencyService currencyService;

	@GetMapping(value = "changes")
	public ResponseEntity<byte[]> getGif(@Valid @Pattern(regexp = "[A-Z]{3}") @RequestParam("charCode") String currencyCode) throws IOException {
		String gifUrl = currencyService.getGifUrlOnCurrencyRateCondition(currencyCode);
		URL url = new URL(gifUrl);
		InputStream is = new URL(gifUrl).openStream();
		byte[] bytes = IOUtils.toByteArray(is);
		return new ResponseEntity<>(bytes, HttpStatus.OK);
	}

}
