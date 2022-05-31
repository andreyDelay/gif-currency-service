package com.andrey.gifcurrencyservice.model.giphy;

import lombok.Data;

@Data
public class GiphyImages {
	private GiphyImage fixedHeight;
	private GiphyImage fixedHeightStill;
	private GiphyImage fixedHeightDownsampled;
	private GiphyImage fixedWidth;
	private GiphyImage fixedWidthStill;
	private GiphyImage fixedWidthDownsampled;
	private GiphyImage fixedHeightSmall;
	private GiphyImage fixedHeightSmallStill;
	private GiphyImage fixedWidthSmall;
	private GiphyImage fixedWidthSmallStill;
	private GiphyImage downsized;
	private GiphyImage downsizedStill;
	private GiphyImage downsizedMedium;
	private GiphyImage downsizedLarge;
	private GiphyImage original;
	private GiphyImage originalStill;
	private GiphyImage looping;
}
