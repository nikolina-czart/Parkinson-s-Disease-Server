package com.example.PDTestServer.utils.firebase;

import com.example.PDTestServer.utils.enums.TestName;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.firebase.cloud.FirestoreClient;

import java.sql.Timestamp;

import static com.example.PDTestServer.utils.enums.Role.PATIENT;
import static com.example.PDTestServer.utils.firebase.FieldName.DOCTOR_ID;
import static com.example.PDTestServer.utils.firebase.FieldName.ROLE;
import static com.example.PDTestServer.utils.firebase.FirebaseReference.testDatesColRef;

public class FirebaseQuery {

    public static Query patientByDoctorId(String doctorId) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        return dbFirestore
                .collection(CollectionName.USERS.name)
                .whereEqualTo(DOCTOR_ID.name, doctorId)
                .whereEqualTo(ROLE.name, PATIENT.name);
    }

    public static Query resultsInRangeTime(String userUid, Timestamp fromDate, Timestamp toDate, TestName testName) {
        return testDatesColRef(userUid, testName)
                .whereGreaterThanOrEqualTo(FieldName.CREATE_AT.name, fromDate)
                .whereLessThanOrEqualTo(FieldName.CREATE_AT.name, toDate);
    }
}
