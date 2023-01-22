package com.example.PDTestServer.controller.doctor.request;

import com.example.PDTestServer.controller.patientTests.request.PatientTestDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@Builder
public class UserWithTestDTO {
    private String uid;
    private String name;
    private String surname;
    private Set<PatientTestDTO> patientTests;
}
