package com.example.PDTestServer.service.results;

import com.example.PDTestServer.controller.results.response.ResultsDTO;
import com.example.PDTestServer.model.DateRangeTest;
import com.example.PDTestServer.model.results.Accel;
import com.example.PDTestServer.model.results.SideResults;
import com.example.PDTestServer.model.results.Tapping;
import com.example.PDTestServer.repository.tests.results.ResultsRepository;
import com.example.PDTestServer.utils.firebase.FieldName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class FingerTappingService {


    @Autowired
    ResultsRepository resultsRepository;


}
