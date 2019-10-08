package com.techSupport.scheduler.intuitiveTechSupportscheduler.service;

import com.techSupport.scheduler.intuitiveTechSupportscheduler.constant.Constants;
import com.techSupport.scheduler.intuitiveTechSupportscheduler.model.CallStatus;
import com.techSupport.scheduler.intuitiveTechSupportscheduler.model.CallSupportDTO;
import com.techSupport.scheduler.intuitiveTechSupportscheduler.model.SlotOnDateDTO;
import com.techSupport.scheduler.intuitiveTechSupportscheduler.model.TrainerDTO;
import com.techSupport.scheduler.intuitiveTechSupportscheduler.repository.CallSupportRepository;
import com.techSupport.scheduler.intuitiveTechSupportscheduler.repository.SlotOnDateRepository;
import com.techSupport.scheduler.intuitiveTechSupportscheduler.repository.TrainerRepository;
import com.techSupport.scheduler.intuitiveTechSupportscheduler.utility.DateUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static com.techSupport.scheduler.intuitiveTechSupportscheduler.constant.Constants.dateFormat;

@Slf4j
@Service
public class TrainerAssignmentService {

    @Autowired
    private SlotOnDateRepository slotOnDateRepository;

    @Autowired
    private CallSupportRepository callSupportRepository;

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private DateUtility dateUtility;

    @Value(Constants.dateFormat)
    private String dateFormat;

    public void assignTrainer(){
        Queue<TrainerDTO>  trainerQueue = getAllTrainersQueued();
        String today = dateUtility.getDateStringFromDate(dateUtility.getFutureDate(0), dateFormat);
        List<SlotOnDateDTO> listOfSlotOnDate = slotOnDateRepository.findBydate(today);

        if(!listOfSlotOnDate.isEmpty()) {
            List<CallSupportDTO> listOfCalls;

            for (SlotOnDateDTO value : listOfSlotOnDate) {
                listOfCalls = callSupportRepository.findByDateSlotIdAndcallStatus(value.getId(), CallStatus.Booked.name());

                for (CallSupportDTO callValue : listOfCalls) {
                    if(callValue.getTrainerId()==null) {
                        TrainerDTO trainer = trainerQueue.remove();
                        CallSupportDTO callSupportDTO = callSupportRepository.findByDateSlotIdAndTrainerId(value.getId(), trainer.getId());
                        if (callSupportDTO != null) {
                            trainerQueue.add(trainer);
                            trainer = trainerQueue.remove();
                        }
                        callValue.setTrainerId(trainer.getId());
                        callSupportRepository.save(callValue);
                        trainerQueue.add(trainer);
                    }
                }
            }
        }else{
            log.info("No slots on date found..");
        }
    }

    public Queue<TrainerDTO> getAllTrainersQueued() {
        List<TrainerDTO> listOfAllTrainers = trainerRepository.findAll();
        Queue<TrainerDTO>  queue = new LinkedList<>();
        queue.addAll(listOfAllTrainers);

        return queue;
    }
}
