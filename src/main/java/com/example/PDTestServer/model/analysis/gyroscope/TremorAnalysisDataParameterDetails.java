package com.example.PDTestServer.model.analysis.gyroscope;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class TremorAnalysisDataParameterDetails {
    private List<Double> dataBeforeMedLeft;
    private List<Double> dataBeforeMedRight;
    private List<Double> dataAfterMedLeft;
    private List<Double> dataAfterMedRight;
}
