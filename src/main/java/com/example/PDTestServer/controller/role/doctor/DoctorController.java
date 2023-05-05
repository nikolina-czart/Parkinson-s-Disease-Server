package com.example.PDTestServer.controller.role.doctor;

import com.example.PDTestServer.controller.role.doctor.request.PatientDTO;
import com.example.PDTestServer.controller.role.doctor.request.PatientsDetails;
import com.example.PDTestServer.controller.role.doctor.request.SummaryPatients;
import com.example.PDTestServer.service.role.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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

    @GetMapping("/{uid}/patients-summary/details")
    public List<PatientsDetails> getPatientsSummaryDetails(@PathVariable String uid) throws ExecutionException, InterruptedException {
        return doctorService.getPatientDetails(uid);
    }

    @GetMapping("/{uid}/patients-summary")
    public List<SummaryPatients> getPatientsSummary(@PathVariable String uid) throws ExecutionException, InterruptedException {
        return doctorService.getPatientSummary(uid);
    }
}
