package com.example.PDTestServer.model.analysis.fingerTapping;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class TappingAnalysisDataParameterDetails {
    private List<Double> dataBeforeMedLeft;
    private List<Double> dataBeforeMedRight;
    private List<Double> dataAfterMedLeft;
    private List<Double> dataAfterMedRight;
    private List<Double> dataBeforeMedLeftMeanByDays;
    private List<Double> dataBeforeMedRightMeanByDays;
    private List<Double> dataAfterMedLeftMeanByDays;
    private List<Double> dataAfterMedRightMeanByDays;
}
