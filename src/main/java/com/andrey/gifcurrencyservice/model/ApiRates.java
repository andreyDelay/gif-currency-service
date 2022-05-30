package com.andrey.gifcurrencyservice.model;

import lombok.Value;

import java.util.Date;
import java.util.List;

@Value
public class ApiRates {
	private String disclaimer;
	private String license;
	private Date timestamp;
	private String base;
	private List<CurrencyRate> rates;
}
