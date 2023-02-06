package com.example.PDTestServer.controller.user;

import com.example.PDTestServer.controller.user.request.UserDTO;
import com.example.PDTestServer.model.UserDAO;
import com.example.PDTestServer.service.UserService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("save/{uid}")
    public ResponseEntity<String> saveUser(@PathVariable String uid, @RequestBody UserDTO userDTO) {
        String response = userService.saveUser(userDTO, uid);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{uid}")
    public UserDTO getUserDetails(@PathVariable String uid) throws ExecutionException, InterruptedException {
        return userService.getUserDetails(uid);
    }

    @PutMapping("/{uid}")
    public String updateUser(@PathVariable String uid, @RequestBody UserDTO userDTO) {
        return userService.updateUser(uid, userDTO);
    }

    @DeleteMapping("/{uid}")
    public String deleteUser(@PathVariable String uid) {
        return userService.deleteUser(uid);
    }
}
