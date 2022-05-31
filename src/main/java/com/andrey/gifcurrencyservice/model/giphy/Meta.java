package com.andrey.gifcurrencyservice.model.giphy;

import lombok.Data;
import lombok.Value;

@Data
public class Meta {
	private int status;
	private String msg;
	private String responseId;
}
