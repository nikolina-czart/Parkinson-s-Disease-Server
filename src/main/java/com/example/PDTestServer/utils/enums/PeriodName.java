package com.example.PDTestServer.utils.enums;

public enum PeriodName {
    MONTH("Miesiąc"),
    QUARTER("Trzy miesiące"),
    HALF("Pół roku"),
    ALL("Wszystkie pomiary");

    public String name;

    PeriodName(String name) {
        this.name = name;
    }
}
