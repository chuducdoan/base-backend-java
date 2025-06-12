package com.example.myshop.job;

import com.example.myshop.service.SMSTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
public class SMSTestScheduler {

    @Autowired
    private SMSTestService smsTestService;

    @Scheduled(fixedRate = 60000) // gửi mỗi phút
    public void scheduleFixedRateTask() {
        smsTestService.sendSmsDataToQueue();
    }
}
