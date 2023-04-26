package com.example.PDTestServer.utils.coverter;

import com.example.PDTestServer.controller.role.doctor.request.TestDetailDTO;
import com.example.PDTestServer.controller.tests.config.request.ConfigTestDTO;
import com.example.PDTestServer.controller.tests.patient.request.PatientTestDTO;
import com.example.PDTestServer.model.tests.ConfigTestDAO;
import com.example.PDTestServer.model.tests.PatientTestDAO;
import com.example.PDTestServer.model.tests.TestDetailsDAO;

public class TestConverter {

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

    //TODO - good
    public static ConfigTestDTO convertConfigTestDAOToDTO(ConfigTestDAO configTest) {
        return ConfigTestDTO.builder()
                .uid(configTest.getUid())
                .icon(configTest.getIcon())
                .namePL(configTest.getNamePL())
                .name(configTest.getName())
                .build();
    }
}
