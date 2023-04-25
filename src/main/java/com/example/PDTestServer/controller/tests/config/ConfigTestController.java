package com.example.PDTestServer.controller.tests.config;

import com.example.PDTestServer.controller.tests.config.request.ConfigTestDTO;
import com.example.PDTestServer.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.concurrent.ExecutionException;

//TODO - good
@RestController
@RequestMapping("/api/config")
public class ConfigTestController {

    @Autowired
    private ConfigService configService;

    //TODO - good
    @GetMapping("/test")
    public Set<ConfigTestDTO> getBaseTestDetails() throws ExecutionException, InterruptedException {
        return configService.getBaseTestDetails();
    }
}
