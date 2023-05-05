package com.example.PDTestServer.testowaAnalizaTestow;

import com.example.PDTestServer.controller.results.request.ResultRequestDTO;
import com.example.PDTestServer.controller.results.response.ResultsDTO;
import com.example.PDTestServer.model.results.Accel;
import com.example.PDTestServer.model.results.DateRangeTest;
import com.example.PDTestServer.model.results.SideResults;
import com.example.PDTestServer.model.results.Tapping;
import com.example.PDTestServer.utils.enums.TestName;
import com.example.PDTestServer.utils.firebase.FieldName;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.example.PDTestServer.utils.coverter.DateConverter.converterResultsRequestToDataRange;

@Service
public class Servis {
    private final static List<String> TAPPING_KEYS = Arrays.asList("timestamp", "upDown", "x", "y", "clickSide");
    private final static List<String> ACCEL_KEYS = Arrays.asList("timestamp", "x", "y", "z");

    public String readDataFromFile() {
        File malkarLeftFolder = new File("C:\\Users\\nikol\\Desktop\\Magisterka\\program\\testowa-aplikacja\\dane\\malkar_FINGER_TAPPING\\Left");
        File malkarRightFolder = new File("C:\\Users\\nikol\\Desktop\\Magisterka\\program\\testowa-aplikacja\\dane\\malkar_FINGER_TAPPING\\Right");
        File marlatLeftFolder = new File("C:\\Users\\nikol\\Desktop\\Magisterka\\program\\testowa-aplikacja\\dane\\marlat_FINGER_TAPPING\\Left");
        File marlatRightFolder = new File("C:\\Users\\nikol\\Desktop\\Magisterka\\program\\testowa-aplikacja\\dane\\marlat_FINGER_TAPPING\\Right");
        File[] malkarLeftFiles = malkarLeftFolder.listFiles();
        File[] malkarRightFiles = malkarRightFolder.listFiles();
        File[] marlatLeftFiles = marlatLeftFolder.listFiles();
        File[] marlatRightFiles = marlatRightFolder.listFiles();

        Map<String, Tapping> malkarLeftResults = getData(malkarLeftFiles);
        Map<String, Tapping> malkarRightResults = getData(malkarRightFiles);
        Map<String, Tapping> marlatLeftResults = getData(marlatLeftFiles);
        Map<String, Tapping> marlatRightResults = getData(marlatRightFiles);

        return "OK";
    }

    private Map<String, Tapping> getData(File[] files) {
        Map<String, Tapping> results = new HashMap<>();
        for (File file : files) {
            if (file.isFile()) {
                try {
                    String content = new String(Files.readAllBytes(file.toPath()));
                    String[] split = content.split("\n");
                    ArrayList<String> rows = new ArrayList<>(Arrays.asList(split));
                    results.put(file.getName(), getResultData(rows));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return results;
    }

    private Tapping getResultData(ArrayList<String> rows) {
        return createListTappingData(rows);
    }

    private com.example.PDTestServer.model.results.Tapping createListTappingData(ArrayList<String> tappingData) {
        return createTappingData(tappingData);
    }

    private com.example.PDTestServer.model.results.Tapping createTappingData(ArrayList<String> data) {
        data.remove(0);
        Map<String, ArrayList<String>> result = getResultMap(data, TAPPING_KEYS);

        return new Tapping(result.get("timestamp"), result.get("upDown"), result.get("x"), result.get("y"), result.get("clickSide"));
    }

    private Map<String, ArrayList<String>> getResultMap(ArrayList<String> data, List<String> keys) {
        return  IntStream.range(0, keys.size())
                .boxed()
                .collect(Collectors.toMap(keys::get, i -> (ArrayList<String>) data.stream()
                        .map(s -> s.split(","))
                        .map(arr -> arr[i])
                        .collect(Collectors.toList())));
    }
}
