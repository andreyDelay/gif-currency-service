package com.andrey.gifcurrencyservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GiphyResponseList {
	@JsonProperty("data")
	private List<GiphyData> giphyRootCollection;
}
