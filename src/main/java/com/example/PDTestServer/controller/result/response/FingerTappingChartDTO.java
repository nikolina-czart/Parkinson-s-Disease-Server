package com.example.PDTestServer.controller.result.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Builder
public class FingerTappingChartDTO {
    private ArrayList<String> timestamp;
    private ArrayList<String> upDown;
}
