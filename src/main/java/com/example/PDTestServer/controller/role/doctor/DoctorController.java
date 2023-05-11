package com.example.PDTestServer.controller.role.doctor;

import com.example.PDTestServer.controller.role.doctor.request.PatientDTO;
import com.example.PDTestServer.controller.role.doctor.request.PatientsDetails;
import com.example.PDTestServer.controller.role.doctor.request.SaveTestDTO;
import com.example.PDTestServer.controller.role.doctor.request.SummaryPatients;
import com.example.PDTestServer.controller.tests.patient.request.PatientTestDTO;
import com.example.PDTestServer.service.results.ResultsService;
import com.example.PDTestServer.service.role.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    @Autowired
    DoctorService doctorService;

    @Autowired
    ResultsService resultsService;


    @GetMapping("hasRole/{uid}")
    public boolean saveTest(@PathVariable String uid) throws ExecutionException, InterruptedException {
        return doctorService.checkRole(uid);
    }

    @GetMapping("/{uid}/patients")
    public Set<PatientDTO> getDoctorPatients(@PathVariable String uid) throws ExecutionException, InterruptedException {
        return doctorService.getDoctorPatients(uid);
    }

    @PostMapping("/{uid}/save-data-to-file")
    public ResponseEntity<byte[]> saveTest(@PathVariable String uid, @RequestBody SaveTestDTO saveTestDTO) throws IOException {

        File file = resultsService.saveTestToFile(uid, saveTestDTO);

        byte[] readAllBytes = Files.readAllBytes(file.toPath());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentLength(readAllBytes.length);
        headers.setContentDispositionFormData("attachment", saveTestDTO.getFileName());

        return ResponseEntity.ok()
                .headers(headers)
                .body(readAllBytes);
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
