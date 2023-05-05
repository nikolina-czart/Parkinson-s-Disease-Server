package com.example.PDTestServer.model.analysis.fingerTapping;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class TappingAnalysisData {
    private String period;
    private TappingAnalysisDataParameter data;
}
