package com.example.PDTestServer.controller.user.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class PatientTestDTO {
    private String uid;
    private String name;
}
