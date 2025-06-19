package com.example.myshop.controller;

import com.example.myshop.dto.request.smstest.SMSTestRequestDTO;
import com.example.myshop.dto.request.track.TrackByUserRequest;
import com.example.myshop.dto.request.track.TrackRequest;
import com.example.myshop.response.BaseResponse;
import com.example.myshop.response.ResponseCode;
import com.example.myshop.service.SMSTestService;
import com.example.myshop.service.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/tracks")
public class TrackController {

    @Autowired
    private TrackService trackService;

    @PostMapping
    public BaseResponse create(@RequestBody TrackRequest request) {
        return trackService.create(request);
    }

    @PostMapping("/user")
    public BaseResponse getListTrackByUser(@RequestBody TrackByUserRequest user) {
        return trackService.getListByUser(user);
    }

    @GetMapping
    public BaseResponse getAll() {
        return trackService.getAllTrack();
    }
}
