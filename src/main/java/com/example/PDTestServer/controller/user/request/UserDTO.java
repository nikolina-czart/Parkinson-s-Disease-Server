package com.example.PDTestServer.controller.user.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@Builder
public class UserDTO {
    private String name;
    private String surname;
    private String role;
    private String doctorID;
    private Set<PatientTestDTO> testsAvailable;
}
