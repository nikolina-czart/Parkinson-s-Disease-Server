package com.example.PDTestServer.utils.coverter;

import com.example.PDTestServer.controller.role.user.request.UserDTO;
import com.example.PDTestServer.model.role.UserDAO;

//TODO - good
public class UserConverter {

    //TODO - good
    public static UserDAO convertUserDTOToUserDAO(UserDTO userDTO) {
        return new UserDAO(userDTO.getUid(), userDTO.getName(), userDTO.getSurname(), userDTO.getEmail(),
                userDTO.getRole(), userDTO.getDoctorID());
    }

    //TODO - good
    public static UserDTO convertUserDAOToUserDTO(UserDAO userDAO) {
        return UserDTO.builder()
                .uid(userDAO.getUid())
                .name(userDAO.getName())
                .surname(userDAO.getSurname())
                .email(userDAO.getEmail())
                .role(userDAO.getRole())
                .doctorID(userDAO.getDoctorID())
                .build();
    }
}
