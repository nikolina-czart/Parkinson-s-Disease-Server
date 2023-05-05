package com.example.PDTestServer.controller.role.doctor.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class SummaryParameter {
    private SummaryParameterDetails touchTime;
    private SummaryParameterDetails upTime;
    private SummaryParameterDetails intertapInterval;
    private SummaryParameterDetails meanX;
    private SummaryParameterDetails meanY;
    private SummaryParameterDetails meanZ;
    private SummaryParameterDetails aggregated;
    private SummaryParameterDetails differenceX;
    private SummaryParameterDetails differenceY;
    private SummaryParameterDetails differenceZ;
}
