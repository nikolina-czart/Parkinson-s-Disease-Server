package com.example.PDTestServer.repository;

import com.example.PDTestServer.controller.user.request.PatientTestDTO;
import com.example.PDTestServer.model.PatientTestDAO;
import com.example.PDTestServer.model.UserDAO;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Repository
public class TestPatientRepository {
    private static final String COLLECTION_USER_NAME = "users";
    private static final String COLLECTION_TEST_NAME = "tests";

    public void savePatientTests(String uid, Map<String, PatientTestDAO> testsAvailable) {
        Firestore dbFirestore = FirestoreClient.getFirestore();

        testsAvailable.forEach((key, value) ->
                dbFirestore.collection(COLLECTION_USER_NAME)
                        .document(uid)
                        .collection(COLLECTION_TEST_NAME)
                        .document(key)
                        .set(value)
        );
    }

//    public PatientTestDAO getUserDetails(String uid) throws ExecutionException, InterruptedException {
//        Firestore dbFirestore = FirestoreClient.getFirestore();
//        DocumentReference documentReference = dbFirestore.collection(COLLECTION_NAME).document(uid);
//        ApiFuture<DocumentSnapshot> future = documentReference.get();
//        DocumentSnapshot document = future.get();
//
//        UserDAO userDAO;
//        if (document.exists()) {
//            userDAO = document.toObject(UserDAO.class);
//            return userDAO;
//        }
//        return null;
//    }
}
