package com.andrey.gifcurrencyservice.model.giphy;

import lombok.Data;
import lombok.Value;

@Data
public class Pagination {
	private int totalCount;
	private int count;
	private int offset;
}
