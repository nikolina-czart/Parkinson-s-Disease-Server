package com.example.PDTestServer.controller.tests.config.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
//TODO - good
public class ConfigTestDTO {
    private String uid;
    private String name;
    private String namePL;
    private String icon;
}
