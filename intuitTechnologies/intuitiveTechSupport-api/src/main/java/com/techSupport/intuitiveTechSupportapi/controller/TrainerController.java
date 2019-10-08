package com.techSupport.intuitiveTechSupportapi.controller;

import com.techSupport.intuitiveTechSupportapi.exceptions.BookSlotException;
import com.techSupport.intuitiveTechSupportapi.model.CallSupportDTO;
import com.techSupport.intuitiveTechSupportapi.service.TrainerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

@Slf4j
@RestController
@RequestMapping("/trainer/call")
public class TrainerController {

    @Autowired
    private TrainerService trainerService;

    @PostMapping("/v1/startSession/{callId}/{trainerId}")
    public CallSupportDTO bookSession(@PathVariable("callId")BigInteger callId,@PathVariable("trainerId")BigInteger trainerId) throws BookSlotException {
        return trainerService.bookSession(callId,trainerId);
    }

    @PostMapping("/v1/completeSession/{callId}/{trainerId}")
    public CallSupportDTO completeSession(@PathVariable("callId")BigInteger callId,@PathVariable("trainerId")BigInteger trainerId) throws BookSlotException {
        return trainerService.completeSession(callId,trainerId);
    }
}
