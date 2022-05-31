package com.andrey.gifcurrencyservice.exception;

import org.springframework.http.HttpStatus;

public class GifFeignClientResponseException extends ApiException {
    public GifFeignClientResponseException(String message) {
        super("GIPHY_RESPONSE_ERROR", message, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
