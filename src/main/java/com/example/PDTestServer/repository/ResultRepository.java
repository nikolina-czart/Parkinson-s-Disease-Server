package com.example.PDTestServer.repository;

import com.example.PDTestServer.controller.result.request.ResultRequestDTO;
import com.example.PDTestServer.model.FingerTappingTestResultDAO;
import com.example.PDTestServer.model.GyroscopeTestResultDAO;
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
        List<String> datesID = getTestDatesIDByRangeDate(testDatesCollectionReference, startDate, endDate);

        datesID.forEach((dateID) -> {
            ApiFuture<DocumentSnapshot> leftSideFuture = testDatesCollectionReference.document(dateID).collection("LEFT").document("testData").get();
            ApiFuture<DocumentSnapshot> rightSideFuture = testDatesCollectionReference.document(dateID).collection("RIGHT").document("testData").get();

            String hoursSinceLastMed = getHoursSinceLastMed(testDatesCollectionReference, dateID);

            ArrayList<String> accelLeft = getArrayList(leftSideFuture, "accel");
            ArrayList<String> dataLeft = getArrayList(leftSideFuture, "data");
            ArrayList<String> accelRight = getArrayList(rightSideFuture, "accel");
            ArrayList<String> dataRight = getArrayList(rightSideFuture, "data");

            results.add(new FingerTappingTestResultDAO(dateID, accelLeft, dataLeft, hoursSinceLastMed, "LEFT"));
            results.add(new FingerTappingTestResultDAO(dateID, accelRight, dataRight, hoursSinceLastMed, "RIGHT"));
        });

        return results;
    }

    public List<GyroscopeTestResultDAO> getGyroscopeData(String uid, ResultRequestDTO resultRequestDTO) throws ExecutionException, InterruptedException {
        List<GyroscopeTestResultDAO> results = new ArrayList<>();

        Firestore dbFirestore = FirestoreClient.getFirestore();

        String testName = resultRequestDTO.getTestNameID();
        LocalDate startDate = LocalDate.parse(resultRequestDTO.getFormDate());
        LocalDate endDate = LocalDate.parse(resultRequestDTO.getToDate());

        CollectionReference testDatesCollectionReference = getTestDatesCollectionReference(dbFirestore, uid, testName);
        List<String> datesID = getTestDatesIDByRangeDate(testDatesCollectionReference, startDate, endDate);

        datesID.forEach((dataID) -> {
            ApiFuture<DocumentSnapshot> leftSideFuture = testDatesCollectionReference.document(dataID).collection("LEFT").document("testData").get();
            ApiFuture<DocumentSnapshot> rightSideFuture = testDatesCollectionReference.document(dataID).collection("RIGHT").document("testData").get();

            String hoursSinceLastMed = getHoursSinceLastMed(testDatesCollectionReference, dataID);

            ArrayList<String> accelLeft = getArrayList(leftSideFuture, "accel");
            ArrayList<String> accelRight = getArrayList(rightSideFuture, "accel");

            results.add(new GyroscopeTestResultDAO(dataID, accelLeft, hoursSinceLastMed, "LEFT"));
            results.add(new GyroscopeTestResultDAO(dataID, accelRight, hoursSinceLastMed, "RIGHT"));
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

    private List<String> getTestDatesIDByRangeDate(CollectionReference collectionReference, LocalDate startDate, LocalDate endDate) throws ExecutionException, InterruptedException {
        List<String> dates = new ArrayList<>();
        List<String> datesAfterFilter = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        ApiFuture<QuerySnapshot> fingerTappingFuture = collectionReference.get();
        fingerTappingFuture.get().forEach(document -> {
            dates.add(document.getId());
        });

        dates.forEach((date) -> {
            LocalDateTime localDateTime = LocalDateTime.parse(date, formatter);
            LocalDate localDate = localDateTime.toLocalDate();
            boolean isInRange = (localDate.isAfter(startDate) & localDate.isBefore(endDate)) || localDate.isEqual(startDate) || localDate.isEqual(endDate);
            if(isInRange) {
                datesAfterFilter.add(date);
            }
        });

        return datesAfterFilter;
    }
}
