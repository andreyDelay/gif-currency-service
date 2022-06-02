package com.andrey.gifcurrencyservice.model;

public enum CurrencyRatesDynamic {
    POSITIVE("rich"),
    NEGATIVE("broke");

    private String currencyRatesRelationDynamicPerformance;

    CurrencyRatesDynamic(String currencyRatesRelationDynamicPerformance) {
        this.currencyRatesRelationDynamicPerformance = currencyRatesRelationDynamicPerformance;
    }

    public String getCurrencyRatesRelationDynamicPerformance() {
        return currencyRatesRelationDynamicPerformance;
    }
}
