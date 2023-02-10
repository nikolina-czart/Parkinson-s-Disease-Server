package com.example.PDTestServer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TestResultDAO {
    private String date;
    private ArrayList<String> accel;
    private int hoursSinceLastMed;
    private String side;
    private String medicineSupply;
}