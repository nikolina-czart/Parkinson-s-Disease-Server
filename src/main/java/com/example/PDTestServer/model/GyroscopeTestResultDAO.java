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
public class GyroscopeTestResultDAO {
    private String date;
    private ArrayList<String> accel;
    private String medicineSupply;
    private String side;
}
