package com.example.PDTestServer.service;

import com.example.PDTestServer.controller.result.request.ResultRequestDTO;
import com.example.PDTestServer.controller.result.response.FingerTappingChartDTO;
import com.example.PDTestServer.controller.result.response.ResultFingerTappingByDayDTO;
import com.example.PDTestServer.controller.result.response.ResultFingerTappingDTO;
import com.example.PDTestServer.controller.result.response.StandardChartDTO;
import com.example.PDTestServer.model.FingerTappingTestResultDAO;
import com.example.PDTestServer.repository.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Service
public class ResultService {

    @Autowired
    ResultRepository resultRepository;

    public List<ResultFingerTappingDTO> getFingerTappingData(String uid, ResultRequestDTO resultRequestDTO) throws ExecutionException, InterruptedException {
        List<ResultFingerTappingDTO> result = new ArrayList<>();
        List<FingerTappingTestResultDAO> fingerTappingData = resultRepository.getFingerTappingData(uid, resultRequestDTO);

        fingerTappingData.forEach(data -> result.add(ResultFingerTappingDTO.builder()
                .testDate(data.getDate())
                .fingerTappingByDayDTO(createFingerTappingByDayDTO(data))
                .build()
        ));

        return result;
    }

    private ResultFingerTappingByDayDTO createFingerTappingByDayDTO(FingerTappingTestResultDAO data) {
        return ResultFingerTappingByDayDTO.builder()
                .medicineSupply(data.getMedicineSupply())
                .side(data.getSide())
                .standardChart(createStandardChart(data.getAccel()))
                .upDownChartDTO(createUpDownChartDTO(data.getData()))
                .build();
    }

    private StandardChartDTO createStandardChart(ArrayList<String> arrayList) {
        ArrayList<String> timestampArray = new ArrayList<>();
        ArrayList<String> x = new ArrayList<>();
        ArrayList<String> y = new ArrayList<>();
        ArrayList<String> z = new ArrayList<>();

        arrayList.remove(0);

        arrayList.forEach(s -> {
            String[] splits = s.split(",");
            timestampArray.add(splits[0]);
            x.add(splits[1]);
            y.add(splits[2]);
            z.add(splits[3]);
        });

        return StandardChartDTO.builder()
                .timestamp(timestampArray)
                .x(x)
                .y(y)
                .z(z)
                .build();
    }
    private FingerTappingChartDTO createUpDownChartDTO(ArrayList<String> data) {
        ArrayList<String> timestamp = new ArrayList<>();
        ArrayList<String> upDown = new ArrayList<>();

        data.remove(0);

        data.forEach(s -> {
            String[] splits = s.split(",");
            timestamp.add(splits[0]);
            upDown.add(splits[1].contains("UP") ? "1" : "0");
        });

        return FingerTappingChartDTO.builder()
                .timestamp(timestamp)
                .upDown(upDown)
                .build();
    }


}
