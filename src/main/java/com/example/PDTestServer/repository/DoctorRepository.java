package com.example.PDTestServer.repository;

import com.example.PDTestServer.model.PatientTestDAO;
import com.example.PDTestServer.model.UserDAO;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Repository
public class DoctorRepository {
    private static final String COLLECTION_USER_NAME = "users";
    private static final String ROLE = "role";
    private static final String ROLE_PATIENT = "PATIENT";
    private static final String DOCTOR_ID = "doctorID";

    public Set<UserDAO> getDoctorPatients(String uid) throws ExecutionException, InterruptedException {
        Set<UserDAO> userDAOS = new HashSet<>();
        Firestore dbFirestore = FirestoreClient.getFirestore();

        ApiFuture<QuerySnapshot> future = dbFirestore
                .collection(COLLECTION_USER_NAME)
                .whereEqualTo(DOCTOR_ID, uid)
                .whereEqualTo(ROLE, ROLE_PATIENT)
                .get();

        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        documents.forEach(document -> {
            userDAOS.add(document.toObject(UserDAO.class));
        });

        return userDAOS;
    }
}
