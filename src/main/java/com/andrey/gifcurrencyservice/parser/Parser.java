package com.andrey.gifcurrencyservice.parser;

public interface Parser<T> {
    T parse(String jsonResponse);
}
