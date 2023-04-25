package com.example.PDTestServer.utils.enums;

public enum Role {
    PATIENT("PATIENT"),
    ADMIN("ADMIN"),
    DOCTOR("DOCTOR");

    public String name;

    Role(String name) {
        this.name = name;
    }
}
