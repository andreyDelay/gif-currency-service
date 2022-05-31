package com.andrey.gifcurrencyservice.model;

public enum CurrencyDynamic {
    POSITIVE("rich"),
    NEGATIVE("broke");

    private String value;

    CurrencyDynamic(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
