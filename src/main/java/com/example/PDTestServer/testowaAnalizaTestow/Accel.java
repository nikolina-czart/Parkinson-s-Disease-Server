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
public class Accel {
    private List<String> timestamp;
    private List<String> x;
    private List<String> y;
    private List<String> z;
}
