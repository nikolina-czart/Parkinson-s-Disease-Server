package com.example.PDTestServer.controller.role.user;

import com.example.PDTestServer.controller.role.user.request.UserDTO;
import com.example.PDTestServer.controller.role.user.request.UserFieldUpdate;
import com.example.PDTestServer.service.role.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.concurrent.ExecutionException;

//TODO - good
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    //TODO - good
    @PostMapping("save/{uid}")
    public ResponseEntity<String> saveUser(@PathVariable String uid, @RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(userService.saveUser(userDTO, uid), HttpStatus.OK);
    }

    //TODO - good
    @GetMapping("/{uid}")
    public UserDTO getUserDetails(@PathVariable String uid) throws ExecutionException, InterruptedException {
        return userService.getUserDetails(uid);
    }

    //TODO - good
    @PutMapping("/{uid}")
    public String updateUser(@PathVariable String uid, @RequestBody Set<UserFieldUpdate> fieldsToUpdate) {
        return userService.updateUser(uid, fieldsToUpdate);
    }

    //TODO - good
    @DeleteMapping("/{uid}")
    public String deleteUser(@PathVariable String uid) {
        return userService.deleteUser(uid);
    }
}
