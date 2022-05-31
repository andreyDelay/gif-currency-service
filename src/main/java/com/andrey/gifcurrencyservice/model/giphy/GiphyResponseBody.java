package com.andrey.gifcurrencyservice.model.giphy;

import lombok.Data;

import java.util.List;

@Data
public class GiphyResponseBody {
	private List<GiphyData> data;
}
