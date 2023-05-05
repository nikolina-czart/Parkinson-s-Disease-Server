package com.example.PDTestServer.controller.role.admin;

import com.example.PDTestServer.model.results.SideResults;
import com.example.PDTestServer.service.role.DoctorService;
import com.example.PDTestServer.utils.enums.Side;
import com.example.PDTestServer.utils.enums.TestName;
import com.example.PDTestServer.utils.firebase.CollectionName;
import com.example.PDTestServer.utils.firebase.FieldName;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static com.example.PDTestServer.utils.coverter.DateConverter.convertStringToTimestampEndDay;
import static com.example.PDTestServer.utils.coverter.DateConverter.convertStringToTimestampStartDay;
import static com.example.PDTestServer.utils.firebase.FirebaseQuery.resultsInRangeTime;
import static com.example.PDTestServer.utils.firebase.FirebaseReference.testDatesColRef;
import static com.example.PDTestServer.utils.firebase.FirebaseReference.testSideDocRef;

@RestController
class HelloWorldController {
    private static final String COLLECTION_USER_NAME = "users";
    private static final String COLLECTION_TEST_NAME = "tests";
    private static final String COLLECTION_TEST_HISTORY_NAME = "testsHistory";
    private static final String COLLECTION_TEST_DATES_NAME = "testDates";
    private static final String ROLE = "role";
    private static final String ROLE_PATIENT = "PATIENT";
    private static final String DOCTOR_ID = "doctorID";
    private static final String UID = "xk2DWuC8nWWcQH4Vj95Ezd1oUA03";
    private static final String TREMOR_UID = " GYROSCOPE";

    @Autowired
    private DoctorService doctorService;
    @GetMapping("/hello")
    public String hello() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        List<SideResults> results = new ArrayList<>();
        List<QueryDocumentSnapshot> filteredResults = testDatesColRef(UID, TestName.TREMORS).get().get().getDocuments();

        filteredResults.forEach(result -> {
            results.add(getSideResults(UID, result, Side.LEFT, TestName.TREMORS));
            results.add(getSideResults(UID, result, Side.RIGHT, TestName.TREMORS));
        });



        return "hello world!";
    }

    private SideResults getSideResults(String userUid, QueryDocumentSnapshot result, Side side, TestName testName) {
        String hoursSinceLastMed = String.valueOf(result.getData().get(FieldName.HOURS_SINCE_LAST_MED.name));
        Map<String, Object> resultFingerTapping = getResult(userUid, result.getId(), side, testName);

        return SideResults.builder()
                .date(result.getId())
                .medicineSupply(hoursSinceLastMed)
                .side(String.valueOf(side))
                .data(resultFingerTapping)
                .build();
    }

    private Map<String, Object> getResult(String userUid, String testUid, Side side, TestName testName) {
        try {
            return testSideDocRef(userUid, testUid, side, testName).get().get().getData();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}