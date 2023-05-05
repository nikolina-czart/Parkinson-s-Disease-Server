package com.example.PDTestServer.controller.role.doctor.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class SummaryParameterDetails {
    private List<Double> dataLeft;
    private List<Double> dataRight;
}
