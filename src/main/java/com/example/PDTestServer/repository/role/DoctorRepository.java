package com.example.PDTestServer.repository.role;

import com.example.PDTestServer.model.role.UserDAO;
import com.example.PDTestServer.utils.enums.TestName;
import com.google.cloud.firestore.AggregateQuery;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static com.example.PDTestServer.utils.firebase.FirebaseQuery.patientByDoctorId;
import static com.example.PDTestServer.utils.firebase.FirebaseQuery.patientByDoctorIdOrGroup;
import static com.example.PDTestServer.utils.firebase.FirebaseReference.userTestDatesColRef;

@Repository
public class DoctorRepository {

    public Set<UserDAO> getPatientsByDoctorId(String uid) throws ExecutionException, InterruptedException {
        Set<UserDAO> userDAOS = new HashSet<>();
        List<QueryDocumentSnapshot> documents = patientByDoctorId(uid).get().get().getDocuments();
        documents.forEach(document -> userDAOS.add(document.toObject(UserDAO.class)));

        return userDAOS;
    }

    public Set<UserDAO> getPatientsByDoctorIdByControlGroup(String uid, boolean controlGroup) throws ExecutionException, InterruptedException {
        Set<UserDAO> userDAOS = new HashSet<>();
        List<QueryDocumentSnapshot> documents = patientByDoctorIdOrGroup(uid, controlGroup).get().get().getDocuments();
        documents.forEach(document -> userDAOS.add(document.toObject(UserDAO.class)));

        return userDAOS;
    }

    public String getNumberPatientPD(String uid) throws ExecutionException, InterruptedException {
        long count = patientByDoctorIdOrGroup(uid, false).count().get().get().getCount();
        return String.valueOf(count);
    }

    public String getNumberControls(String uid) throws ExecutionException, InterruptedException {
        long count = patientByDoctorIdOrGroup(uid, true).count().get().get().getCount();
        return String.valueOf(count);
    }

    public String getNumberTests(String uid, boolean controlGroup, TestName testName) throws ExecutionException, InterruptedException {
        Set<UserDAO> userDAOS = new HashSet<>();
        List<QueryDocumentSnapshot> documents = patientByDoctorIdOrGroup(uid, controlGroup).get().get().getDocuments();
        documents.forEach(document -> userDAOS.add(document.toObject(UserDAO.class)));

        List<Long> counts = new ArrayList<>();
        userDAOS.forEach(userDAO -> {
            CollectionReference testDatesCollectionReference = userTestDatesColRef(userDAO.getUid(), testName.name);
            try {
                long temp = testDatesCollectionReference.count().get().get().getCount();
                counts.add(temp);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
        long sum = counts.stream().mapToLong(Long::longValue).sum();
        return String.valueOf(sum);
    }
}
