package com.example.myshop.controller;

import com.example.myshop.dto.request.smstest.SMSTestRequestDTO;
import com.example.myshop.response.BaseResponse;
import com.example.myshop.service.SMSTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/sms-test")
public class SmsTestController {

    @Autowired
    private SMSTestService smsTestService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public BaseResponse create(@RequestBody SMSTestRequestDTO request) {
        return smsTestService.create(request);
    }


    @PostMapping("/test")
    public String homepage() {
        try {
            smsTestService.set("doancd", "doancd");
            return "success";
        } catch (Exception e) {
            return "error" + e.getMessage();
        }
    }
}
