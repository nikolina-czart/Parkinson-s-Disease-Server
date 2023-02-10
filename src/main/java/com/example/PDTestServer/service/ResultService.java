package com.example.PDTestServer.service;

import com.example.PDTestServer.controller.result.request.ResultRequestDTO;
import com.example.PDTestServer.controller.result.response.*;
import com.example.PDTestServer.model.FingerTappingTestResultDAO;
import com.example.PDTestServer.model.GyroscopeTestResultDAO;
import com.example.PDTestServer.repository.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class ResultService {

    @Autowired
    ResultRepository resultRepository;

    public List<TestResultResponseDTO> getTestResultData(String uid, ResultRequestDTO resultRequestDTO) throws ExecutionException, InterruptedException {
        List<TestResultResponseDTO> result = new ArrayList<>();
        if(resultRequestDTO.getTestNameID().contains("FINGER_TAPPING")){
            return getFingerTappingData(uid, resultRequestDTO);
        }
        if(resultRequestDTO.getTestNameID().contains(" GYROSCOPE_TEST")){
            return getGyroscopeData(uid, resultRequestDTO);
        }
        return result;
    }

    public List<TestResultResponseDTO> getFingerTappingData(String uid, ResultRequestDTO resultRequestDTO) throws ExecutionException, InterruptedException {
        List<TestResultResponseDTO> result = new ArrayList<>();
        List<FingerTappingTestResultDAO> fingerTappingData = resultRepository.getFingerTappingData(uid, resultRequestDTO);

        fingerTappingData.forEach(data -> {
            StandardChartDTO standardChart = createStandardChart(data.getAccel());
            FingerTappingChartDTO upDownChartDTO = createUpDownChartDTO(data.getData());

            result.add(TestResultResponseDTO.builder()
                    .testDate(data.getDate())
                    .medicineSupply(data.getMedicineSupply())
                    .side(data.getSide())
                    .timestamp(standardChart.getTimestamp())
                    .x(standardChart.getX())
                    .y(standardChart.getY())
                    .z(standardChart.getZ())
                    .timestampUpDown(upDownChartDTO.getTimestamp())
                    .upDown(upDownChartDTO.getUpDown())
                    .build());
        }
        );

        return result;
    }



    public List<TestResultResponseDTO> getGyroscopeData(String uid, ResultRequestDTO resultRequestDTO) throws ExecutionException, InterruptedException {
        List<TestResultResponseDTO> result = new ArrayList<>();
        List<GyroscopeTestResultDAO> gyroscopeData = resultRepository.getGyroscopeData(uid, resultRequestDTO);

        gyroscopeData.forEach(data -> {
            StandardChartDTO standardChart = createStandardChart(data.getAccel());

            result.add(TestResultResponseDTO.builder()
                    .testDate(data.getDate())
                    .medicineSupply(data.getMedicineSupply())
                    .side(data.getSide())
                    .timestamp(standardChart.getTimestamp())
                    .x(standardChart.getX())
                    .y(standardChart.getY())
                    .z(standardChart.getZ())
                    .timestampUpDown(new ArrayList<>())
                    .upDown(new ArrayList<>())
                    .build());
        });

        return result;
    }

    private ResultGyroscopeByDayDTO createGyrocopeByDayDTO(GyroscopeTestResultDAO data) {
        return ResultGyroscopeByDayDTO.builder()
                .medicineSupply(data.getMedicineSupply())
                .side(data.getSide())
                .standardChart(createStandardChart(data.getAccel()))
                .build();
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

        if(!arrayList.isEmpty()){
            arrayList.remove(0);
        }

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
