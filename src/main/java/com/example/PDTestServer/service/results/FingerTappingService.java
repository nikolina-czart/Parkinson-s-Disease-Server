package com.example.PDTestServer.service.results;

import com.example.PDTestServer.repository.tests.results.ResultsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FingerTappingService {


    @Autowired
    ResultsRepository resultsRepository;


}
