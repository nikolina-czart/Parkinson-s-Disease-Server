package com.example.PDTestServer.model.analysis.gyroscope;

import com.example.PDTestServer.model.analysis.fingerTapping.TappingAnalysisDataParameter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class TremorAnalysisData {
    private String period;
    private TremorAnalysisDataParameter data;
}
