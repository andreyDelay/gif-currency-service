package com.andrey.gifcurrencyservice.model.giphy;

import lombok.Data;

@Data
public class GiphyImage {
	private String height;
	private String width;
	private String size;
	private String url;
	private String mp4Size;
	private String mp4;
	private String webpSize;
	private String webp;
	private String frames;
	private String hash;
}
