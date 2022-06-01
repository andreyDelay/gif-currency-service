package com.andrey.gifcurrencyservice.model;

import lombok.Value;

import java.util.Date;
import java.util.Map;

@Value
public class ApiRates {
	private Date timestamp;
	private String base;
	private Map<String, Double> rates;
}
