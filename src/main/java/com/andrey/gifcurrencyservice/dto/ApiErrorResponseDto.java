package com.andrey.gifcurrencyservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiErrorResponseDto {
	private String code;
	private String message;

	public ApiErrorResponseDto(Enum<?> code, String message) {
		this.code = code.name();
		this.message = message;
	}
}
