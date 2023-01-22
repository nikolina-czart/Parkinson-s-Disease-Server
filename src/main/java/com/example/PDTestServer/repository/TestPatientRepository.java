package com.example.PDTestServer.repository;

import com.example.PDTestServer.model.PatientTestDAO;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Repository
public class TestPatientRepository {
    private static final String COLLECTION_USER_NAME = "users";
    private static final String COLLECTION_TEST_NAME = "tests";
    private static final String COLLECTION_TEST_HISTORY_NAME = "testsHistory";

    public void saveTestByPatient(String uid, Map<String, PatientTestDAO> testsAvailable) {
        Firestore dbFirestore = FirestoreClient.getFirestore();

        testsAvailable.forEach((key, value) -> {
            dbFirestore.collection(COLLECTION_USER_NAME)
                    .document(uid)
                    .collection(COLLECTION_TEST_NAME)
                    .document(key)
                    .set(value);

            dbFirestore.collection(COLLECTION_USER_NAME)
                    .document(uid)
                    .collection(COLLECTION_TEST_HISTORY_NAME)
                    .document(key)
                    .set(value);
                }
        );
    }

    public Map<String, PatientTestDAO> getTestDetails(String uid) throws ExecutionException, InterruptedException {
        Map<String, PatientTestDAO> patientTestDAOMap = new HashMap<>();

        Firestore dbFirestore = FirestoreClient.getFirestore();

        CollectionReference collectionReference = dbFirestore.collection(COLLECTION_USER_NAME).document(uid).collection(COLLECTION_TEST_NAME);
        ApiFuture<QuerySnapshot> future = collectionReference.get();

        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        documents.forEach(document -> {
            if (document.exists()) {
                PatientTestDAO patientTestDAO = document.toObject(PatientTestDAO.class);
                patientTestDAOMap.put(document.getId(), patientTestDAO);
            }
        });

        return patientTestDAOMap;
    }

    public void deleteTest(String uid, String testID) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        dbFirestore.collection(COLLECTION_USER_NAME).document(uid).collection(COLLECTION_TEST_NAME).document(testID).delete();
    }
}
