package com.example.PDTestServer.service.results;

import com.example.PDTestServer.controller.results.response.ResultsDTO;
import com.example.PDTestServer.model.DateRangeTest;
import com.example.PDTestServer.model.results.SideResults;
import com.example.PDTestServer.repository.tests.results.ResultsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TremorsService {
    private final static List<String> ACCEL_KEYS = Arrays.asList("timestamp", "x", "y", "z");

    @Autowired
    ResultsRepository resultsRepository;
    public List<ResultsDTO> getGyroscopeData(String uid, DateRangeTest resultRequestDTO) {
        List<ResultsDTO> results = new ArrayList<>();
//        List<SideResults> fingerTappingData = resultsRepository.getFingerTappingData(uid, resultRequestDTO);
//        fingerTappingData.forEach(data -> results.add(getResultData(data)));

        return results;
    }
}
