package com.andrey.gifcurrencyservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GiphyResponseList {
	@JsonProperty("data")
	private List<GiphyData> rootCollection;
}
