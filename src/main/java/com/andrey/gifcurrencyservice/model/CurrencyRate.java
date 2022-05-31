package com.andrey.gifcurrencyservice.model;

import lombok.Value;

@Value
public class CurrencyRate {
	private String currencyCode;
	private double currencyRate;
}
