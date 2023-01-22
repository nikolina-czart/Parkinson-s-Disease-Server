package com.example.PDTestServer.controller.result.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ResultFingerTappingByDayDTO {
    private String medicineSupply;
    private String side;
    private StandardChartDTO standardChart;
    private FingerTappingChartDTO upDownChartDTO;
}
