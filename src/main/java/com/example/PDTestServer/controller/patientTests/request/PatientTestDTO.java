package com.example.PDTestServer.controller.patientTests.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class PatientTestDTO {
    private String uid;
    private String name;
}
