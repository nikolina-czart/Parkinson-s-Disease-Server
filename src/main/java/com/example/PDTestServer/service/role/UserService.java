package com.example.PDTestServer.service.role;

import com.example.PDTestServer.controller.role.user.request.UserDTO;
import com.example.PDTestServer.model.role.UserDAO;
import com.example.PDTestServer.repository.tests.PatientTestsRepository;
import com.example.PDTestServer.repository.role.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

import static com.example.PDTestServer.utils.coverter.UserConverter.convertUserDAOToUserDTO;
import static com.example.PDTestServer.utils.coverter.UserConverter.convertUserDTOToUserDAO;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PatientTestsRepository patientTestsRepository;

    public String saveUser(UserDTO userDTO, String uid) {
        userRepository.saveUser(uid, convertUserDTOToUserDAO(userDTO));
        return "Document with User ID " + uid + " has been saved successfully";
    }

    public UserDTO getUserDetails(String uid) throws ExecutionException, InterruptedException {
        UserDAO userDAO = userRepository.getUserDetails(uid);
        return convertUserDAOToUserDTO(userDAO);
    }

    public String updateUser(String uid, UserDTO userDTO) {
        userRepository.updateUser(uid, convertUserDTOToUserDAO(userDTO));
        return "Document with User ID " + uid + " has been updated successfully";
    }

    public String deleteUser(String uid) {
        userRepository.deleteUser(uid);
        return "Document with User ID " + uid + " has been deleted successfully";
    }
}
