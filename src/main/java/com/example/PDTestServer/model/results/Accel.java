package com.example.PDTestServer.model.results;

import lombok.*;

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
