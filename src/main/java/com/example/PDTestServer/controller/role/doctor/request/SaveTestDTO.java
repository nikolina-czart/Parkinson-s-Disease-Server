package com.example.PDTestServer.controller.role.doctor.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class SaveTestDTO {
    private String testId;
    private String testName;
    private String fileName;
    private String side;
}
