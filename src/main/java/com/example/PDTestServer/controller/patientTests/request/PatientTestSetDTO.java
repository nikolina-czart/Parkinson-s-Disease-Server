package com.example.PDTestServer.controller.patientTests.request;


import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
public class PatientTestSetDTO {
    private Set<PatientTestDTO1> patientTests;
}
