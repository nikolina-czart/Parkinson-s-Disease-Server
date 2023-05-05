package com.example.PDTestServer.repository.tests.analysis;

import com.example.PDTestServer.model.results.Accel;
import com.example.PDTestServer.model.results.SideResults;
import com.example.PDTestServer.utils.enums.PeriodName;
import com.example.PDTestServer.utils.enums.ResultGroup;
import com.example.PDTestServer.utils.enums.Side;
import com.example.PDTestServer.utils.enums.TestName;
import com.example.PDTestServer.utils.firebase.FieldName;
import com.google.cloud.firestore.DocumentSnapshot;
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
public class AnalysisTremorRepository {
    private final static List<String> ACCEL_KEYS = Arrays.asList("timestamp","x", "y", "z");
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Map<String, Map<String, List<Accel>>> getDataTremor(String userUid, String period) throws ExecutionException, InterruptedException {
        Map<String, Map<String, List<Accel>>> results = new HashMap<>();

        List<QueryDocumentSnapshot> queryDocumentSnapshots = testDatesColRef(userUid, TestName.TREMORS).get().get().getDocuments();

        List<SideResults> sideResults = new ArrayList<>();
        queryDocumentSnapshots.forEach(result -> {
            sideResults.add(getSideResults(userUid, result, Side.LEFT, TestName.TREMORS));
            sideResults.add(getSideResults(userUid, result, Side.RIGHT, TestName.TREMORS));
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

    private Map<String, Map<String, List<Accel>>> groupAllData(List<SideResults> value) {
        Map<String, Map<String, List<Accel>>> result = new HashMap<>();

        Map<String, List<Accel>> listMap = new HashMap<>();
        List<Accel> beforeLeftTapping = value.stream()
                .filter(sr -> sr.getSide().equals(Side.LEFT.name()) && sr.getMedicineSupply().equals("0"))
                .map(this::covertToAccel)
                .toList();
        List<Accel> beforeRightTapping = value.stream()
                .filter(sr -> sr.getSide().equals(Side.RIGHT.name()) && sr.getMedicineSupply().equals("0"))
                .map(this::covertToAccel)
                .toList();
        List<Accel> afterLeftTapping = value.stream()
                .filter(sr -> sr.getSide().equals(Side.LEFT.name()) && !sr.getMedicineSupply().equals("0"))
                .map(this::covertToAccel)
                .toList();
        List<Accel> afterRightTapping = value.stream()
                .filter(sr -> sr.getSide().equals(Side.RIGHT.name()) && !sr.getMedicineSupply().equals("0"))
                .map(this::covertToAccel)
                .toList();
        listMap.put(ResultGroup.LEFT_BEFORE.name(), beforeLeftTapping);
        listMap.put(ResultGroup.RIGHT_BEFORE.name(), beforeRightTapping);
        listMap.put(ResultGroup.LEFT_AFTER.name(), afterLeftTapping);
        listMap.put(ResultGroup.RIGHT_AFTER.name(), afterRightTapping);
        result.put("Wszystkie dane", listMap);

        return result;
    }

    private Map<String, Map<String, List<Accel>>> groupDataByQuarter(List<SideResults> sideResults) {
        Map<String, Map<String, List<Accel>>> result = new HashMap<>();

        Map<String, List<SideResults>> resultsByQuarter = sideResults.stream()
                .collect(Collectors.groupingBy(s -> {
                    LocalDate date = LocalDate.parse(s.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    int quarter = (date.getMonthValue() - 1) / 3 + 1;
                    String quarterName = "";
                    int year = date.getYear();
                    switch (quarter) {
                        case 1:
                            quarterName = String.format("Styczeń-Marzec %d", year);
                            break;
                        case 2:
                            quarterName = String.format("Kwiecień-Czerwiec %d", year);
                            break;
                        case 3:
                            quarterName = String.format("Lipiec-Wrzesień %d", year);
                            break;
                        case 4:
                            quarterName = String.format("Październik-Grudzień %d", year);
                            break;
                    }
                    return String.format("%s %s", quarterName, date.getYear());
                }));

        resultsByQuarter.forEach((key, value) -> {
            Map<String, List<Accel>> listMap = new HashMap<>();
            List<Accel> beforeLeftTapping = value.stream()
                    .filter(sr -> sr.getSide().equals(Side.LEFT.name()) && sr.getMedicineSupply().equals("0"))
                    .map(this::covertToAccel)
                    .toList();
            List<Accel> beforeRightTapping = value.stream()
                    .filter(sr -> sr.getSide().equals(Side.RIGHT.name()) && sr.getMedicineSupply().equals("0"))
                    .map(this::covertToAccel)
                    .toList();
            List<Accel> afterLeftTapping = value.stream()
                    .filter(sr -> sr.getSide().equals(Side.LEFT.name()) && !sr.getMedicineSupply().equals("0"))
                    .map(this::covertToAccel)
                    .toList();
            List<Accel> afterRightTapping = value.stream()
                    .filter(sr -> sr.getSide().equals(Side.RIGHT.name()) && !sr.getMedicineSupply().equals("0"))
                    .map(this::covertToAccel)
                    .toList();
            listMap.put(ResultGroup.LEFT_BEFORE.name(), beforeLeftTapping);
            listMap.put(ResultGroup.RIGHT_BEFORE.name(), beforeRightTapping);
            listMap.put(ResultGroup.LEFT_AFTER.name(), afterLeftTapping);
            listMap.put(ResultGroup.RIGHT_AFTER.name(), afterRightTapping);
            result.put(key, listMap);
        });

        return result;
    }

    private Map<String, Map<String, List<Accel>>> groupDataBYHalf(List<SideResults> sideResults) {
        Map<String, Map<String, List<Accel>>> result = new HashMap<>();

        Map<String, List<SideResults>> groupByHalfYearWithMonthNames = sideResults.stream()
                .collect(Collectors.groupingBy(sr -> {
                    LocalDateTime date = LocalDateTime.parse(sr.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    int month = date.getMonthValue();
                    int year = date.getYear();
                    String halfYear = (month <= 6) ? "Styczeń-Czerwiec" : "Lipiec-Grudzień";
                    return String.format("%s %d", halfYear, year);
                }));

        groupByHalfYearWithMonthNames.forEach((key, value) -> {
            Map<String, List<Accel>> listMap = new HashMap<>();
            List<Accel> beforeLeftTapping = value.stream()
                    .filter(sr -> sr.getSide().equals(Side.LEFT.name()) && sr.getMedicineSupply().equals("0"))
                    .map(this::covertToAccel)
                    .toList();
            List<Accel> beforeRightTapping = value.stream()
                    .filter(sr -> sr.getSide().equals(Side.RIGHT.name()) && sr.getMedicineSupply().equals("0"))
                    .map(this::covertToAccel)
                    .toList();
            List<Accel> afterLeftTapping = value.stream()
                    .filter(sr -> sr.getSide().equals(Side.LEFT.name()) && !sr.getMedicineSupply().equals("0"))
                    .map(this::covertToAccel)
                    .toList();
            List<Accel> afterRightTapping = value.stream()
                    .filter(sr -> sr.getSide().equals(Side.RIGHT.name()) && !sr.getMedicineSupply().equals("0"))
                    .map(this::covertToAccel)
                    .toList();
            listMap.put(ResultGroup.LEFT_BEFORE.name(), beforeLeftTapping);
            listMap.put(ResultGroup.RIGHT_BEFORE.name(), beforeRightTapping);
            listMap.put(ResultGroup.LEFT_AFTER.name(), afterLeftTapping);
            listMap.put(ResultGroup.RIGHT_AFTER.name(), afterRightTapping);
            result.put(key, listMap);
        });

        return result;
    }

    private Map<String, Map<String, List<Accel>>> groupDataByMonth(List<SideResults> sideResults) {
        Map<String, Map<String, List<Accel>>> result = new HashMap<>();

        Map<String, List<SideResults>> groupByMonth = sideResults.stream()
                .collect(Collectors.groupingBy(sr -> {
                    LocalDateTime date = LocalDateTime.parse(sr.getDate(), formatter);
                    String month = date.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, new Locale("pl"));
                    return String.format("%s %d", month, date.getYear());
                }));

        groupByMonth.forEach((key, value) -> {
            Map<String, List<Accel>> listMap = new HashMap<>();
            List<Accel> beforeLeftTapping = value.stream()
                    .filter(sr -> sr.getSide().equals(Side.LEFT.name()) && sr.getMedicineSupply().equals("0"))
                    .map(this::covertToAccel)
                    .toList();
            List<Accel> beforeRightTapping = value.stream()
                    .filter(sr -> sr.getSide().equals(Side.RIGHT.name()) && sr.getMedicineSupply().equals("0"))
                    .map(this::covertToAccel)
                    .toList();
            List<Accel> afterLeftTapping = value.stream()
                    .filter(sr -> sr.getSide().equals(Side.LEFT.name()) && !sr.getMedicineSupply().equals("0"))
                    .map(this::covertToAccel)
                    .toList();
            List<Accel> afterRightTapping = value.stream()
                    .filter(sr -> sr.getSide().equals(Side.RIGHT.name()) && !sr.getMedicineSupply().equals("0"))
                    .map(this::covertToAccel)
                    .toList();
            listMap.put(ResultGroup.LEFT_BEFORE.name(), beforeLeftTapping);
            listMap.put(ResultGroup.RIGHT_BEFORE.name(), beforeRightTapping);
            listMap.put(ResultGroup.LEFT_AFTER.name(), afterLeftTapping);
            listMap.put(ResultGroup.RIGHT_AFTER.name(), afterRightTapping);
            result.put(key, listMap);
        });

        return result;
    }

    private SideResults getSideResults(String userUid, QueryDocumentSnapshot result, Side side, TestName testName) {
        String hoursSinceLastMed = String.valueOf(result.getData().get(FieldName.HOURS_SINCE_LAST_MED.name));
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
            DocumentSnapshot documentSnapshot = testSideDocRef(userUid, testUid, side, testName).get().get();
            return documentSnapshot.getData();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private Accel covertToAccel(SideResults results) {
        return createAccelData((ArrayList<String>) results.getData().get(FieldName.ACCEL.name));
    }

    private Accel createAccelData(ArrayList<String> data) {
        data.remove(0);
        Map<String, ArrayList<String>> result = getResultMap(data, ACCEL_KEYS);

        return new Accel(result.get("timestamp"), result.get("x"), result.get("y"), result.get("x"));
    }

    private Map<String, ArrayList<String>> getResultMap(ArrayList<String> data, List<String> keys) {
        return IntStream.range(0, keys.size())
                .boxed()
                .collect(Collectors.toMap(keys::get, i -> (ArrayList<String>) data.stream()
                        .map(s -> s.split(","))
                        .map(arr -> arr[i])
                        .collect(Collectors.toList())));
    }

    public Map<String, Map<String, List<Accel>>> getFakeDataTremor(String userUid, String period) {
        Map<String, Map<String, List<Accel>>> result = new HashMap<>();
        Map<String, List<Accel>> map = readDataFromFile();
        result.put("Wszystkie dane", map);
        return result;
    }

    private Map<String, List<Accel>> readDataFromFile() {
        File leftBefore = new File("C:\\Users\\nikol\\Desktop\\Magisterka\\program\\python_finger_tapping\\dane\\zofmar\\TREMOR\\LEFT_BEFORE");
        File rightBefore = new File("C:\\Users\\nikol\\Desktop\\Magisterka\\program\\python_finger_tapping\\dane\\zofmar\\TREMOR\\RIGHT_BEFORE");
        File leftAfter = new File("C:\\Users\\nikol\\Desktop\\Magisterka\\program\\python_finger_tapping\\dane\\zofmar\\TREMOR\\LEFT_AFTER");
        File rightAfter = new File("C:\\Users\\nikol\\Desktop\\Magisterka\\program\\python_finger_tapping\\dane\\zofmar\\TREMOR\\RIGHT_AFTER");
        File[] leftBeforeFiles = leftBefore.listFiles();
        File[] rightBeforeFiles = rightBefore.listFiles();
        File[] leftAfterFiles = leftAfter.listFiles();
        File[] rightAfterFiles = rightAfter.listFiles();

        List<Accel> leftBeforeResult = getData(leftBeforeFiles);
        List<Accel> rightBeforeResult = getData(rightBeforeFiles);
        List<Accel> leftAfterResult = getData(leftAfterFiles);
        List<Accel> rightAfterResult = getData(rightAfterFiles);


        Map<String, List<Accel>> results = new HashMap<>();
        results.put(ResultGroup.LEFT_BEFORE.name(), leftBeforeResult);
        results.put(ResultGroup.RIGHT_BEFORE.name(), rightBeforeResult);
        results.put(ResultGroup.LEFT_AFTER.name(), leftAfterResult);
        results.put(ResultGroup.RIGHT_AFTER.name(), rightAfterResult);

        return results;
    }

    private List<Accel> getData(File[] files) {
        List<Accel> results = new ArrayList<>();
        for (File file : files) {
            if (file.isFile()) {
                try {
                    String content = new String(Files.readAllBytes(file.toPath()));
                    String[] split = content.split("\n");
                    ArrayList<String> rows = new ArrayList<>(Arrays.asList(split));
                    results.add(createAccelDataX(rows));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return results;
    }

    private Accel createAccelDataX(ArrayList<String> data) {
        data.remove(0);
        Map<String, ArrayList<String>> result = getResultMap(data, ACCEL_KEYS);

        return new Accel(result.get("timestamp"), result.get("x"), result.get("y"), result.get("z"));
    }


}
