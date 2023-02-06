package com.example.PDTestServer.repository;

import com.example.PDTestServer.model.UserDAO;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.SetOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ExecutionException;

@Repository
public class UserRepository {
    private static final String COLLECTION_NAME = "users";

    public void saveUser(String uid, UserDAO userDAO) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        dbFirestore.collection(COLLECTION_NAME).document(uid).set(userDAO);
    }

    public UserDAO getUserDetails(String uid) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection(COLLECTION_NAME).document(uid);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();

        UserDAO userDAO;
        if (document.exists()) {
            userDAO = document.toObject(UserDAO.class);
            return userDAO;
        }
        return null;
    }

    //TODO correct update
    public void updateUser(String uid, UserDAO userDAO) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        dbFirestore.collection(COLLECTION_NAME).document(uid).set(userDAO, SetOptions.merge());
    }

    public void deleteUser(String uid) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        dbFirestore.collection(COLLECTION_NAME).document(uid).delete();
    }

}
