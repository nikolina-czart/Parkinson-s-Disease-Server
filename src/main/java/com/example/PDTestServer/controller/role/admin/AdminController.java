package com.example.PDTestServer.controller.role.admin;

import com.example.PDTestServer.controller.role.admin.request.DoctorDTO;
import com.example.PDTestServer.service.role.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @GetMapping("/all-doctor")
    public List<DoctorDTO> getAllDoctors() throws ExecutionException, InterruptedException {
        return adminService.getAllDoctors();
    }

    @DeleteMapping("/remove/{uid}")
    public ResponseEntity<String> deleteDoctor(@PathVariable String uid) {
        return new ResponseEntity<>(adminService.deleteDoctor(uid), HttpStatus.OK);
    }
}