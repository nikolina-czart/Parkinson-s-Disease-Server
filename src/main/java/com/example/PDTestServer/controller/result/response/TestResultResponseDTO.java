package com.example.PDTestServer.controller.result.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Setter
@Getter
@Builder
public class TestResultResponseDTO {
    private String date;
    private String medicineSupply;
    private String side;
    private ArrayList<String> timestamp;
//    private ArrayList<String> x;
//    private ArrayList<String> y;
//    private ArrayList<String> z;
//    private ArrayList<String> timestampUpDown;
//    private ArrayList<String> upDown;
}
