package com.example.PDTestServer.controller.tests.patient.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
//TODO - good
public class PatientTestDTO {
    private String uid;
    private String name;
}
