package com.andrey.gifcurrencyservice.model;

import lombok.Value;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Value
public class ApiRates {
	private String disclaimer;
	private String license;
	private Date timestamp;
	private String base;
	private Map<String, Double> rates;
}
