package com.example.PDTestServer.controller.result;

import com.example.PDTestServer.controller.result.request.ResultRequestDTO;
import com.example.PDTestServer.controller.result.response.ResultFingerTappingDTO;
import com.example.PDTestServer.controller.user.request.UserDTO;
import com.example.PDTestServer.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/result")
public class ResultController {

    @Autowired
    ResultService resultService;

    @PostMapping("/{uid}/fingertapping")
    public List<ResultFingerTappingDTO> saveUser(@PathVariable String uid, @RequestBody ResultRequestDTO resultRequestDTO) throws ExecutionException, InterruptedException {
        return resultService.getFingerTappingData(uid, resultRequestDTO);
    }
}
