package com.andrey.gifcurrencyservice.exception;

import org.springframework.http.HttpStatus;

public class GifFetchingException extends ApiException {
    public GifFetchingException(String message) {
        super("CANNOT_GET_GIF_RECOURSE_ERROR", message, HttpStatus.BAD_GATEWAY);
    }
}
