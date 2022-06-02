package com.andrey.gifcurrencyservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class ApiException extends RuntimeException {
	private final String code;
	private final String message;
	private final HttpStatus httpStatus;
}
