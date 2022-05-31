package com.andrey.gifcurrencyservice.model;

import lombok.Data;

import java.util.Map;

@Data
public class GiphyData {
	private String type;
	private String id;
	private String url;
	Map<String, GiphyImage> images;
}
