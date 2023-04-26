package com.example.PDTestServer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FingerTappingTestResultDAO {
    private String date;
    private ArrayList<String> accel;
//    private ArrayList<String> data;
    private String medicineSupply;
    private String side;
}
