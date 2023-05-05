package com.example.PDTestServer.controller.role.doctor.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class PatientsDetails {
    private String parameter;
    private String patientPD;
    private String patientControl;
}
