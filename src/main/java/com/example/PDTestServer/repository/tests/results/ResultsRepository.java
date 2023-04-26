package com.example.PDTestServer.repository.tests.results;

import com.example.PDTestServer.model.DateRangeTest;
import com.example.PDTestServer.model.results.SideResults;
import com.example.PDTestServer.utils.enums.Side;
import com.example.PDTestServer.utils.enums.TestName;
import com.example.PDTestServer.utils.firebase.FieldName;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.example.PDTestServer.utils.coverter.DateConverter.convertStringToTimestampEndDay;
import static com.example.PDTestServer.utils.coverter.DateConverter.convertStringToTimestampStartDay;
import static com.example.PDTestServer.utils.firebase.FirebaseQuery.resultsInRangeTime;
import static com.example.PDTestServer.utils.firebase.FirebaseReference.*;

@Repository
public class ResultsRepository {

    public List<SideResults> getResultData(String uid, DateRangeTest date, TestName testName) throws ExecutionException, InterruptedException {
        List<SideResults> results = new ArrayList<>();
        Timestamp startDate = convertStringToTimestampStartDay(date.getFormDate());
        Timestamp endDate = convertStringToTimestampEndDay(date.getToDate());

        List<QueryDocumentSnapshot> filteredResults = resultsInRangeTime(uid, startDate, endDate, testName).get().get().getDocuments();

        filteredResults.forEach(result -> {
            results.add(getSideResults(uid, result, Side.LEFT, testName));
            results.add(getSideResults(uid, result, Side.RIGHT, testName));
        });

        return results;
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
            return testSideDocRef(userUid, testUid, side, testName).get().get().getData();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
