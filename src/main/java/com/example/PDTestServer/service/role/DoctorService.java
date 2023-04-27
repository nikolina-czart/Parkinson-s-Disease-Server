package com.example.PDTestServer.service.role;

import com.example.PDTestServer.controller.role.doctor.request.PatientDTO;
import com.example.PDTestServer.controller.role.doctor.request.TestDetailDTO;
import com.example.PDTestServer.controller.role.user.request.UserDTO;
import com.example.PDTestServer.model.role.UserDAO;
import com.example.PDTestServer.model.tests.TestDetailsDAO;
import com.example.PDTestServer.repository.role.DoctorRepository;
import com.example.PDTestServer.repository.tests.patient.PatientTestsRepository;
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

//TODO - good
@Service
public class DoctorService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    PatientTestsRepository patientTestsRepository;

    //TODO - good
    public boolean checkRole(String uid) throws ExecutionException, InterruptedException {
        UserDTO userDTO = convertUserDAOToUserDTO(userRepository.getUserDetails(uid));
        return userDTO.getRole().equals("DOCTOR");
    }

    //TODO - good
    public Set<PatientDTO> getDoctorPatients(String uid) throws ExecutionException, InterruptedException {
        Set<UserDAO> patientsByDoctorId = doctorRepository.getPatientsByDoctorId(uid);
        return getPatientsWithTests(patientsByDoctorId);
    }

    //TODO - good
    private Set<PatientDTO> getPatientsWithTests(Set<UserDAO> patients) {
        Set<PatientDTO> patientsWithTests = new HashSet<>();
        patients.forEach(patient -> patientsWithTests.add(coverterUserDAOToPatientDTO(patient, getTestsByPatient(patient.getUid()))));

        return patientsWithTests;
    }

    //TODO - good
    private Set<TestDetailDTO> getTestsByPatient(String uid){
        Set<TestDetailDTO> patientTestsDetail = new HashSet<>();

        try{
            Map<String, TestDetailsDAO> dataTestToTestDetails = patientTestsRepository.getTestByUser(uid);
            dataTestToTestDetails.forEach((key, value) -> patientTestsDetail.add(convertTestDetailsDAOToTestDTO(value, key)));
            return patientTestsDetail;
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
