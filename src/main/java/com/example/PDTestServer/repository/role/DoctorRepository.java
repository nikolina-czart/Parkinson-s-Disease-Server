package com.example.PDTestServer.repository.role;

import com.example.PDTestServer.model.role.UserDAO;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static com.example.PDTestServer.utils.firebase.FirebaseQuery.patientByDoctorId;

@Repository
public class DoctorRepository {

    //TODO - good
    public Set<UserDAO> getPatientsByDoctorId(String uid) throws ExecutionException, InterruptedException {
        Set<UserDAO> userDAOS = new HashSet<>();
        List<QueryDocumentSnapshot> documents = patientByDoctorId(uid).get().get().getDocuments();
        documents.forEach(document -> userDAOS.add(document.toObject(UserDAO.class)));

        return userDAOS;
    }
}
