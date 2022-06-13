package com.andrey.gifcurrencyservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiRates {
	private Date timestamp;
	private String base;
	private Map<String, Double> rates;
}
