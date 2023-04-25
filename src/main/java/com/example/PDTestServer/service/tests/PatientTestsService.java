package com.example.PDTestServer.service.tests;

import com.example.PDTestServer.controller.tests.patient.request.PatientTestDTO;
import com.example.PDTestServer.model.tests.PatientTestDAO;
import com.example.PDTestServer.repository.tests.PatientTestsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.example.PDTestServer.utils.coverter.TestConverter.covertPatientTestDTOToDAO;

@Service
public class PatientTestsService {

    @Autowired
    PatientTestsRepository patientTestsRepository;

    //TODO - good
    public String saveTest(String uid, Set<PatientTestDTO> patientTests) {
        patientTestsRepository.saveTestToPatient(uid, convertSetPatientTestsDTOToDAO(patientTests));
        return "Tests has been saved successfully to user with ID " + uid;
    }

    //TODO - good
    public String deleteTest(String uid, String testID) {
        patientTestsRepository.deleteTest(uid, testID);
        return "Test " + testID + " for User with User ID " + uid + "has been deleted successfully";
    }

    //TODO - good
    private Map<String, PatientTestDAO> convertSetPatientTestsDTOToDAO(Set<PatientTestDTO> patientTests) {
        Map<String, PatientTestDAO> tests = new HashMap<>();

        patientTests.forEach(test -> tests.put(test.getUid(), covertPatientTestDTOToDAO(test)));

        return tests;
    }
}
