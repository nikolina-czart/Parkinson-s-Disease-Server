package com.example.PDTestServer.controller.role.user.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
//TODO - good
public class UserFieldUpdate {
    private String fieldName;
    private String fieldValue;
}
