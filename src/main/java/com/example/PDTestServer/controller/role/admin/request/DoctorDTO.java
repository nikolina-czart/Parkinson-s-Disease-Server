package com.example.PDTestServer.controller.role.admin.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class DoctorDTO {
    private String uid;
    private String name;
    private String surname;
    private String email;
    private long patientsNumber;
    private long controlsNumber;
}
