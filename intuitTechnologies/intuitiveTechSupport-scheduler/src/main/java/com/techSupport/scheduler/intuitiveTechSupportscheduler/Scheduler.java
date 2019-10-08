package com.techSupport.scheduler.intuitiveTechSupportscheduler;

import com.techSupport.scheduler.intuitiveTechSupportscheduler.service.ReminderService;
import com.techSupport.scheduler.intuitiveTechSupportscheduler.service.SlotUpdateService;
import com.techSupport.scheduler.intuitiveTechSupportscheduler.service.TrainerAssignmentService;
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

    @Autowired
    private TrainerAssignmentService trainerAssignmentService;

    @Scheduled(cron = "${support.scheduler.slots.cron}")
    public void runSlotscheduler() throws ParseException {
        log.info("Running slot Scheduler");
        slotUpdateService.slotUpdate();
    }

    @Scheduled(cron = "${support.scheduler.customer.cron}")
    public void runCustomerUpdateScheduler() throws ParseException, InterruptedException {
        log.info("Running customer scheduler");
        reminderService.sendReminderAlerts();
    }

    @Scheduled(cron = "${support.scheduler.trainer.cron}")
    public void runTrainerAssignmentScheduler()
    {
        log.info("Running trainer assignment scheduler");
        trainerAssignmentService.assignTrainer();
    }
}
