package com.example.PDTestServer.controller.role.admin;

import com.example.PDTestServer.service.role.DoctorService;
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

@RestController
class HelloWorldController {
    private static final String COLLECTION_USER_NAME = "users";
    private static final String COLLECTION_TEST_NAME = "tests";
    private static final String COLLECTION_TEST_HISTORY_NAME = "testsHistory";
    private static final String COLLECTION_TEST_DATES_NAME = "testDates";
    private static final String ROLE = "role";
    private static final String ROLE_PATIENT = "PATIENT";
    private static final String DOCTOR_ID = "doctorID";

    @Autowired
    private DoctorService doctorService;
    @GetMapping("/hello")
    public String hello() throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();

        List<QueryDocumentSnapshot> users = dbFirestore
                .collection(COLLECTION_USER_NAME)
                .whereEqualTo(DOCTOR_ID, "hqsbyjOGjJRJlE0qetx3T0v4FuB2")
                .whereEqualTo(ROLE, ROLE_PATIENT)
                .get()
                .get()
                .getDocuments();

        List<String> dates = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        users.forEach(user -> {
            CollectionReference collectionTestReference = dbFirestore
                    .collection(COLLECTION_USER_NAME)
                    .document(user.getId())
                    .collection(COLLECTION_TEST_HISTORY_NAME)
                    .document("FINGER_TAPPING")
                    .collection(COLLECTION_TEST_DATES_NAME);


            ApiFuture<QuerySnapshot> futureTest = collectionTestReference.get();

            List<QueryDocumentSnapshot> documents = null;
            try {
                documents = futureTest.get().getDocuments();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
            documents.forEach(document -> {
                if (document.exists()) {
                    dates.add(document.getId());
                }
            });

            dates.forEach(date -> {
                System.out.println("-----");
                System.out.println(date);
                System.out.println("-----");
                LocalDateTime localDateTime = LocalDateTime.from(formatter.parse(date));
                Timestamp timestamp = Timestamp.valueOf(localDateTime);

                Map<String, Object> update = new HashMap<>();
                update.put("createAt", timestamp);
                dbFirestore.collection(COLLECTION_USER_NAME)
                        .document(user.getId())
                        .collection(COLLECTION_TEST_HISTORY_NAME)
                        .document("FINGER_TAPPING")
                        .collection(COLLECTION_TEST_DATES_NAME)
                        .document(date).set(update, SetOptions.merge());
            });
        });

        return "hello world!";
    }
}