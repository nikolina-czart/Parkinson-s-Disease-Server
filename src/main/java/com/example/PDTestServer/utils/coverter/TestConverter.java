package com.example.PDTestServer.utils.coverter;

import com.example.PDTestServer.controller.role.doctor.request.TestDetailDTO;
import com.example.PDTestServer.controller.tests.patient.request.PatientTestDTO;
import com.example.PDTestServer.model.tests.PatientTestDAO;
import com.example.PDTestServer.model.tests.TestDetailsDAO;

public class TestConverter {

    public static TestDetailsDAO convertTestDTOToTestDAO(TestDetailDTO testDetailDTO) {
        return new TestDetailsDAO(testDetailDTO.getNumberTest(), testDetailDTO.getStartDate(), testDetailDTO.getLastDate());
    }

    public static TestDetailDTO convertTestDetailsDAOToTestDTO(TestDetailsDAO testDetailsDAO, String uid) {
        return TestDetailDTO.builder()
                .uid(uid)
                .numberTest(testDetailsDAO.getNumberTest())
                .startDate(testDetailsDAO.getStartDate())
                .lastDate(testDetailsDAO.getLastDate())
                .build();
    }

    public static PatientTestDAO covertPatientTestDTOToDAO(PatientTestDTO patientTestDTO) {
        return new PatientTestDAO(patientTestDTO.getName());
    }
}
