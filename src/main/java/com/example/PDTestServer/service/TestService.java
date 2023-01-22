package com.example.PDTestServer.service;

import com.example.PDTestServer.controller.patientTests.request.PatientTestDTO;
import com.example.PDTestServer.model.PatientTestDAO;
import com.example.PDTestServer.repository.TestPatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Service
public class TestService {
    @Autowired
    TestPatientRepository testPatientRepository;

    public String saveTest(String uid, Set<PatientTestDTO> patientTestDTOSet) {
        testPatientRepository.saveTestByPatient(uid, getMapStringToPatientTestDAO(patientTestDTOSet));

        return "Tests has been saved successfully to user with ID " + uid;
    }

    public Set<PatientTestDTO> getTestDetails(String uid) throws ExecutionException, InterruptedException {
        Map<String, PatientTestDAO> patientTestDAOMap = testPatientRepository.getTestDetails(uid);

        return getPatientTestDTOSet(patientTestDAOMap);
    }

    public String deleteTest(String uid, String testID) {
        testPatientRepository.deleteTest(uid, testID);
        return "Test " + testID + " for User with User ID " + uid + "has been deleted successfully";
    }

    private Map<String, PatientTestDAO> getMapStringToPatientTestDAO(Set<PatientTestDTO> testsAvailable) {
        Map<String, PatientTestDAO> patientTestDAOMap = new HashMap<>();

        testsAvailable.forEach(test -> patientTestDAOMap.put(test.getUid(), new PatientTestDAO(test.getName())));

        return patientTestDAOMap;
    }

    private Set<PatientTestDTO> getPatientTestDTOSet(Map<String, PatientTestDAO> patientTestDAOMap) {
        Set<PatientTestDTO> patientTestDTOSet = new HashSet<>();

        patientTestDAOMap.forEach((key, value) ->
                patientTestDTOSet.add(PatientTestDTO.builder()
                        .uid(key)
                        .name(value.getName())
                        .build())
        );

        return patientTestDTOSet;
    }
}
