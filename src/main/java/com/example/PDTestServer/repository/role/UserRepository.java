package com.example.PDTestServer.repository.role;

import com.example.PDTestServer.controller.role.user.request.UserFieldUpdate;
import com.example.PDTestServer.model.role.UserDAO;
import com.google.cloud.firestore.DocumentSnapshot;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.Set;
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

    public void updateUser(String uid, Set<UserFieldUpdate> fieldsToUpdate) {
        fieldsToUpdate.forEach(field -> userDocRef(uid).update(Collections.singletonMap(field.getFieldName(), field.getFieldValue())));
    }

    public void deleteUser(String uid) {
        userDocRef(uid).delete();
    }
}
