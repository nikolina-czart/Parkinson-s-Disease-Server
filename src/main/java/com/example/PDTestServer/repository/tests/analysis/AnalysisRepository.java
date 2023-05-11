package com.example.PDTestServer.repository.tests.analysis;

import com.example.PDTestServer.model.results.Accel;
import com.example.PDTestServer.model.results.SideResults;
import com.example.PDTestServer.model.results.Tapping;
import com.example.PDTestServer.utils.enums.PeriodName;
import com.example.PDTestServer.utils.enums.ResultGroup;
import com.example.PDTestServer.utils.enums.Side;
import com.example.PDTestServer.utils.enums.TestName;
import com.example.PDTestServer.utils.firebase.FieldName;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.example.PDTestServer.utils.firebase.FirebaseReference.testDatesColRef;
import static com.example.PDTestServer.utils.firebase.FirebaseReference.testSideDocRef;

@Repository
public class AnalysisRepository {
    private final static List<String> TAPPING_KEYS = Arrays.asList("timestamp", "upDown", "x", "y", "clickSide");
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Map<String, Map<String, List<Tapping>>> getDataFingerTapping(String userUid, String period) throws ExecutionException, InterruptedException {
        Map<String, Map<String, List<Tapping>>> results = new HashMap<>();

        List<QueryDocumentSnapshot> queryDocumentSnapshots = testDatesColRef(userUid, TestName.FINGER_TAPPING).get().get().getDocuments();

        List<SideResults> sideResults = new ArrayList<>();
        queryDocumentSnapshots.forEach(result -> {
            sideResults.add(getSideResults(userUid, result, Side.LEFT, TestName.FINGER_TAPPING));
            sideResults.add(getSideResults(userUid, result, Side.RIGHT, TestName.FINGER_TAPPING));
        });
        sideResults.removeIf(sideResult -> sideResult.getMedicineSupply() == "null");
        sideResults.removeIf(sideResult -> sideResult.getData() == null);

        if (period.equals(PeriodName.MONTH.name)) {
            results = groupDataByMonth(sideResults);
        }
        if (period.equals(PeriodName.QUARTER.name)) {
            results = groupDataByQuarter(sideResults);
        }
        if (period.equals(PeriodName.HALF.name)) {
            results = groupDataBYHalf(sideResults);
        }
        if (period.equals(PeriodName.ALL.name)) {
            results = groupAllData(sideResults);
        }

        return results;
    }

    private Map<String, Map<String, List<Tapping>>> groupAllData(List<SideResults> value) {
        Map<String, Map<String, List<Tapping>>> result = new HashMap<>();

        Map<String, List<Tapping>> listMap = new HashMap<>();
        List<Tapping> beforeLeftTapping = value.stream()
                .filter(sr -> sr.getSide().equals(Side.LEFT.name()) && sr.getMedicineSupply().equals("0"))
                .map(this::covertToTapping)
                .toList();
        List<Tapping> beforeRightTapping = value.stream()
                .filter(sr -> sr.getSide().equals(Side.RIGHT.name()) && sr.getMedicineSupply().equals("0"))
                .map(this::covertToTapping)
                .toList();
        List<Tapping> afterLeftTapping = value.stream()
                .filter(sr -> sr.getSide().equals(Side.LEFT.name()) && !sr.getMedicineSupply().equals("0"))
                .map(this::covertToTapping)
                .toList();
        List<Tapping> afterRightTapping = value.stream()
                .filter(sr -> sr.getSide().equals(Side.RIGHT.name()) && !sr.getMedicineSupply().equals("0"))
                .map(this::covertToTapping)
                .toList();
        listMap.put(ResultGroup.LEFT_BEFORE.name(), beforeLeftTapping);
        listMap.put(ResultGroup.RIGHT_BEFORE.name(), beforeRightTapping);
        listMap.put(ResultGroup.LEFT_AFTER.name(), afterLeftTapping);
        listMap.put(ResultGroup.RIGHT_AFTER.name(), afterRightTapping);
        result.put("All measurements", listMap);

        return result;
    }

    private Map<String, Map<String, List<Tapping>>> groupDataByQuarter(List<SideResults> sideResults) {
        Map<String, Map<String, List<Tapping>>> result = new HashMap<>();

        Map<String, List<SideResults>> resultsByQuarter = sideResults.stream()
                .collect(Collectors.groupingBy(s -> {
                    LocalDate date = LocalDate.parse(s.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    int quarter = (date.getMonthValue() - 1) / 3 + 1;
                    String quarterName = "";
                    int year = date.getYear();
                    switch (quarter) {
                        case 1:
                            quarterName = String.format("January-March %d", year);
                            break;
                        case 2:
                            quarterName = String.format("April-June %d", year);
                            break;
                        case 3:
                            quarterName = String.format("July-September %d", year);
                            break;
                        case 4:
                            quarterName = String.format("October-December %d", year);
                            break;
                    }
                    return String.format("%s %s", quarterName, date.getYear());
                }));

        resultsByQuarter.forEach((key, value) -> {
            Map<String, List<Tapping>> listMap = new HashMap<>();
            List<Tapping> beforeLeftTapping = value.stream()
                    .filter(sr -> sr.getSide().equals(Side.LEFT.name()) && sr.getMedicineSupply().equals("0"))
                    .map(this::covertToTapping)
                    .toList();
            List<Tapping> beforeRightTapping = value.stream()
                    .filter(sr -> sr.getSide().equals(Side.RIGHT.name()) && sr.getMedicineSupply().equals("0"))
                    .map(this::covertToTapping)
                    .toList();
            List<Tapping> afterLeftTapping = value.stream()
                    .filter(sr -> sr.getSide().equals(Side.LEFT.name()) && !sr.getMedicineSupply().equals("0"))
                    .map(this::covertToTapping)
                    .toList();
            List<Tapping> afterRightTapping = value.stream()
                    .filter(sr -> sr.getSide().equals(Side.RIGHT.name()) && !sr.getMedicineSupply().equals("0"))
                    .map(this::covertToTapping)
                    .toList();
            listMap.put(ResultGroup.LEFT_BEFORE.name(), beforeLeftTapping);
            listMap.put(ResultGroup.RIGHT_BEFORE.name(), beforeRightTapping);
            listMap.put(ResultGroup.LEFT_AFTER.name(), afterLeftTapping);
            listMap.put(ResultGroup.RIGHT_AFTER.name(), afterRightTapping);
            result.put(key, listMap);
        });

        return result;
    }

    private Map<String, Map<String, List<Tapping>>> groupDataBYHalf(List<SideResults> sideResults) {
        Map<String, Map<String, List<Tapping>>> result = new HashMap<>();

        Map<String, List<SideResults>> groupByHalfYearWithMonthNames = sideResults.stream()
                .collect(Collectors.groupingBy(sr -> {
                    LocalDateTime date = LocalDateTime.parse(sr.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    int month = date.getMonthValue();
                    int year = date.getYear();
                    String halfYear = (month <= 6) ? "January-June" : "July-December";
                    return halfYear + " " + year;
                }));

        groupByHalfYearWithMonthNames.forEach((key, value) -> {
            Map<String, List<Tapping>> listMap = new HashMap<>();
            List<Tapping> beforeLeftTapping = value.stream()
                    .filter(sr -> sr.getSide().equals(Side.LEFT.name()) && sr.getMedicineSupply().equals("0"))
                    .map(this::covertToTapping)
                    .toList();
            List<Tapping> beforeRightTapping = value.stream()
                    .filter(sr -> sr.getSide().equals(Side.RIGHT.name()) && sr.getMedicineSupply().equals("0"))
                    .map(this::covertToTapping)
                    .toList();
            List<Tapping> afterLeftTapping = value.stream()
                    .filter(sr -> sr.getSide().equals(Side.LEFT.name()) && !sr.getMedicineSupply().equals("0"))
                    .map(this::covertToTapping)
                    .toList();
            List<Tapping> afterRightTapping = value.stream()
                    .filter(sr -> sr.getSide().equals(Side.RIGHT.name()) && !sr.getMedicineSupply().equals("0"))
                    .map(this::covertToTapping)
                    .toList();
            listMap.put(ResultGroup.LEFT_BEFORE.name(), beforeLeftTapping);
            listMap.put(ResultGroup.RIGHT_BEFORE.name(), beforeRightTapping);
            listMap.put(ResultGroup.LEFT_AFTER.name(), afterLeftTapping);
            listMap.put(ResultGroup.RIGHT_AFTER.name(), afterRightTapping);
            result.put(key, listMap);
        });

        return result;
    }

    private Map<String, Map<String, List<Tapping>>> groupDataByMonth(List<SideResults> sideResults) {
        Map<String, Map<String, List<Tapping>>> result = new HashMap<>();

        Map<String, List<SideResults>> groupByMonth = sideResults.stream()
                .collect(Collectors.groupingBy(sr -> {
                    LocalDateTime date = LocalDateTime.parse(sr.getDate(), formatter);
                    String month = date.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);;
                    return month.substring(0, 1).toUpperCase() + month.substring(1) + " " + date.getYear();
                }));

        groupByMonth.forEach((key, value) -> {
            Map<String, List<Tapping>> listMap = new HashMap<>();
            List<Tapping> beforeLeftTapping = value.stream()
                    .filter(sr -> sr.getSide().equals(Side.LEFT.name()) && sr.getMedicineSupply().equals("0"))
                    .map(this::covertToTapping)
                    .toList();
            List<Tapping> beforeRightTapping = value.stream()
                    .filter(sr -> sr.getSide().equals(Side.RIGHT.name()) && sr.getMedicineSupply().equals("0"))
                    .map(this::covertToTapping)
                    .toList();
            List<Tapping> afterLeftTapping = value.stream()
                    .filter(sr -> sr.getSide().equals(Side.LEFT.name()) && !sr.getMedicineSupply().equals("0"))
                    .map(this::covertToTapping)
                    .toList();
            List<Tapping> afterRightTapping = value.stream()
                    .filter(sr -> sr.getSide().equals(Side.RIGHT.name()) && !sr.getMedicineSupply().equals("0"))
                    .map(this::covertToTapping)
                    .toList();
            listMap.put(ResultGroup.LEFT_BEFORE.name(), beforeLeftTapping);
            listMap.put(ResultGroup.RIGHT_BEFORE.name(), beforeRightTapping);
            listMap.put(ResultGroup.LEFT_AFTER.name(), afterLeftTapping);
            listMap.put(ResultGroup.RIGHT_AFTER.name(), afterRightTapping);
            result.put(key, listMap);
        });

        return result;
    }

    private Tapping covertToTapping(SideResults results) {
        return createTappingData((ArrayList<String>) results.getData().get(FieldName.TAPPING_DATA.name));
    }


    private SideResults getSideResults(String userUid, QueryDocumentSnapshot result, Side side, TestName testName) {
        String hoursSinceLastMed = String.valueOf(result.getData().get(FieldName.HOURS_SINCE_LAST_MED.name));
        String createAt = String.valueOf(result.getData().get(FieldName.CREATE_AT.name));
        Map<String, Object> resultFingerTapping = getResult(userUid, result.getId(), side, testName);

        return SideResults.builder()
                .date(result.getId())
                .medicineSupply(hoursSinceLastMed)
                .side(String.valueOf(side))
                .data(resultFingerTapping)
                .build();
    }

    private Map<String, Object> getResult(String userUid, String testUid, Side side, TestName testName) {
        try {
            return testSideDocRef(userUid, testUid, side, testName).get().get().getData();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, Map<String, Tapping>> readDataFromFile() {
        File malkarLeftFolder = new File("C:\\Users\\nikol\\Desktop\\Magisterka\\program\\python_finger_tapping\\dane\\malkar_FINGER_TAPPING\\Left");
        File malkarRightFolder = new File("C:\\Users\\nikol\\Desktop\\Magisterka\\program\\python_finger_tapping\\dane\\malkar_FINGER_TAPPING\\Right");
        File marlatLeftFolder = new File("C:\\Users\\nikol\\Desktop\\Magisterka\\program\\testowa-aplikacja\\dane\\marlat_FINGER_TAPPING\\Left");
        File marlatRightFolder = new File("C:\\Users\\nikol\\Desktop\\Magisterka\\program\\testowa-aplikacja\\dane\\marlat_FINGER_TAPPING\\Right");
        File[] malkarLeftFiles = malkarLeftFolder.listFiles();
        File[] malkarRightFiles = malkarRightFolder.listFiles();
        File[] marlatLeftFiles = marlatLeftFolder.listFiles();
        File[] marlatRightFiles = marlatRightFolder.listFiles();

        Map<String, Tapping> malkarLeftResults = getData(malkarLeftFiles);
        Map<String, Tapping> malkarRightResults = getData(malkarRightFiles);
//        Map<String, Tapping> marlatLeftResults = getData(marlatLeftFiles);
//        Map<String, Tapping> marlatRightResults = getData(marlatRightFiles);
        Map<String, Map<String, Tapping>> results = new HashMap<>();
        results.put(String.valueOf(Side.LEFT), malkarLeftResults);
        results.put(String.valueOf(Side.RIGHT), malkarRightResults);
        return results;
    }

    private Map<String, Tapping> getData(File[] files) {
        Map<String, Tapping> results = new HashMap<>();
        for (File file : files) {
            if (file.isFile()) {
                try {
                    String content = new String(Files.readAllBytes(file.toPath()));
                    String[] split = content.split("\n");
                    ArrayList<String> rows = new ArrayList<>(Arrays.asList(split));
                    results.put(file.getName(), createTappingData(rows));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return results;
    }

    private Tapping createTappingData(ArrayList<String> data) {
        data.remove(0);
        Map<String, ArrayList<String>> result = getResultMap(data, TAPPING_KEYS);

        return new Tapping(result.get("timestamp"), result.get("upDown"), result.get("x"), result.get("y"), result.get("clickSide"));
    }

    private Map<String, ArrayList<String>> getResultMap(ArrayList<String> data, List<String> keys) {
        return IntStream.range(0, keys.size())
                .boxed()
                .collect(Collectors.toMap(keys::get, i -> (ArrayList<String>) data.stream()
                        .map(s -> s.split(","))
                        .map(arr -> arr[i])
                        .collect(Collectors.toList())));
    }
}
