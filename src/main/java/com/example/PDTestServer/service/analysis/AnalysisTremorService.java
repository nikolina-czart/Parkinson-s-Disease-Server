package com.example.PDTestServer.service.analysis;

import com.example.PDTestServer.controller.analysis.request.AnalysisRequestDTO;
import com.example.PDTestServer.model.analysis.fingerTapping.TappingAnalysisData;
import com.example.PDTestServer.model.analysis.fingerTapping.TappingAnalysisDataParameter;
import com.example.PDTestServer.model.analysis.gyroscope.TremorAnalysisData;
import com.example.PDTestServer.model.analysis.gyroscope.TremorAnalysisDataParameter;
import com.example.PDTestServer.model.analysis.gyroscope.TremorAnalysisDataParameterDetails;
import com.example.PDTestServer.model.results.Accel;
import com.example.PDTestServer.model.results.Tapping;
import com.example.PDTestServer.repository.tests.analysis.AnalysisTremorRepository;
import com.example.PDTestServer.utils.enums.ResultGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class AnalysisTremorService {

    @Autowired
    AnalysisTremorRepository analysisRepository;
    public List<TremorAnalysisData> getChartDate(String userUid, AnalysisRequestDTO analysisRequest) throws ExecutionException, InterruptedException {
        List<TremorAnalysisData> results = new ArrayList<>();
        Map<String, Map<String, List<Accel>>> dataTremor = analysisRepository.getDataTremor(userUid, analysisRequest.getPeriod());
//        Map<String, Map<String, List<Accel>>> dataTremor = analysisRepository.getFakeDataTremor(userUid, analysisRequest.getPeriod());

        dataTremor.forEach((key, value) -> {
            results.add(createTremornalysisData(key, value));
        });

        return results;
    }

    private TremorAnalysisData createTremornalysisData(String period, Map<String, List<Accel>> data) {
        return new TremorAnalysisData(period, createTremorAnalysisDataParameter(data));
    }

    private TremorAnalysisDataParameter createTremorAnalysisDataParameter(Map<String, List<Accel>> data) {
        List<Accel> leftBefore = filteredData(data.get(String.valueOf(ResultGroup.LEFT_BEFORE)));
        List<Accel> rightBefore = filteredData(data.get(String.valueOf(ResultGroup.RIGHT_BEFORE)));
        List<Accel> leftAfter = filteredData(data.get(String.valueOf(ResultGroup.LEFT_AFTER)));
        List<Accel> rightAfter =filteredData( data.get(String.valueOf(ResultGroup.RIGHT_AFTER)));

        List<Double> leftBeforeMeanX = new ArrayList<>();
        List<Double> leftBeforeMeanY = new ArrayList<>();
        List<Double> leftBeforeMeanZ = new ArrayList<>();
        List<Double> rightBeforeMeanX = new ArrayList<>();
        List<Double> rightBeforeMeanY = new ArrayList<>();
        List<Double> rightBeforeMeanZ = new ArrayList<>();
        List<Double> leftAfterMeanX = new ArrayList<>();
        List<Double> leftAfterMeanY = new ArrayList<>();
        List<Double> leftAfterMeanZ = new ArrayList<>();
        List<Double> rightAfterMeanX = new ArrayList<>();
        List<Double> rightAfterMeanY = new ArrayList<>();
        List<Double> rightAfterMeanZ = new ArrayList<>();

        List<Double> leftBeforeAggregatedMeanByDay = new ArrayList<>();
        List<Double> rightBeforeAggregatedMeanByDay = new ArrayList<>();
        List<Double> leftAfterAggregatedMeanByDay = new ArrayList<>();
        List<Double> rightAfterAggregatedMeanByDay = new ArrayList<>();

        List<Double> leftBeforeDifferenceMeanByDayX = new ArrayList<>();
        List<Double> leftBeforeDifferenceMeanByDayY = new ArrayList<>();
        List<Double> leftBeforeDifferenceMeanByDayZ = new ArrayList<>();
        List<Double> rightBeforeDifferenceMeanByDayX = new ArrayList<>();
        List<Double> rightBeforeDifferenceMeanByDayY = new ArrayList<>();
        List<Double> rightBeforeDifferenceMeanByDayZ = new ArrayList<>();
        List<Double> leftAfterDifferenceMeanByDayX = new ArrayList<>();
        List<Double> leftAfterDifferenceMeanByDayY = new ArrayList<>();
        List<Double> leftAfterDifferenceMeanByDayZ = new ArrayList<>();
        List<Double> rightAfterDifferenceMeanByDayX = new ArrayList<>();
        List<Double> rightAfterDifferenceMeanByDayY = new ArrayList<>();
        List<Double> rightAfterDifferenceMeanByDayZ = new ArrayList<>();

        leftBefore.forEach(element -> {
            double meanX = mean(element.getX());
            double meanY = mean(element.getY());
            double meanZ = mean(element.getZ());
            leftBeforeMeanX.add(meanX);
            leftBeforeMeanY.add(meanY);
            leftBeforeMeanZ.add(meanZ);
            leftBeforeAggregatedMeanByDay.add(aggregatedMeanByDay(element.getX(), element.getY(), element.getZ()));
            leftBeforeDifferenceMeanByDayX.add(calculateDifferenceMeanByDay(element.getX(), meanX));
            leftBeforeDifferenceMeanByDayY.add(calculateDifferenceMeanByDay(element.getY(), meanX));
            leftBeforeDifferenceMeanByDayZ.add(calculateDifferenceMeanByDay(element.getZ(), meanX));
        });
        rightBefore.forEach(element -> {
            double meanX = mean(element.getX());
            double meanY = mean(element.getY());
            double meanZ = mean(element.getZ());
            rightBeforeMeanX.add(meanX);
            rightBeforeMeanY.add(meanY);
            rightBeforeMeanZ.add(meanZ);
            rightBeforeAggregatedMeanByDay.add(aggregatedMeanByDay(element.getX(), element.getY(), element.getZ()));
            rightBeforeDifferenceMeanByDayX.add(calculateDifferenceMeanByDay(element.getX(), meanX));
            rightBeforeDifferenceMeanByDayY.add(calculateDifferenceMeanByDay(element.getY(), meanX));
            rightBeforeDifferenceMeanByDayZ.add(calculateDifferenceMeanByDay(element.getZ(), meanX));

        });
        leftAfter.forEach(element -> {
            double meanX = mean(element.getX());
            double meanY = mean(element.getY());
            double meanZ = mean(element.getZ());
            leftAfterMeanX.add(meanX);
            leftAfterMeanY.add(meanY);
            leftAfterMeanZ.add(meanZ);
            leftAfterAggregatedMeanByDay.add(aggregatedMeanByDay(element.getX(), element.getY(), element.getZ()));
            leftAfterDifferenceMeanByDayX.add(calculateDifferenceMeanByDay(element.getX(), meanX));
            leftAfterDifferenceMeanByDayY.add(calculateDifferenceMeanByDay(element.getY(), meanX));
            leftAfterDifferenceMeanByDayZ.add(calculateDifferenceMeanByDay(element.getZ(), meanX));
        });
        rightAfter.forEach(element -> {
            double meanX = mean(element.getX());
            double meanY = mean(element.getY());
            double meanZ = mean(element.getZ());
            rightAfterMeanX.add(meanX);
            rightAfterMeanY.add(meanY);
            rightAfterMeanZ.add(meanZ);
            rightAfterAggregatedMeanByDay.add(aggregatedMeanByDay(element.getX(), element.getY(), element.getZ()));
            rightAfterDifferenceMeanByDayX.add(calculateDifferenceMeanByDay(element.getX(), meanX));
            rightAfterDifferenceMeanByDayY.add(calculateDifferenceMeanByDay(element.getY(), meanX));
            rightAfterDifferenceMeanByDayZ.add(calculateDifferenceMeanByDay(element.getZ(), meanX));
        });

        return new TremorAnalysisDataParameter(
                new TremorAnalysisDataParameterDetails(leftBeforeMeanX, rightBeforeMeanX, leftAfterMeanX, rightAfterMeanX),
                new TremorAnalysisDataParameterDetails(leftBeforeMeanY, rightBeforeMeanY, leftAfterMeanY, rightAfterMeanY),
                new TremorAnalysisDataParameterDetails(leftBeforeMeanZ, rightBeforeMeanZ, leftAfterMeanZ, rightAfterMeanZ),
                new TremorAnalysisDataParameterDetails(leftBeforeAggregatedMeanByDay, rightBeforeAggregatedMeanByDay, leftAfterAggregatedMeanByDay, rightAfterAggregatedMeanByDay),
                new TremorAnalysisDataParameterDetails(leftBeforeDifferenceMeanByDayX, rightBeforeDifferenceMeanByDayX, leftAfterDifferenceMeanByDayX, rightAfterDifferenceMeanByDayX),
                new TremorAnalysisDataParameterDetails(leftBeforeDifferenceMeanByDayY, rightBeforeDifferenceMeanByDayY, leftAfterDifferenceMeanByDayY, rightAfterDifferenceMeanByDayY),
                new TremorAnalysisDataParameterDetails(leftBeforeDifferenceMeanByDayZ, rightBeforeDifferenceMeanByDayZ, leftAfterDifferenceMeanByDayZ, rightAfterDifferenceMeanByDayZ )
        );
    }

    private static List<Accel> filteredData(List<Accel> data) {
        List<Accel> filtered = new ArrayList<>();

        for (Accel accel : data) {
            List<String> timestamps = accel.getTimestamp();
            List<String> x = accel.getX();
            List<String> y = accel.getY();
            List<String> z = accel.getZ();

            List<String> timestampsNew = new ArrayList<>();
            List<String> xNew = new ArrayList<>();
            List<String> yNew = new ArrayList<>();
            List<String> zNew = new ArrayList<>();

            for (int i = 0; i < timestamps.size(); i++) {
                double value = Double.parseDouble(timestamps.get(i));

                if (value > 5 && value < 25) {
                    timestampsNew.add(timestamps.get(i));
                    xNew.add(x.get(i));
                    yNew.add(y.get(i));
                    zNew.add(z.get(i));
                }
            }

            filtered.add(new Accel(timestampsNew, xNew, yNew, zNew));
        }

        return filtered;
    }

    private double calculateDifferenceMeanByDay(List<String> data, double mean) {
        List<Double> result = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            double x = Double.parseDouble(data.get(i));
            double value = x - mean;
            double temp = value > 0 ? value : (-1)*value;
            result.add(temp);
        }
        return mean_2(result);
    }

    private static double mean(List<String> data) {
        double sum = 0;
        for (int i = 0; i < data.size(); i++) {
            sum += Double.valueOf(data.get(i));
        }
        return sum / data.size();
    }

    private static double mean_2(List<Double> data) {
        double sum = 0;
        for (int i = 0; i < data.size(); i++) {
            sum += data.get(i);
        }
        return sum / data.size();
    }


    private static double aggregatedMeanByDay(List<String> dataX, List<String> dataY, List<String> dataZ) {
        List<Double> result = new ArrayList<>();

        for (int i = 0; i < dataX.size(); i++) {
            double x = Double.parseDouble(dataX.get(i));
            double y = Double.parseDouble(dataY.get(i));
            double z = Double.parseDouble(dataZ.get(i));
            double value = Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2)+Math.pow(z, 2));
            result.add(value);
        }
        return mean_2(result);
    }
}
