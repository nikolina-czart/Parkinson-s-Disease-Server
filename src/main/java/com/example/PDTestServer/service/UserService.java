package com.example.PDTestServer.service;

import com.example.PDTestServer.controller.user.request.PatientTestDTO;
import com.example.PDTestServer.controller.user.request.UserDTO;
import com.example.PDTestServer.model.PatientTestDAO;
import com.example.PDTestServer.model.UserDAO;
import com.example.PDTestServer.repository.TestPatientRepository;
import com.example.PDTestServer.repository.UserRepository;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {
    private static final String COLLECTION_NAME = "users";

    @Autowired
    UserRepository userRepository;

    @Autowired
    TestPatientRepository testPatientRepository;

    public String saveUser(UserDTO userDTO, String uid) throws ExecutionException {
        userRepository.saveUser(uid, convertUserDTOToUserDAO(userDTO));
        testPatientRepository.savePatientTests(uid, getMapStringToPatientTestDAO(userDTO.getTestsAvailable()));

        return "Document with User ID " + uid + " has been saved successfully";
    }

    public UserDTO getUserDetails(String uid) throws ExecutionException, InterruptedException {
        UserDAO userDAO = userRepository.getUserDetails(uid);

        return convertUserDAOToUserDTO(userDAO);
    }

    private UserDTO convertUserDAOToUserDTO(UserDAO userDAO) {
        return UserDTO.builder()
                .name(userDAO.getName())
                .surname(userDAO.getSurname())
                .role(userDAO.getRole())
                .doctorID(userDAO.getRole())
                .testsAvailable(new HashSet<>())
                .build();
    }

    public String updateUser(UserDAO userDAO) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();

        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(COLLECTION_NAME).document(userDAO.getName()).set(userDAO);

        return collectionApiFuture.get().getUpdateTime().toString();
    }

    public String deleteUser(String uid) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();

        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(COLLECTION_NAME).document(uid).delete();

        return "Document with User ID " + uid + "has been deleted successfully";
    }

    private Map<String, PatientTestDAO> getMapStringToPatientTestDAO(Set<PatientTestDTO> testsAvailable) {
        Map<String, PatientTestDAO> patientTestDAOMap = new HashMap<>();

        testsAvailable.forEach(test -> patientTestDAOMap.put(test.getUid(),
                PatientTestDAO.builder().name(test.getName()).build()
        ));

        return patientTestDAOMap;
    }

    private UserDAO convertUserDTOToUserDAO(UserDTO userDTO) {
        return new UserDAO(userDTO.getName(), userDTO.getSurname(), userDTO.getRole(), userDTO.getDoctorID());
    }
}
