package com.example.PDTestServer.repository;

import com.example.PDTestServer.model.tests.ConfigTestDAO;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static com.example.PDTestServer.utils.firebase.FirebaseReference.configTestColRef;

@Repository
public class ConfigRepository {

    public Set<ConfigTestDAO> getBaseTestDetails() throws ExecutionException, InterruptedException {
        Set<ConfigTestDAO> configTestDAOS = new HashSet<>();
        List<QueryDocumentSnapshot> documents = configTestColRef().get().get().getDocuments();
        documents.forEach(document -> configTestDAOS.add(document.toObject(ConfigTestDAO.class)));

        return configTestDAOS;
    }
}
