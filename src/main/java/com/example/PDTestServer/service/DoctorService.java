package com.example.PDTestServer.service;

import com.example.PDTestServer.controller.doctor.request.UserWithTestDTO;
import com.example.PDTestServer.controller.patientTests.request.PatientTestDTO;
import com.example.PDTestServer.controller.user.request.UserDTO;
import com.example.PDTestServer.model.PatientTestDAO;
import com.example.PDTestServer.model.UserDAO;
import com.example.PDTestServer.repository.DoctorRepository;
import com.example.PDTestServer.repository.TestPatientRepository;
import com.example.PDTestServer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Service
public class DoctorService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    TestPatientRepository testPatientRepository;


    public boolean checkRole(String uid) throws ExecutionException, InterruptedException {
        UserDTO userDTO = convertUserDAOToUserDTO(userRepository.getUserDetails(uid));
        return userDTO.getRole().equals("DOCTOR");
    }

    public Set<UserWithTestDTO> getDoctorPatients(String uid) throws ExecutionException, InterruptedException {
        Set<UserDAO> userDAOList = doctorRepository.getDoctorPatients(uid);
        return createDoctorPatientsSet(userDAOList);
    }

    private Set<UserWithTestDTO> createDoctorPatientsSet(Set<UserDAO> userDAOList) {
        Set<UserWithTestDTO> userWithTestDTOS = new HashSet<>();

        userDAOList.forEach(userDAO -> {

            UserWithTestDTO user = UserWithTestDTO.builder()
                    .uid(userDAO.getUid())
                    .name(userDAO.getName())
                    .surname(userDAO.getSurname())
                    .email(userDAO.getEmail())
                    .patientTests(getPatientTest(userDAO.getUid()))
                    .build();

            userWithTestDTOS.add(user);
        });

        return userWithTestDTOS;
    }

    private Set<PatientTestDTO> getPatientTest(String uid){
        try{
            return getPatientTestDTOSet(testPatientRepository.getTestDetails(uid));
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private UserDTO convertUserDAOToUserDTO(UserDAO userDAO) {
        return UserDTO.builder()
                .name(userDAO.getName())
                .surname(userDAO.getSurname())
                .role(userDAO.getRole())
                .doctorID(userDAO.getRole())
                .build();
    }

    private Set<PatientTestDTO> getPatientTestDTOSet(Map<String, PatientTestDAO> patientTestDAOMap) {
        Set<PatientTestDTO> patientTestDTOSet = new HashSet<>();

        patientTestDAOMap.forEach((key, value) ->
                patientTestDTOSet.add(PatientTestDTO.builder()
                        .uid(key)
                        .name(value.getName())
                        .build())
        );

        return patientTestDTOSet;
    }

}
