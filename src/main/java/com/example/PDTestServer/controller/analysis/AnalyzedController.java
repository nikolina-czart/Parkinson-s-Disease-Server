package com.example.PDTestServer.controller.analysis;

import com.example.PDTestServer.controller.analysis.request.AggregateDataResponseDTO;
import com.example.PDTestServer.controller.analysis.request.AnalyzedRequestDTO;
import com.example.PDTestServer.controller.result.response.TestResultResponseDTO;
import com.example.PDTestServer.service.AnalyzedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/analyzed")
public class AnalyzedController {

//    @Autowired
//    AnalyzedService analyzedService;
//
//    @PostMapping("/{uid}/test")
//    public List<AggregateDataResponseDTO> getAnalyzedData(@PathVariable String uid, @RequestBody AnalyzedRequestDTO analyzedRequestDTO) throws ExecutionException, InterruptedException {
//        return analyzedService.getAggregatedData(uid, analyzedRequestDTO);
//    }

}
