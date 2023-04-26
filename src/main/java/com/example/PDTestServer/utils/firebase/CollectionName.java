package com.example.PDTestServer.utils.firebase;

public enum CollectionName {
    USERS("users"),
    TESTS("tests"),
    TESTS_HISTORY("testsHistory"),
    TEST_DATES("testDates");

    public String name;

    CollectionName(String name) {
        this.name = name;
    }
}
