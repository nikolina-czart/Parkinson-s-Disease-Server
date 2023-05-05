package com.example.PDTestServer.service.analysis;

import com.example.PDTestServer.controller.analysis.request.AnalysisRequestDTO;
import com.example.PDTestServer.model.analysis.fingerTapping.TappingAnalysisData;
import com.example.PDTestServer.model.analysis.gyroscope.TremorAnalysisData;
import com.example.PDTestServer.repository.tests.analysis.AnalysisRepository;
import com.example.PDTestServer.utils.enums.TestName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class AnalysisService {

    @Autowired
    AnalysisRepository analysisRepository;
    @Autowired
    AnalysisFingerTappingService analysisFingerTappingService;
    @Autowired
    AnalysisTremorService analysisTremorService;

    public List<TappingAnalysisData> getTappingChartData(String userUid, AnalysisRequestDTO analysisRequest) throws ExecutionException, InterruptedException {
        List<TappingAnalysisData> chartDate = analysisFingerTappingService.getChartDate(userUid, analysisRequest);
        return chartDate;
    }

    public List<TremorAnalysisData> getTremorChartData(String userUid, AnalysisRequestDTO analysisRequest) throws ExecutionException, InterruptedException {
        List<TremorAnalysisData> chartDate = analysisTremorService.getChartDate(userUid, analysisRequest);
        return chartDate;
    }
}
