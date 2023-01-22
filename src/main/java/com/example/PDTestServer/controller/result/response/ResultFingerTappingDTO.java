package com.example.PDTestServer.controller.result.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@Builder
public class ResultFingerTappingDTO {
    private String testDate;
    private ResultFingerTappingByDayDTO fingerTappingByDayDTO;
}
