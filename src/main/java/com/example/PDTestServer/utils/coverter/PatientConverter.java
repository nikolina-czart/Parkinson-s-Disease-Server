package com.example.PDTestServer.utils.coverter;

import com.example.PDTestServer.controller.role.doctor.request.PatientDTO;
import com.example.PDTestServer.controller.role.doctor.request.TestDetailDTO;
import com.example.PDTestServer.model.role.UserDAO;

import java.util.Set;

public class PatientConverter {

    public static PatientDTO coverterUserDAOToPatientDTO(UserDAO user, Set<TestDetailDTO> patientTests) {
        return PatientDTO.builder()
                .uid(user.getUid())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .patientTests(patientTests)
                .build();
    }
}
