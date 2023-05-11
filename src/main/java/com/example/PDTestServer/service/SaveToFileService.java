package com.example.PDTestServer.service;

import com.example.PDTestServer.controller.role.doctor.request.SaveTestDTO;
import com.example.PDTestServer.model.results.SideResults;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class SaveToFileService {
    public File save(SideResults sideResults, SaveTestDTO saveTestDTO) {
//        String fileName = saveTestDTO.getTestNameID().trim() + "_" + saveTestDTO.getPatientName() + "_" + saveTestDTO.getPatientSurname() + "_" + data.getDate().replace(" ", "_").replace(":", "-") + "_" + data.getSide();
        String fileName = saveTestDTO.getFileName();

        try {
            File file = new File(fileName);
            FileWriter writer = new FileWriter(file);
//            Gson gson = new Gson();
            writer.write("Type test: " + saveTestDTO.getTestName() + "\n");
            writer.write("Date test: " + sideResults.getDate() + "\n");
            writer.write("Side: " + sideResults.getSide() + "\n");

            writer.write("Data: \n");
            for (Map.Entry<String, Object> entry : sideResults.getData().entrySet()) {
                String key = entry.getKey();
                List<String> value = (List<String>) entry.getValue();
                writer.write(key + ":\n");
                value.forEach(element -> {
                    try {
                        writer.write(element + "\n");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            writer.close();
            return file;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

