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
public class TremorAnalysisDataParameter {
    private TremorAnalysisDataParameterDetails meanByDayX;
    private TremorAnalysisDataParameterDetails meanByDayY;
    private TremorAnalysisDataParameterDetails meanByDayZ;
    private TremorAnalysisDataParameterDetails aggregatedMeanByDay;
    private TremorAnalysisDataParameterDetails differenceMeanByDayX;
    private TremorAnalysisDataParameterDetails differenceMeanByDayY;
    private TremorAnalysisDataParameterDetails differenceMeanByDayZ;
}
