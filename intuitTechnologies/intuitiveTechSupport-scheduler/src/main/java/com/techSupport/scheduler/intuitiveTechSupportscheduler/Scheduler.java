package com.techSupport.scheduler.intuitiveTechSupportscheduler;

import com.techSupport.scheduler.intuitiveTechSupportscheduler.service.ReminderService;
import com.techSupport.scheduler.intuitiveTechSupportscheduler.service.SlotUpdateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Slf4j
@Service
public class Scheduler {

    @Autowired
    private SlotUpdateService slotUpdateService;

    @Autowired
    private ReminderService reminderService;

    @Scheduled(cron = "${support.scheduler.slots.cron}")
    public void run() throws ParseException {
        log.info("Running slot Scheduler");
        slotUpdateService.slotUpdate();
        //deliveredAWBShipperUpdateCheck();

    }

    @Scheduled(cron = "${support.scheduler.customer.cron}")
    public void run2() throws ParseException, InterruptedException {

        log.info(String.valueOf(System.currentTimeMillis()));
        log.info("Running customer scheduler");
        reminderService.sendReminderAlerts();
    }
}
