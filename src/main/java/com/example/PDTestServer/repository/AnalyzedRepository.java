package com.example.PDTestServer.repository;

import com.example.PDTestServer.controller.analysis.request.AnalyzedRequestDTO;
import com.example.PDTestServer.model.FingerTappingTestResultDAO;
import com.example.PDTestServer.model.GyroscopeTestResultDAO;
import com.example.PDTestServer.model.TestResultDAO;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Repository
public class AnalyzedRepository {

    private static final String COLLECTION_USER_NAME = "users";
    private static final String COLLECTION_TEST_NAME = "tests";
    private static final String COLLECTION_TEST_HISTORY_NAME = "testsHistory";
    private static final String COLLECTION_TEST_DATES_NAME = "testDates";
    private static final String KEY_DATA = "date";


    public List<TestResultDAO> getAggregatedData(String uid, AnalyzedRequestDTO analyzedRequestDTO) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();


        List<TestResultDAO> results = new ArrayList<>();

        String testName = analyzedRequestDTO.getTestNameID();
//        String timeRange = analyzedRequestDTO.getTimeRange();

        CollectionReference testDatesCollectionReference = getTestDatesCollectionReference(dbFirestore, uid, testName);

        List<String> datesID = new ArrayList<>();
        ApiFuture<QuerySnapshot> fingerTappingFuture = testDatesCollectionReference.get();
        Map<String, String> hoursSinceLastMedByTestTimeMap = new HashMap<>();

        fingerTappingFuture.get().forEach(document -> {
            String medicineSupply = "After medicine";
            datesID.add(document.getId());
            ApiFuture<DocumentSnapshot> leftSideFuture = testDatesCollectionReference.document(document.getId()).collection("LEFT").document("testData").get();
            ApiFuture<DocumentSnapshot> rightSideFuture = testDatesCollectionReference.document(document.getId()).collection("RIGHT").document("testData").get();

            String hoursSinceLastMed = getHoursSinceLastMed(testDatesCollectionReference, document.getId());
            hoursSinceLastMedByTestTimeMap.put(document.getId(), hoursSinceLastMed);

            ArrayList<String> accelLeft = getArrayList(leftSideFuture, "accel");
            ArrayList<String> accelRight = getArrayList(rightSideFuture, "accel");

            if(Integer.parseInt(hoursSinceLastMed)==0){
                medicineSupply = "Before medicine";
            }

            results.add(new TestResultDAO(document.getId(), accelLeft, Integer.parseInt(hoursSinceLastMed), "LEFT", medicineSupply));
            results.add(new TestResultDAO(document.getId(), accelRight, Integer.parseInt(hoursSinceLastMed), "RIGHT", medicineSupply));
        });

        return results;
    }



    private CollectionReference getTestDatesCollectionReference(Firestore dbFirestore, String uid, String testName) {
        return dbFirestore
                .collection(COLLECTION_USER_NAME)
                .document(uid)
                .collection(COLLECTION_TEST_HISTORY_NAME)
                .document(testName)
                .collection(COLLECTION_TEST_DATES_NAME);
    }

    private String getHoursSinceLastMed(CollectionReference testDatesCollectionReference, String dateID) {
        try {
            return testDatesCollectionReference.document(dateID).get().get().get("hoursSinceLastMed").toString();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private ArrayList<String> getArrayList(ApiFuture<DocumentSnapshot> sideFuture, String name) {
        try {
            return (ArrayList<String>) sideFuture.get().get(name);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }


}
