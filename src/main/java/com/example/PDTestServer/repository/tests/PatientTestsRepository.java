package com.example.PDTestServer.repository.tests;

import com.example.PDTestServer.model.tests.PatientTestDAO;
import com.example.PDTestServer.model.tests.TestDetailsDAO;
import com.example.PDTestServer.utils.firebase.FieldName;
import com.google.cloud.firestore.*;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static com.example.PDTestServer.utils.firebase.FirebaseReference.*;

@Repository
public class PatientTestsRepository {

    public void saveTestToPatient(String uid, Map<String, PatientTestDAO> patientTests) {
        patientTests.forEach((testUid, test) -> {
            patientTestDocRef(uid, testUid).set(test);
            patientTestHistoryDocRef(uid, testUid).set(test);
        });
    }

    public Map<String, TestDetailsDAO> getTestByUser(String uid) throws ExecutionException, InterruptedException {
        Map<String, TestDetailsDAO> testNameToTestDetails = new HashMap<>();
        List<QueryDocumentSnapshot> userTests = userTestColRef(uid).get().get().getDocuments();

        userTests.forEach(document -> testNameToTestDetails.put(document.getId(), document.toObject(TestDetailsDAO.class)));

        testNameToTestDetails.forEach(((testId, test) -> {
            try {
                createTestDetails(uid, testId, test);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }));

        return testNameToTestDetails;
    }

    private void createTestDetails(String userUid, String testUid, TestDetailsDAO test) throws ExecutionException, InterruptedException {
        CollectionReference testDatesCollectionReference = userTestDatesColRef(userUid, testUid);

        long countTests = testDatesCollectionReference.count().get().get().getCount();

        if(countTests != 0) {
            test.setNumberTest(String.valueOf(countTests));
            test.setStartDate(testDatesCollectionReference.orderBy(FieldName.CREATE_AT.name).limit(1).get().get().getDocuments().get(0).getId());
            test.setLastDate(testDatesCollectionReference.orderBy(FieldName.CREATE_AT.name).limitToLast(1).get().get().getDocuments().get(0).getId());
        }
    }
}
