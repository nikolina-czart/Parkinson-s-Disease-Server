package com.example.PDTestServer.controller.analysis.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class AnalysisRequestDTO {
    private String testNameID;
    private String period;
}
