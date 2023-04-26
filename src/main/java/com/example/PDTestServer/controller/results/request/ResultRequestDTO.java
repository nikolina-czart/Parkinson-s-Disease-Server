package com.example.PDTestServer.controller.results.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
//TODO - good
public class ResultRequestDTO {
    private String testNameID;
    private String formDate;
    private String toDate;
}
