package com.example.PDTestServer.utils.enums;

public enum TestName {
    FINGER_TAPPING("FINGER_TAPPING"),
    TREMORS(" GYROSCOPE_TEST");

    public String name;

    TestName(String name) {
        this.name = name;
    }

    public static TestName valueOfLabel(String label) {
        for (TestName e : values()) {
            if (e.name.equals(label)) {
                return e;
            }
        }
        return null;
    }
}
