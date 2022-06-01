package com.andrey.gifcurrencyservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class GiphyData {
	@JsonProperty("type")
	private String contentType;
	@JsonProperty("id")
	private String imageId;
	@JsonProperty("url")
	private String originalImageUrl;
	private Map<String, GiphyImage> images;
}
