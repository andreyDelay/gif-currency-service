package com.andrey.gifcurrencyservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GiphyImage {
	@JsonProperty("url")
	private String gifFormatURL;
	@JsonProperty("mp4")
	private String mp4FormatURL;
	@JsonProperty("webp")
	private String webpFormatURL;
}
