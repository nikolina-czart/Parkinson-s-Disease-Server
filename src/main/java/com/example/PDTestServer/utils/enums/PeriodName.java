package com.example.PDTestServer.utils.enums;

public enum PeriodName {
    MONTH("Month"),
    QUARTER("Three months"),
    HALF("Six months"),
    ALL("All measurements");

    public String name;

    PeriodName(String name) {
        this.name = name;
    }
}
