package com.example.PDTestServer.utils.firebase;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

public class FirebaseReference {

    public static DocumentReference userDocRef(String userUid) {
        Firestore db = FirestoreClient.getFirestore();
        return db.collection(CollectionName.USERS.name).document(userUid);
    }

    public static CollectionReference userTestColRef(String userUid) {
        Firestore db = FirestoreClient.getFirestore();
        return db.collection(CollectionName.USERS.name).document(userUid).collection(CollectionName.TESTS.name);
    }

    public static CollectionReference userTestDatesColRef(String userUid, String testUid) {
        Firestore db = FirestoreClient.getFirestore();
        return db.collection(CollectionName.USERS.name).document(userUid)
                .collection(CollectionName.TESTS_HISTORY.name).document(testUid)
                .collection(CollectionName.TEST_DATES.name);
    }

    public static DocumentReference patientTestDocRef(String userUid, String testUid) {
        Firestore db = FirestoreClient.getFirestore();
        return db.collection(CollectionName.USERS.name).document(userUid)
                .collection(CollectionName.TESTS.name).document(testUid);
    }

    public static DocumentReference patientTestHistoryDocRef(String userUid, String testUid) {
        Firestore db = FirestoreClient.getFirestore();
        return db.collection(CollectionName.USERS.name).document(userUid)
                .collection(CollectionName.TESTS_HISTORY.name).document(testUid);
    }

//    public static DocumentReference userTestReference(String userUid) {
//
//    }
//
//    public static DocumentReference userTestHistoryReference(String userUid) {
//
//    }


}
