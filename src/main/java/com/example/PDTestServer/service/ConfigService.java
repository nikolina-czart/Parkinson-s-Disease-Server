package com.example.PDTestServer.service;

import com.example.PDTestServer.controller.tests.config.request.ConfigTestDTO;
import com.example.PDTestServer.model.tests.ConfigTestDAO;
import com.example.PDTestServer.repository.ConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Service
public class ConfigService {
    @Autowired
    ConfigRepository configRepository;

    public Set<ConfigTestDTO> getBaseTestDetails() throws ExecutionException, InterruptedException {
        Set<ConfigTestDAO> configTestDAOs = configRepository.getBaseTestDetails();
        return converterDAOtoDTO(configTestDAOs);
    }

    private Set<ConfigTestDTO> converterDAOtoDTO(Set<ConfigTestDAO> configTests) {
        Set<ConfigTestDTO> configTestDTOs = new HashSet<>();

        configTests.forEach((configTest) ->
                configTestDTOs.add(ConfigTestDTO.builder()
                                .uid(configTest.getUid())
                                .icon(configTest.getIcon())
                                .namePL(configTest.getNamePL())
                                .name(configTest.getName())
                                .build())
        );

        return configTestDTOs;
    }
}
