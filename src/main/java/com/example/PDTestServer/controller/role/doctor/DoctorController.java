package com.example.PDTestServer.controller.role.doctor;

import com.example.PDTestServer.controller.role.doctor.request.PatientDTO;
import com.example.PDTestServer.service.role.DoctorService;
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

    @GetMapping("/{uid}/patients")
    public Set<PatientDTO> getDoctorPatients(@PathVariable String uid) throws ExecutionException, InterruptedException {
        return doctorService.getDoctorPatients(uid);
    }
}
