package com.example.PDTestServer.controller.results.response;

import com.example.PDTestServer.model.results.Accel;
import com.example.PDTestServer.model.results.Tapping;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
@Builder
public class ResultsDTO {
    private String date;
    private String medicineSupply;
    private String side;
    private Accel accelData;
    private Tapping tappingData;
}
