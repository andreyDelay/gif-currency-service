package com.andrey.gifcurrencyservice.model;

import lombok.Data;

import java.util.List;

@Data
public class GiphyResponseList {
	private List<GiphyData> data;
}
