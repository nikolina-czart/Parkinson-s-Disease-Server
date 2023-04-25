package com.example.PDTestServer.service;

import com.example.PDTestServer.controller.tests.config.request.ConfigTestDTO;
import com.example.PDTestServer.model.tests.ConfigTestDAO;
import com.example.PDTestServer.repository.ConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static com.example.PDTestServer.utils.coverter.TestConverter.convertConfigTestDAOToDTO;

@Service
public class ConfigService {
    @Autowired
    ConfigRepository configRepository;

    //TODO - good
    public Set<ConfigTestDTO> getBaseTestDetails() throws ExecutionException, InterruptedException {
        Set<ConfigTestDAO> configTestDAOs = configRepository.getBaseTestDetails();
        return converterDAOtoDTO(configTestDAOs);
    }

    //TODO - good
    private Set<ConfigTestDTO> converterDAOtoDTO(Set<ConfigTestDAO> configTests) {
        Set<ConfigTestDTO> configTestDTOs = new HashSet<>();
        configTests.forEach((configTest) -> configTestDTOs.add(convertConfigTestDAOToDTO(configTest)));

        return configTestDTOs;
    }
}
