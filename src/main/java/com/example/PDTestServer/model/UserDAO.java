package com.example.PDTestServer.model;

import lombok.*;

@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class UserDAO {
    private String name;
    private String surname;
    private String role;
    private String doctorID;
}
