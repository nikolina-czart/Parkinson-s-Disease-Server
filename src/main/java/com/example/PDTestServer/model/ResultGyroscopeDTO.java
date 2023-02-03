package com.example.PDTestServer.model;

import com.example.PDTestServer.controller.result.response.ResultGyroscopeByDayDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ResultGyroscopeDTO {
    private String testDate;
    private ResultGyroscopeByDayDTO gyroscopeByDayDTO;
}
