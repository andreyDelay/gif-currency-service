package com.andrey.gifcurrencyservice.dto;

import lombok.Value;

import java.util.Map;

@Value
public class GifDto {
	private Map<String, String> headers;
	private String body;
}
