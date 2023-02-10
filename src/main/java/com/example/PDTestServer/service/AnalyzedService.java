package com.example.PDTestServer.service;

import com.example.PDTestServer.controller.analysis.request.AggregateDataResponseDTO;
import com.example.PDTestServer.controller.analysis.request.AnalyzedRequestDTO;
import com.example.PDTestServer.model.TestResultDAO;
import com.example.PDTestServer.repository.AnalyzedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class AnalyzedService {
    @Autowired
    AnalyzedRepository analyzedRepository;

    public List<AggregateDataResponseDTO> getAggregatedData(String uid, AnalyzedRequestDTO analyzedRequestDTO) throws ExecutionException, InterruptedException {
        List<AggregateDataResponseDTO> result = new ArrayList<>();
        List<TestResultDAO> testResultDAOList = analyzedRepository.getAggregatedData(uid, analyzedRequestDTO);

        if(analyzedRequestDTO.getTimeRange().equals("Miesiąc")) {
            result = getMonthAggregateData(testResultDAOList);
        }else if(analyzedRequestDTO.getTimeRange().equals("Trzy miesiące")) {
            result = getThreeMonthAggregateData(testResultDAOList);
        }else if (analyzedRequestDTO.getTimeRange().equals("Pół roku")) {
            result = getSixMonthAggregateData(testResultDAOList);
        }


        return result;
    }

    private List<AggregateDataResponseDTO> getMonthAggregateData(List<TestResultDAO> testResultDAOList) {
        Map<String, ArrayList<TestResultDAO>> monthLeftBeforeMedicineMap = mapResultByMonthYearLabel(testResultDAOList, "LEFT", "Before medicine");
        Map<String, ArrayList<TestResultDAO>> monthLeftAfterMedicineMap = mapResultByMonthYearLabel(testResultDAOList, "LEFT", "After medicine");
        Map<String, ArrayList<TestResultDAO>> monthRightBeforeMedicineMap = mapResultByMonthYearLabel(testResultDAOList, "RIGHT", "Before medicine");
        Map<String, ArrayList<TestResultDAO>> monthRightAfterMedicineMap = mapResultByMonthYearLabel(testResultDAOList, "RIGHT", "After medicine");;

        List<AggregateDataResponseDTO> result_1 = createAggregateDataResponseDTO(monthLeftBeforeMedicineMap, "LEFT", "Before medicine");
        List<AggregateDataResponseDTO> result_2 = createAggregateDataResponseDTO(monthLeftAfterMedicineMap, "LEFT", "After medicine");
        List<AggregateDataResponseDTO> result_3 = createAggregateDataResponseDTO(monthRightBeforeMedicineMap, "RIGHT", "Before medicine");
        List<AggregateDataResponseDTO> result_4 = createAggregateDataResponseDTO(monthRightAfterMedicineMap, "RIGHT", "After medicine");

        List<AggregateDataResponseDTO> resultAll = new ArrayList<>();
        resultAll.addAll(result_1);
        resultAll.addAll(result_2);
        resultAll.addAll(result_3);
        resultAll.addAll(result_4);
        return resultAll;
    }

    private List<AggregateDataResponseDTO> createAggregateDataResponseDTO(Map<String, ArrayList<TestResultDAO>> listMap, String side, String medicineSupply){
        List<AggregateDataResponseDTO> result = new ArrayList<>();
        listMap.forEach((key, testResultList) -> {
            int sum = testResultList.stream().mapToInt(TestResultDAO::getHoursSinceLastMed).sum();
            String hoursSinceLastMedAverage = String.valueOf(Math.round(sum / testResultList.size() * 100.0) / 100);
            String testLabel = key;
            ArrayList<ArrayList<Double>> aggregatedArray = new ArrayList<>();

            testResultList.forEach(testResult -> {
                ArrayList<String> accel = testResult.getAccel();
                if(!accel.isEmpty()){
                    accel.remove(0);
                }
                ArrayList<Double> temp = new ArrayList<>();
                accel.forEach(row -> {
                    String[] splits = row.split(",");
                    Double timestamp = Double.parseDouble(splits[0]);
                    if(timestamp > 5 && timestamp < 25){
                        double x = Double.parseDouble(splits[1]);
                        double y = Double.parseDouble(splits[1]);
                        double z = Double.parseDouble(splits[1]);
                        double xyz = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
                        temp.add(xyz);
                    }
                });
                aggregatedArray.add(temp);
            });

            result.add(AggregateDataResponseDTO.builder()
                            .testLabel(testLabel)
                            .hoursSinceLastMedAverage(hoursSinceLastMedAverage)
                            .side(side)
                            .medicineSupply(medicineSupply)
                            .vectorLength(aggregatedArray.get(0).size())
                            .result(aggregatedArray)
                    .build());
        });

        return result;
    }

    private List<AggregateDataResponseDTO> getSixMonthAggregateData(List<TestResultDAO> testResultDAOList) {
        return null;
    }

    private List<AggregateDataResponseDTO> getThreeMonthAggregateData(List<TestResultDAO> testResultDAOList) {
        return null;
    }

    private Map<String, ArrayList<TestResultDAO>> mapResultByMonthYearLabel(List<TestResultDAO> testResultDAOList, String side, String supplyMed) {
        List<TestResultDAO> testResultLeftBefore = testResultDAOList.stream().filter(testResult -> testResult.getSide().equals(side) & testResult.getMedicineSupply().equals(supplyMed)).toList();

        Map<String, ArrayList<TestResultDAO>> result = new HashMap<>();
        testResultLeftBefore.forEach(testResult -> {
            String label = getMonthYearLabel(testResult.getDate());

            if (!result.containsKey(label)) {
                result.put(label, new ArrayList<>());
            }
            result.get(label).add(testResult);
        });

        return result;
    }

    private String getMonthYearLabel(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime localDateTime = LocalDateTime.parse(date, formatter);
        LocalDate localDate = localDateTime.toLocalDate();
        return localDate.getMonth().name() + " " + localDate.getYear();
    }
}
