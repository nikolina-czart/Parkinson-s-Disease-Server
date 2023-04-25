package com.example.PDTestServer.controller.role.doctor.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@Builder
//TODO - good
public class PatientDTO {
    private String uid;
    private String name;
    private String surname;
    private String email;
    private Set<TestDetailDTO> patientTests;
}
