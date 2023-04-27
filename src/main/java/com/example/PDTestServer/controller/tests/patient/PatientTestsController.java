package com.example.PDTestServer.controller.tests.patient;

import com.example.PDTestServer.controller.tests.patient.request.PatientTestDTO;
import com.example.PDTestServer.service.tests.PatientTestsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/patient-tests")
public class PatientTestsController {

    @Autowired
    private PatientTestsService patientTestService;

    @PostMapping("save/{uid}")
    public String saveTest(@PathVariable String uid, @RequestBody Set<PatientTestDTO> patientTestsSet) {
        return patientTestService.saveTest(uid, patientTestsSet);
    }

    @PostMapping("delete/{uid}")
    public String deleteTest(@PathVariable String uid, @RequestBody Set<PatientTestDTO> patientTestsSet) {
        return patientTestService.deleteTest(uid, patientTestsSet);
    }
}