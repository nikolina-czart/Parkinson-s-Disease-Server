package com.example.PDTestServer.service;

import com.example.PDTestServer.controller.patientTests.request.PatientTestDTO1;
import com.example.PDTestServer.model.tests.TestDetailsDAO;
import com.example.PDTestServer.repository.tests.PatientTestsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class TestService {
    @Autowired
    PatientTestsRepository patientTestsRepository;

//    public String saveTest(String uid, Set<PatientTestDTO> patientTestDTOSet) {
//        testPatientRepository.saveTestToPatient(uid, getMapStringToPatientTestDAO(patientTestDTOSet));
//
//        return "Tests has been saved successfully to user with ID " + uid;
//    }

//    public Set<PatientTestDTO> getTestDetails(String uid) throws ExecutionException, InterruptedException {
//        Map<String, TestDAO> patientTestDAOMap = testPatientRepository.getTestDetailsForUser(uid);
//
//        return getPatientTestDTOSet(patientTestDAOMap);
//    }
//
//    public String deleteTest(String uid, String testID) {
//        testPatientRepository.deleteTest(uid, testID);
//        return "Test " + testID + " for User with User ID " + uid + "has been deleted successfully";
//    }

//    private Map<String, TestDetailsDAO> getMapStringToPatientTestDAO(Set<PatientTestDTO> testsAvailable) {
//        Map<String, TestDetailsDAO> patientTestDAOMap = new HashMap<>();
//
//        testsAvailable.forEach(test -> patientTestDAOMap.put(test.getUid(), new PatientTestDAO(test.getUid())));
//
//        return patientTestDAOMap;
//    }

    private Set<PatientTestDTO1> getPatientTestDTOSet(Map<String, TestDetailsDAO> patientTestDAOMap) {
        Set<PatientTestDTO1> patientTestDTO1Set = new HashSet<>();

        patientTestDAOMap.forEach((key, value) ->
                patientTestDTO1Set.add(PatientTestDTO1.builder()
                        .uid(key)
//                        .name(value.getName())
                        .startDate(value.getStartDate())
                        .lastDate(value.getLastDate())
                        .numberTest(value.getNumberTest())
                        .build())
        );

        return patientTestDTO1Set;
    }
}
