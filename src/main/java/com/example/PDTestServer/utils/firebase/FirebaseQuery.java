package com.example.PDTestServer.utils.firebase;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.firebase.cloud.FirestoreClient;

import static com.example.PDTestServer.utils.enums.Role.PATIENT;
import static com.example.PDTestServer.utils.firebase.FieldName.DOCTOR_ID;
import static com.example.PDTestServer.utils.firebase.FieldName.ROLE;

public class FirebaseQuery {

    //TODO - good
    public static Query patientByDoctorId(String doctorId) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        return dbFirestore
                .collection(CollectionName.USERS.name)
                .whereEqualTo(DOCTOR_ID.name, doctorId)
                .whereEqualTo(ROLE.name, PATIENT.name);
    }
}
