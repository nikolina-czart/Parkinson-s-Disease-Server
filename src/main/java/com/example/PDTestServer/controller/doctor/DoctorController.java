package com.example.PDTestServer.controller.doctor;

import com.example.PDTestServer.controller.doctor.request.UserWithTestDTO;
import com.example.PDTestServer.controller.user.request.UserDTO;
import com.example.PDTestServer.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    @Autowired
    DoctorService doctorService;


    @GetMapping("hasRole/{uid}")
    public boolean saveTest(@PathVariable String uid) throws ExecutionException, InterruptedException {
        return doctorService.checkRole(uid);
    }

    @GetMapping("/{uid}/patient")
    public Set<UserWithTestDTO> getDoctorPatients(@PathVariable String uid) throws ExecutionException, InterruptedException {
        System.out.println(uid);
        return doctorService.getDoctorPatients(uid);
    }
}
