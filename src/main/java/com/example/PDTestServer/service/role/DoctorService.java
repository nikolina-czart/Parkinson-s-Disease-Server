package com.example.PDTestServer.service.role;

import com.example.PDTestServer.controller.role.doctor.request.PatientDTO;
import com.example.PDTestServer.controller.role.doctor.request.TestDetailDTO;
import com.example.PDTestServer.controller.role.user.request.UserDTO;
import com.example.PDTestServer.model.role.UserDAO;
import com.example.PDTestServer.model.tests.TestDetailsDAO;
import com.example.PDTestServer.repository.role.DoctorRepository;
import com.example.PDTestServer.repository.tests.PatientTestsRepository;
import com.example.PDTestServer.repository.role.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static com.example.PDTestServer.utils.coverter.PatientConverter.coverterUserDAOToPatientDTO;
import static com.example.PDTestServer.utils.coverter.TestConverter.convertTestDetailsDAOToTestDTO;
import static com.example.PDTestServer.utils.coverter.UserConverter.convertUserDAOToUserDTO;

@Service
public class DoctorService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    PatientTestsRepository patientTestsRepository;


    public boolean checkRole(String uid) throws ExecutionException, InterruptedException {
        UserDTO userDTO = convertUserDAOToUserDTO(userRepository.getUserDetails(uid));
        return userDTO.getRole().equals("DOCTOR");
    }

    public Set<PatientDTO> getDoctorPatients(String uid) throws ExecutionException, InterruptedException {
        Set<UserDAO> userDAOList = doctorRepository.getPatientsByDoctorId(uid);
        return convertSetPatientsDTOToDAO(userDAOList);
    }

    private Set<PatientDTO> convertSetPatientsDTOToDAO(Set<UserDAO> users) {
        Set<PatientDTO> patientDTOS = new HashSet<>();
        users.forEach(user -> patientDTOS.add(coverterUserDAOToPatientDTO(user, getPatientTests(user.getUid()))));

        return patientDTOS;
    }

    private Set<TestDetailDTO> getPatientTests(String uid){
        Set<TestDetailDTO> patientTestDetailDTOSet = new HashSet<>();

        try{
            Map<String, TestDetailsDAO> dataTestToTestDetails = patientTestsRepository.getTestByUser(uid);
            dataTestToTestDetails.forEach((key, value) -> patientTestDetailDTOSet.add(convertTestDetailsDAOToTestDTO(value, key)));
            return patientTestDetailDTOSet;
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
