package com.example.PDTestServer.controller.analysis;

import com.example.PDTestServer.controller.analysis.request.AnalysisRequestDTO;
import com.example.PDTestServer.model.analysis.fingerTapping.TappingAnalysisData;
import com.example.PDTestServer.model.analysis.gyroscope.TremorAnalysisData;
import com.example.PDTestServer.service.analysis.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/analysis-tests")
public class AnalysisController {

    @Autowired
    AnalysisService analysisService;

    @PostMapping("/{userUid}/chart-data")
    public List<TappingAnalysisData> analysisChartTest(@PathVariable String userUid, @RequestBody AnalysisRequestDTO analysisRequest) throws ExecutionException, InterruptedException {
        return analysisService.getTappingChartData(userUid, analysisRequest);
    }

    @PostMapping("/{userUid}/tremor/chart-data")
    public List<TremorAnalysisData> analysisTremorChartTest(@PathVariable String userUid, @RequestBody AnalysisRequestDTO analysisRequest) throws ExecutionException, InterruptedException {
        return analysisService.getTremorChartData(userUid, analysisRequest);
    }
}
