package com.example.PDTestServer.testowaAnalizaTestow;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Tapping {
    private List<String> timestamp;
    private List<String> upDown;
    private List<String> x;
    private List<String> y;
    private List<String> clickSide;
}
