package com.example.PDTestServer.controller.analysis.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Setter
@Getter
@Builder
public class AggregateDataResponseDTO {
    private String testLabel;
    private String hoursSinceLastMedAverage;
    private String side;
    private String medicineSupply;
    private int vectorLength;
    private ArrayList<ArrayList<Double>> result;
}
