package com.example.PDTestServer.model.analysis.fingerTapping;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class TappingAnalysisDataParameter {
    private TappingAnalysisDataParameterDetails touchTime;
    private TappingAnalysisDataParameterDetails upTime;
    private TappingAnalysisDataParameterDetails intertapInterval;
}
