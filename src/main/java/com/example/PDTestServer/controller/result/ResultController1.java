package com.example.PDTestServer.controller.result;

import com.example.PDTestServer.controller.results.request.ResultRequestDTO;
import com.example.PDTestServer.controller.result.response.TestResultResponseDTO;
import com.example.PDTestServer.service.results.ResultsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/test-results1")
public class ResultController1 {

    @Autowired
    ResultsService resultsService;

    //TODO - good
//    @PostMapping("/{uid}/test")
//    public List<TestResultResponseDTO> getTestsResult(@PathVariable String uid, @RequestBody ResultRequestDTO resultRequestDTO) throws ExecutionException, InterruptedException {
//        return resultsService.getTestResultData(uid, resultRequestDTO);
//    }
//
//    @PostMapping("/{uid}/all")
//    public String getAllResult(@PathVariable String uid, @RequestBody ResultRequestDTO resultRequestDTO) throws ExecutionException, InterruptedException {
//        List<FingerTappingTestResultDAO> testAllResultData = resultService.getTestAllResultData(uid, resultRequestDTO);
//        return String.valueOf(testAllResultData.size());
//    }

}
