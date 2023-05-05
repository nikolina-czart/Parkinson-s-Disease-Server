package com.example.PDTestServer.service.analysis;

import com.example.PDTestServer.controller.analysis.request.AnalysisRequestDTO;
import com.example.PDTestServer.model.analysis.fingerTapping.TappingAnalysisData;
import com.example.PDTestServer.model.analysis.fingerTapping.TappingAnalysisDataParameter;
import com.example.PDTestServer.model.analysis.fingerTapping.TappingAnalysisDataParameterDetails;
import com.example.PDTestServer.model.results.Tapping;
import com.example.PDTestServer.repository.tests.analysis.AnalysisRepository;
import com.example.PDTestServer.utils.enums.ResultGroup;
import com.example.PDTestServer.utils.enums.Side;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class AnalysisFingerTappingService {

    @Autowired
    AnalysisRepository analysisRepository;

    public List<TappingAnalysisData> getChartDate(String userUid, AnalysisRequestDTO analysisRequest) throws ExecutionException, InterruptedException {
        List<TappingAnalysisData> results = new ArrayList<>();
        Map<String, Map<String, List<Tapping>>> dataFingerTapping = analysisRepository.getDataFingerTapping(userUid, analysisRequest.getPeriod());

        dataFingerTapping.forEach((key, value) -> results.add(createTappingAnalysisData(key, value)));

        return results;
    }

    private TappingAnalysisData createTappingAnalysisData(String period, Map<String, List<Tapping>> data) {
        return new TappingAnalysisData(period, createTappingAnalysisDataParameter(data));
    }

    private TappingAnalysisDataParameter createTappingAnalysisDataParameter(Map<String, List<Tapping>> data) {
        List<Tapping> leftBefore = data.get(String.valueOf(ResultGroup.LEFT_BEFORE));
        List<Tapping> rightBefore = data.get(String.valueOf(ResultGroup.RIGHT_BEFORE));
        List<Tapping> leftAfter = data.get(String.valueOf(ResultGroup.LEFT_AFTER));
        List<Tapping> rightAfter = data.get(String.valueOf(ResultGroup.RIGHT_AFTER));

        return new TappingAnalysisDataParameter(
                getTouchTime(leftBefore, rightBefore, leftAfter, rightAfter),
                getUpTime(leftBefore, rightBefore, leftAfter, rightAfter),
                getIntertapInterval(leftBefore, rightBefore, leftAfter, rightAfter)
        );
    }

    private TappingAnalysisDataParameterDetails getIntertapInterval(List<Tapping> leftBefore, List<Tapping> rightBefore, List<Tapping> leftAfter, List<Tapping> rightAfter) {
        Map<String, List<Double>> invertapIntervalLeftBefore = calculateIntertapInterval(leftBefore);
        Map<String, List<Double>> invertapIntervalRightBefore = calculateIntertapInterval(rightBefore);
        Map<String, List<Double>> invertapIntervalLeftAfter = calculateIntertapInterval(leftAfter);
        Map<String, List<Double>> invertapIntervalRightAfter = calculateIntertapInterval(rightAfter);

        List<Double> dataBeforeMedLeft = invertapIntervalLeftBefore.get("all");
        List<Double> dataBeforeMedRight = invertapIntervalRightBefore.get("all");
        List<Double> dataAfterMedLeft = invertapIntervalLeftAfter.get("all");
        List<Double> dataAfterMedRight = invertapIntervalRightAfter.get("all");
        List<Double> dataBeforeMedLeftMeanByDays = invertapIntervalLeftBefore.get("meanDay");
        List<Double> dataBeforeMedRightMeanByDays = invertapIntervalRightBefore.get("meanDay");
        List<Double> dataAfterMedLeftMeanByDays = invertapIntervalLeftAfter.get("meanDay");
        List<Double> dataAfterMedRightMeanByDays = invertapIntervalRightAfter.get("meanDay");


        return new TappingAnalysisDataParameterDetails(dataBeforeMedLeft, dataBeforeMedRight, dataAfterMedLeft,
                dataAfterMedRight, dataBeforeMedLeftMeanByDays, dataBeforeMedRightMeanByDays, dataAfterMedLeftMeanByDays,
                dataAfterMedRightMeanByDays);
    }

    private TappingAnalysisDataParameterDetails getUpTime(List<Tapping> leftBefore, List<Tapping> rightBefore, List<Tapping> leftAfter, List<Tapping> rightAfter) {
        Map<String, List<Double>> upTimeLeftBefore = calculateUpTime(leftBefore);
        Map<String, List<Double>> upTimeRightBefore = calculateUpTime(rightBefore);
        Map<String, List<Double>> upTimeLeftAfter = calculateUpTime(leftAfter);
        Map<String, List<Double>> upTimeRightAfter = calculateUpTime(rightAfter);

        List<Double> dataBeforeMedLeft = upTimeLeftBefore.get("all");
        List<Double> dataBeforeMedRight = upTimeRightBefore.get("all");
        List<Double> dataAfterMedLeft = upTimeLeftAfter.get("all");
        List<Double> dataAfterMedRight = upTimeRightAfter.get("all");
        List<Double> dataBeforeMedLeftMeanByDays = upTimeLeftBefore.get("meanDay");
        List<Double> dataBeforeMedRightMeanByDays = upTimeRightBefore.get("meanDay");
        List<Double> dataAfterMedLeftMeanByDays = upTimeLeftAfter.get("meanDay");
        List<Double> dataAfterMedRightMeanByDays = upTimeRightAfter.get("meanDay");


        return new TappingAnalysisDataParameterDetails(dataBeforeMedLeft, dataBeforeMedRight, dataAfterMedLeft,
                dataAfterMedRight, dataBeforeMedLeftMeanByDays, dataBeforeMedRightMeanByDays, dataAfterMedLeftMeanByDays,
                dataAfterMedRightMeanByDays);
    }

    private TappingAnalysisDataParameterDetails getTouchTime(List<Tapping> leftBefore, List<Tapping> rightBefore, List<Tapping> leftAfter, List<Tapping> rightAfter) {
        Map<String, List<Double>> holdTimeLeftBefore = calculateHoldTime(leftBefore);
        Map<String, List<Double>> holdTimeRightBefore = calculateHoldTime(rightBefore);
        Map<String, List<Double>> holdTimeLeftAfter = calculateHoldTime(leftAfter);
        Map<String, List<Double>> holdTimeRightAfter = calculateHoldTime(rightAfter);

        List<Double> dataBeforeMedLeft = holdTimeLeftBefore.get("all");
        List<Double> dataBeforeMedRight = holdTimeRightBefore.get("all");
        List<Double> dataAfterMedLeft = holdTimeLeftAfter.get("all");
        List<Double> dataAfterMedRight = holdTimeRightAfter.get("all");
        List<Double> dataBeforeMedLeftMeanByDays = holdTimeLeftBefore.get("meanDay");
        List<Double> dataBeforeMedRightMeanByDays = holdTimeRightBefore.get("meanDay");
        List<Double> dataAfterMedLeftMeanByDays = holdTimeLeftAfter.get("meanDay");
        List<Double> dataAfterMedRightMeanByDays = holdTimeRightAfter.get("meanDay");


        return new TappingAnalysisDataParameterDetails(dataBeforeMedLeft, dataBeforeMedRight, dataAfterMedLeft,
                dataAfterMedRight, dataBeforeMedLeftMeanByDays, dataBeforeMedRightMeanByDays, dataAfterMedLeftMeanByDays,
                dataAfterMedRightMeanByDays);
    }

    private Map<String, List<Double>> calculateHoldTime(List<Tapping> tappingData) {
        Map<String, List<Double>> results = new HashMap<>();
        List<Double> holdTimeAll = new ArrayList<>();
        List<Double> holdTimeMeanDay = new ArrayList<>();

        tappingData.forEach(value -> {
            List<String> time = value.getTimestamp();
            List<String> upDown = value.getUpDown();
            List<String> side = value.getClickSide();
            List<Double> ht = new ArrayList<>();
            for (int i = 2; i < time.size()-1; i++){
                if (upDown.get(i).equals("DOWN")) {
                    for (int j=i; j < time.size()-1; j++) {
                        if (upDown.get(j).equals("UP") && side.get(i).equals(side.get(j))) {
                            double x = Double.valueOf(time.get(j));
                            double y = Double.valueOf(time.get(i));
                            double temp = (x-y)*1000;
                            holdTimeAll.add(temp);
                            ht.add(temp);
                            break;
                        }
                    }
                }
            }
            holdTimeMeanDay.add(ht.stream().mapToDouble(Double::doubleValue).average().orElse(0.0));
        });

        results.put("all", holdTimeAll);
        results.put("meanDay", holdTimeMeanDay);
        return results;
    }

    private Map<String, List<Double>> calculateUpTime(List<Tapping> tappingData) {
        Map<String, List<Double>> results = new HashMap<>();
        List<Double> upTimeAll = new ArrayList<>();
        List<Double> upTimeMeanDay = new ArrayList<>();

        tappingData.forEach(value -> {
            List<String> time = value.getTimestamp();
            List<String> upDown = value.getUpDown();
            List<String> side = value.getClickSide();
            List<Double> tt = new ArrayList<>();
            for (int i = 2; i < time.size()-1; i++){
                if (upDown.get(i).equals("UP")) {
                    for (int j=i; j < time.size()-1; j++) {
                        if (upDown.get(j).equals("DOWN")) {
                            double temp = (Double.valueOf((time.get(j))) - Double.valueOf(time.get(i)))*1000;
                            upTimeAll.add(temp);
                            tt.add(temp);
                            break;
                        }
                    }
                }
            }
            upTimeMeanDay.add(tt.stream().mapToDouble(Double::doubleValue).average().orElse(0.0));
        });

        results.put("all", upTimeAll);
        results.put("meanDay", upTimeMeanDay);
        return results;
    }

    private Map<String, List<Double>> calculateIntertapInterval(List<Tapping> tappingData) {
        Map<String, List<Double>> results = new HashMap<>();
        List<Double> intertapIntervalAll = new ArrayList<>();
        List<Double> intertapIntervalMeanDay = new ArrayList<>();

        tappingData.forEach(value -> {
            List<String> time = value.getTimestamp();
            List<String> upDown = value.getUpDown();
            List<String> side = value.getClickSide();
            List<Double> iti = new ArrayList<>();
            for (int i = 2; i < time.size(); i++){
                if (upDown.get(i).equals("DOWN")) {
                    for (int j = i; j < time.size(); j++) {
                        if (upDown.get(j).equals("UP") && side.get(i).equals(side.get(j))) {
                            for (int k = j; k < time.size(); k++) {
                                if (upDown.get(k).equals("DOWN")) {
                                    double x = Double.valueOf(time.get(k));
                                    double y = Double.valueOf(time.get(i));
                                    double temp = (x-y)*1000;
                                    intertapIntervalAll.add(temp);
                                    iti.add(temp);
                                    break;
                                }
                            }
                            break;
                        }
                    }
                }
            }
            intertapIntervalMeanDay.add(iti.stream().mapToDouble(Double::doubleValue).average().orElse(0.0));
        });

        results.put("all", intertapIntervalAll);
        results.put("meanDay", intertapIntervalMeanDay);
        return results;
    }
}
