package com.example.PDTestServer.controller.patientTests;

import com.example.PDTestServer.controller.patientTests.request.PatientTestDTO;
import com.example.PDTestServer.controller.patientTests.request.PatientTestSetDTO;
import com.example.PDTestServer.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/tests")
public class PatientTestController {

    @Autowired
    private TestService testService;

    @PostMapping("save/{uid}")
    public String saveTest(@PathVariable String uid, @RequestBody Set<PatientTestDTO> patientTestsSet) throws ExecutionException {
        return testService.saveTest(uid, patientTestsSet);
    }

    @GetMapping("/{uid}")
    public Set<PatientTestDTO> getTestDetails(@PathVariable String uid) throws ExecutionException, InterruptedException {
        return testService.getTestDetails(uid);
    }

    @DeleteMapping("/{uid}/{testID}")
    public String deleteTest(@PathVariable String uid, @PathVariable String testID) throws ExecutionException, InterruptedException {
        return testService.deleteTest(uid, testID);
    }
}
