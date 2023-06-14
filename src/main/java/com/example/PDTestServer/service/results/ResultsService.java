package com.example.PDTestServer.service.results;

import com.example.PDTestServer.controller.results.request.ResultRequestDTO;
import com.example.PDTestServer.controller.results.response.ResultsDTO;
import com.example.PDTestServer.controller.role.doctor.request.SaveTestDTO;
import com.example.PDTestServer.model.results.DateRangeTest;
import com.example.PDTestServer.model.results.Accel;
import com.example.PDTestServer.model.results.SideResults;
import com.example.PDTestServer.model.results.Tapping;
import com.example.PDTestServer.repository.tests.results.ResultsRepository;
import com.example.PDTestServer.service.SaveToFileService;
import com.example.PDTestServer.utils.enums.Side;
import com.example.PDTestServer.utils.enums.TestName;
import com.example.PDTestServer.utils.firebase.FieldName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.example.PDTestServer.utils.coverter.DateConverter.converterResultsRequestToDataRange;

@Service
public class ResultsService {
    private final static List<String> TAPPING_KEYS = Arrays.asList("timestamp", "upDown", "x", "y", "clickSide");
    private final static List<String> ACCEL_KEYS = Arrays.asList("timestamp", "x", "y", "z");

    @Autowired
    ResultsRepository resultsRepository;
    @Autowired
    SaveToFileService saveToFileService;

    public File saveTestToFile(String uid, SaveTestDTO saveTestDTO) {
        TestName testName = TestName.valueOfLabel(saveTestDTO.getTestName());
        Side side = Side.valueOfLabel(saveTestDTO.getSide());
        SideResults sideResults = resultsRepository.getResultDataToSave(uid, testName, saveTestDTO.getTestId(), side);
        return saveToFileService.save(sideResults, saveTestDTO);
    }

    public List<ResultsDTO> getTestResultData(String uid, ResultRequestDTO resultRequestDTO) throws ExecutionException, InterruptedException {
        TestName testName = TestName.valueOfLabel(resultRequestDTO.getTestNameID());
        return getResultsDataByTestType(uid, testName, converterResultsRequestToDataRange(resultRequestDTO));
    }

    public List<ResultsDTO> getResultsDataByTestType(String uid, TestName testName, DateRangeTest resultRequestDTO) throws ExecutionException, InterruptedException {
        List<ResultsDTO> results = new ArrayList<>();
        List<SideResults> sideResults = resultsRepository.getResultData(uid, resultRequestDTO, testName);
        sideResults.forEach(data -> {
            if(data.getData() != null) {
                results.add(getResultData(data));
            }
        });

        return results;
    }

    private ResultsDTO getResultData(SideResults result) {
        Accel accel = null;
        Tapping tapping = null;

        if(result.getData().containsKey(FieldName.ACCEL.name)){
            accel = createListAccelData((ArrayList<String>) result.getData().get(FieldName.ACCEL.name));
        }
        if(result.getData().containsKey(FieldName.TAPPING_DATA.name)){
            tapping = createListTappingData((ArrayList<String>) result.getData().get(FieldName.TAPPING_DATA.name));
        }

        return ResultsDTO.builder()
                .date(result.getDate())
                .medicineSupply(result.getMedicineSupply())
                .side(result.getSide())
                .accelData(accel)
                .tappingData(tapping)
                .build();
    }

    private Tapping createListTappingData(ArrayList<String> tappingData) {
        tappingData.remove(0);

        return createTappingData(tappingData);
    }

    private Accel createListAccelData(ArrayList<String> accelData) {
        accelData.remove(0);
        return createAccelData(accelData);
    }

    private Accel createAccelData(ArrayList<String> accelData) {
        Map<String, List<String>> result = getResultMap(accelData, ACCEL_KEYS);

        return new Accel(result.get("timestamp"), result.get("x"), result.get("y"), result.get("z"));
    }

    private Tapping createTappingData(ArrayList<String> data) {
        Map<String, List<String>> result = getResultMap(data, TAPPING_KEYS);

        return new Tapping(result.get("timestamp"), result.get("upDown"), result.get("x"), result.get("y"), result.get("clickSide"));
    }

    private Map<String, List<String>> getResultMap(ArrayList<String> data, List<String> keys) {
        return  IntStream.range(0, keys.size())
                .boxed()
                .collect(Collectors.toMap(keys::get, i -> data.stream()
                        .map(s -> s.split(","))
                        .map(arr -> arr[i])
                        .collect(Collectors.toList())));
    }

}
