package com.andrey.gifcurrencyservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyRate {
	private String currencyCode;
	private double currencyRate;
}
