package com.example.PDTestServer.controller.result.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ResultRequestDTO {
    private String testNameID;
    private String formDate;
    private String toDate;
}
