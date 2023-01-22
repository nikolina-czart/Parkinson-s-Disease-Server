package com.example.PDTestServer.service;

import com.example.PDTestServer.controller.user.request.UserDTO;
import com.example.PDTestServer.model.UserDAO;
import com.example.PDTestServer.repository.TestPatientRepository;
import com.example.PDTestServer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TestPatientRepository testPatientRepository;

    public String saveUser(UserDTO userDTO, String uid) {
        userRepository.saveUser(uid, convertUserDTOToUserDAO(userDTO));

        return "Document with User ID " + uid + " has been saved successfully";
    }

    public UserDTO getUserDetails(String uid) throws ExecutionException, InterruptedException {
        UserDAO userDAO = userRepository.getUserDetails(uid);
        return convertUserDAOToUserDTO(userDAO);
    }

    public String updateUser(String uid, UserDTO userDTO) {
        userRepository.upadateUser(uid, convertUserDTOToUserDAO(userDTO));
        return "Document with User ID " + uid + " has been updated successfully";
    }

    public String deleteUser(String uid) {
        userRepository.deleteUser(uid);
        return "Document with User ID " + uid + " has been deleted successfully";
    }

    private UserDTO convertUserDAOToUserDTO(UserDAO userDAO) {
        return UserDTO.builder()
                .name(userDAO.getName())
                .surname(userDAO.getSurname())
                .role(userDAO.getRole())
                .doctorID(userDAO.getRole())
                .build();
    }

    private UserDAO convertUserDTOToUserDAO(UserDTO userDTO) {
        return new UserDAO(userDTO.getName(), userDTO.getSurname(), userDTO.getRole(), userDTO.getDoctorID());
    }
}
