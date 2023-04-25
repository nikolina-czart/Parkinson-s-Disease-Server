package com.example.PDTestServer.utils.firebase;

public enum FieldName {
    ROLE("role"),
    DOCTOR_ID("doctorID"),
    CREATE_AT("createAt");

    public String name;

    FieldName(String name) {
        this.name = name;
    }
}
