package com.example.PDTestServer.utils.enums;

public enum Side {
    LEFT("LEFT"),
    RIGHT("RIGHT");

    public String name;

    Side(String name) {
        this.name = name;
    }

    public static Side valueOfLabel(String label) {
        for (Side e : values()) {
            if (e.name.equals(label)) {
                return e;
            }
        }
        return null;
    }
}
