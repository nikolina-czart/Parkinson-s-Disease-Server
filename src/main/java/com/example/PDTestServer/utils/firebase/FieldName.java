package com.example.PDTestServer.utils.firebase;

public enum FieldName {
    ROLE("role"),
    NAME("name"),
    SURNAME("surname"),
    EMAIL("email"),
    DOCTOR_ID("doctorID"),
    CREATE_AT("createAt"),
    ACCEL("accel"),
    TAPPING_DATA("data"),
    HOURS_SINCE_LAST_MED("hoursSinceLastMed");

    public String name;

    FieldName(String name) {
        this.name = name;
    }
}
