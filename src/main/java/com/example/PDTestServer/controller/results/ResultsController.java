package com.example.PDTestServer.controller.results;

import com.example.PDTestServer.controller.results.request.ResultRequestDTO;
import com.example.PDTestServer.controller.results.response.ResultsDTO;
import com.example.PDTestServer.service.results.ResultsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/test-results")
public class ResultsController {

    @Autowired
    ResultsService resultsService;
    
    @PostMapping("/{uid}")
    public List<ResultsDTO> getTestsResult(@PathVariable String uid, @RequestBody ResultRequestDTO resultRequestDTO) throws ExecutionException, InterruptedException {
        resultsService.getTestResultData(uid, resultRequestDTO);
        return resultsService.getTestResultData(uid, resultRequestDTO);
    }
}