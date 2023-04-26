package com.example.PDTestServer.utils.firebase;

public enum CollectionName {
    USERS("users"),
    TESTS("tests"),
    TESTS_HISTORY("testsHistory"),
    TEST_DATES("testDates"),
    LEFT("LEFT"),
    RIGHT("RIGHT");

    public String name;

    CollectionName(String name) {
        this.name = name;
    }
}
