package com.example.PDTestServer.repository.role;

import com.example.PDTestServer.model.role.UserDAO;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.SetOptions;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ExecutionException;

import static com.example.PDTestServer.utils.firebase.FirebaseReference.userDocRef;

@Repository
public class UserRepository {

    public void saveUser(String uid, UserDAO userDAO) {
        userDocRef(uid).set(userDAO);
    }

    public UserDAO getUserDetails(String uid) throws ExecutionException, InterruptedException {
        DocumentSnapshot document = userDocRef(uid).get().get();
        return document.toObject(UserDAO.class);
    }

    //TODO correct update
    public void updateUser(String uid, UserDAO userDAO) {
        userDocRef(uid).set(userDAO, SetOptions.merge());
    }

    public void deleteUser(String uid) {
        userDocRef(uid).delete();
    }
}
