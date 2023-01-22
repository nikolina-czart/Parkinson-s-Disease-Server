package com.example.PDTestServer.controller.user;

import com.example.PDTestServer.controller.user.request.UserDTO;
import com.example.PDTestServer.model.UserDAO;
import com.example.PDTestServer.service.UserService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("save/{uid}")
    public String saveUser(@PathVariable String uid, @RequestBody UserDTO userDTO) throws ExecutionException, InterruptedException {
        return userService.saveUser(userDTO, uid);
    }

    @GetMapping("/{uid}")
    public UserDTO getUserDetails(@PathVariable String uid) throws ExecutionException, InterruptedException {
        return userService.getUserDetails(uid);
    }

    @PutMapping("/{uid}")
    public String updateUser(@PathVariable String uid, @RequestBody UserDTO userDTO) throws ExecutionException, InterruptedException {
        return userService.updateUser(uid, userDTO);
    }

    @DeleteMapping("/{uid}")
    public String deleteUser(@PathVariable String uid) throws ExecutionException, InterruptedException {
        return userService.deleteUser(uid);
    }
}
