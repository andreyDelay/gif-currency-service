package com.andrey.gifcurrencyservice.model;

public enum CurrencyDynamic {
    POSITIVE("rich"),
    NEGATIVE("broke");

    private String currencyRatesRelationDynamicPerformance;

    CurrencyDynamic(String currencyRatesRelationDynamicPerformance) {
        this.currencyRatesRelationDynamicPerformance = currencyRatesRelationDynamicPerformance;
    }

    public String getCurrencyRatesRelationDynamicPerformance() {
        return currencyRatesRelationDynamicPerformance;
    }
}
