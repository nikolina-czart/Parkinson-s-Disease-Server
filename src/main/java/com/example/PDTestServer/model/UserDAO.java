package com.example.PDTestServer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
