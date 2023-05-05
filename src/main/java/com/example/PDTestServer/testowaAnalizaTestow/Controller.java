package com.example.PDTestServer.testowaAnalizaTestow;

import com.example.PDTestServer.controller.role.user.request.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/testowa-analiza")
public class Controller {

    @Autowired
    Servis servis;

    @GetMapping("/readFile")
    public String readDataFromFile() {
        return servis.readDataFromFile();
    }
}
