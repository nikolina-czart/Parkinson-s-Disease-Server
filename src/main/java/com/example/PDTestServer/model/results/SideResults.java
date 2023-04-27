package com.example.PDTestServer.model.results;

import lombok.*;

import java.util.ArrayList;
import java.util.Map;

@Setter
@Getter
@Builder
public class SideResults {
    private String date;
    private Map<String, Object> data;
    private String medicineSupply;
    private String side;
}