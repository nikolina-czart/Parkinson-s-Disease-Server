package com.example.PDTestServer.service.role;

import com.example.PDTestServer.controller.role.admin.request.DoctorDTO;
import com.example.PDTestServer.repository.role.AdminRepository;
import com.example.PDTestServer.repository.role.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class AdminService {

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    UserRepository userRepository;

    public List<DoctorDTO> getAllDoctors() throws ExecutionException, InterruptedException {
        return adminRepository.getAllDoctors();
    }

    public String deleteDoctor(String uid) {
        userRepository.deleteUser(uid);
        return "User has been deleted successfully";
    }
}
