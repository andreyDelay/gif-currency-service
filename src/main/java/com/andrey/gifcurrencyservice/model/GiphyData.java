package com.andrey.gifcurrencyservice.model;

import lombok.Data;

import java.util.Map;

@Data
public class GiphyData {
	private String type;
	private String id;
	private String url;
	private String embed_url;
	private String username;
	private String source;
	private String title;
	private String rating;
	Map<String, GiphyImage> images;
}
