package com.example.PDTestServer.testowaAnalizaTestow;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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