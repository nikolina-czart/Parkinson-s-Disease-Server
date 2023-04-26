package com.example.PDTestServer.utils.coverter;

import com.example.PDTestServer.controller.results.request.ResultRequestDTO;
import com.example.PDTestServer.model.DateRangeTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.sql.Timestamp;

public class DateConverter {

    public static DateRangeTest converterResultsRequestToDataRange (ResultRequestDTO resultRequest) {
        return new DateRangeTest(resultRequest.getFormDate(), resultRequest.getToDate());
    }

    public static Timestamp convertStringToTimestampStartDay(String date) {
        LocalDate localDate = LocalDate.parse(date);
        LocalDateTime localDateTime = localDate.atStartOfDay();
        return Timestamp.valueOf(localDateTime);
    }

    public static Timestamp convertStringToTimestampEndDay(String date) {
        LocalDate localDate = LocalDate.parse(date);
        LocalDateTime localDateTime = localDate.atTime(LocalTime.MAX);
        return Timestamp.valueOf(localDateTime);
    }
}
