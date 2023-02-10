package com.example.PDTestServer.controller.analysis.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class AnalyzedRequestDTO {
    private String testNameID;
    private String timeRange;
}
