package com.example.PDTestServer.repository;

import com.example.PDTestServer.controller.result.request.ResultRequestDTO;
import com.example.PDTestServer.model.FingerTappingTestResultDAO;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

@Repository
public class ResultRepository {

    private static final String COLLECTION_USER_NAME = "users";
    private static final String COLLECTION_TEST_NAME = "tests";
    private static final String COLLECTION_TEST_HISTORY_NAME = "testsHistory";
    private static final String COLLECTION_TEST_DATES_NAME = "testDates";
    private static final String KEY_DATA = "date";
    public List<FingerTappingTestResultDAO> getFingerTappingData(String uid, ResultRequestDTO resultRequestDTO) throws ExecutionException, InterruptedException {
        List<FingerTappingTestResultDAO> results = new ArrayList<>();

        Firestore dbFirestore = FirestoreClient.getFirestore();

        String testName = resultRequestDTO.getTestNameID();
        LocalDate startDate = LocalDate.parse(resultRequestDTO.getFormDate());
        LocalDate endDate = LocalDate.parse(resultRequestDTO.getToDate());

        CollectionReference testDatesCollectionReference = getTestDatesCollectionReference(dbFirestore, uid, testName);
        Map<String, String> datesID = getTestDatesIDByRangeDate(testDatesCollectionReference, startDate, endDate);

        datesID.forEach((k, v) -> {
            ApiFuture<DocumentSnapshot> leftSideFuture = testDatesCollectionReference.document(k).collection("LEFT").document("testData").get();
            ApiFuture<DocumentSnapshot> rightSideFuture = testDatesCollectionReference.document(k).collection("RIGHT").document("testData").get();

            String hoursSinceLastMed = getHoursSinceLastMed(testDatesCollectionReference, k);

            ArrayList<String> accelLeft = getArrayList(leftSideFuture, "accel");
            ArrayList<String> dataLeft = getArrayList(leftSideFuture, "data");
            ArrayList<String> accelRight = getArrayList(rightSideFuture, "accel");
            ArrayList<String> dataRight = getArrayList(rightSideFuture, "data");

            results.add(new FingerTappingTestResultDAO(v, accelLeft, dataLeft, hoursSinceLastMed, "LEFT"));
            results.add(new FingerTappingTestResultDAO(v, accelRight, dataRight, hoursSinceLastMed, "RIGHT"));
        });

        return results;
    }

    private ArrayList<String> getArrayList(ApiFuture<DocumentSnapshot> sideFuture, String name) {
        try {
            return (ArrayList<String>) sideFuture.get().get(name);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private String getHoursSinceLastMed(CollectionReference testDatesCollectionReference, String dateID) {
        try {
            return testDatesCollectionReference.document(dateID).get().get().get("hoursSinceLastMed").toString();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private CollectionReference getTestDatesCollectionReference(Firestore dbFirestore, String uid, String testName) {
        return dbFirestore
                .collection(COLLECTION_USER_NAME)
                .document(uid)
                .collection(COLLECTION_TEST_HISTORY_NAME)
                .document(testName)
                .collection(COLLECTION_TEST_DATES_NAME);
    }

    private Map<String, String> getTestDatesIDByRangeDate(CollectionReference collectionReference, LocalDate startDate, LocalDate endDate) throws ExecutionException, InterruptedException {
        Map<String, String> dates = new HashMap<>();
        Map<String, String> datesIDAfterFilter = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        ApiFuture<QuerySnapshot> fingerTappingFuture = collectionReference.get();
        fingerTappingFuture.get().forEach(document -> {
            dates.put(document.getId(), document.get("date").toString());
        });

        dates.forEach((k, v) -> {
            LocalDateTime localDateTime = LocalDateTime.parse(v, formatter);
            LocalDate localDate = localDateTime.toLocalDate();
            boolean isInRange = (localDate.isAfter(startDate) & localDate.isBefore(endDate)) || localDate.isEqual(startDate) || localDate.isEqual(endDate);
            if(isInRange) {
                datesIDAfterFilter.put(k, v);
            }
        });

        return datesIDAfterFilter;
    }

}
