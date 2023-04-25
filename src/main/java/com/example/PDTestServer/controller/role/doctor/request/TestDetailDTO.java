package com.example.PDTestServer.controller.role.doctor.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class TestDetailDTO {
    private String uid;
    private String startDate;
    private String lastDate;
    private String numberTest;
}
