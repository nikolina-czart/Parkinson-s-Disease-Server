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

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("save/{uid}")
    public ResponseEntity<String> saveUser(@PathVariable String uid, @RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(userService.saveUser(userDTO, uid), HttpStatus.OK);
    }

    @GetMapping("/{uid}")
    public UserDTO getUserDetails(@PathVariable String uid) throws ExecutionException, InterruptedException {
        return userService.getUserDetails(uid);
    }

    @PutMapping("/{uid}")
    public ResponseEntity<String> updateUser(@PathVariable String uid, @RequestBody Set<UserFieldUpdate> fieldsToUpdate) {
        return new ResponseEntity<>(userService.updateUser(uid, fieldsToUpdate), HttpStatus.OK);
    }

    @DeleteMapping("/{uid}")
    public ResponseEntity<String> deleteUser(@PathVariable String uid) {
        return new ResponseEntity<>(userService.deleteUser(uid), HttpStatus.OK);
    }
}
