package com.example.PDTestServer.repository;

import com.example.PDTestServer.model.tests.ConfigTestDAO;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Repository
public class ConfigRepository {
    private static final String COLLECTION_TEST_NAME = "tests";
    public Set<ConfigTestDAO> getBaseTestDetails() throws ExecutionException, InterruptedException {
        Set<ConfigTestDAO> configTestDAOS = new HashSet<>();
        Firestore dbFirestore = FirestoreClient.getFirestore();
        List<QueryDocumentSnapshot> documents = dbFirestore.collection(COLLECTION_TEST_NAME).get().get().getDocuments();

        documents.forEach(document -> configTestDAOS.add(document.toObject(ConfigTestDAO.class)));

        return configTestDAOS;
    }
}
