package com.example.PDTestServer.controller.role.user.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
//TODO - good
public class UserDTO {
    private String uid;
    private String name;
    private String surname;
    private String email;
    private String role;
    private String doctorID;
}
