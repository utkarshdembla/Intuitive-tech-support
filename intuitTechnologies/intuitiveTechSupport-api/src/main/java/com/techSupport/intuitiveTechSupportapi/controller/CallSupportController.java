package com.techSupport.intuitiveTechSupportapi.controller;

import com.techSupport.intuitiveTechSupportapi.entity.ScheduleCall;
import com.techSupport.intuitiveTechSupportapi.exceptions.EntityNotFoundException;
import com.techSupport.intuitiveTechSupportapi.model.*;
import com.techSupport.intuitiveTechSupportapi.repository.*;
import com.techSupport.intuitiveTechSupportapi.service.CallSupportService;
import com.techSupport.intuitiveTechSupportapi.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/techsupport/call")
public class CallSupportController {

    @Autowired
    private CallSupportService callSupportService;

    @GetMapping("/v1/getAvailableSlots/{date}")
    public List<SlotOnDateDTO> getAllAvailableSlotsOnDate(@PathVariable("date") String date) throws EntityNotFoundException {
        return callSupportService.getAllAvailableSlots(date);
    }

    @GetMapping("/v1/getAllSlots/{date}")
    public List<SlotOnDateDTO> getAllSlotsOnDate(@PathVariable("date") String date) throws EntityNotFoundException {
        return callSupportService.getAllSlotsOnDate(date);
    }

    @GetMapping("/v1/getAllCallsInfo")
    public List<CallSupportDTO> getAllCallsInfo() {
        return callSupportService.getAllCalls();
    }

    @GetMapping("/v1/getBookedSlots/{date}")
    public List<CallSupportDTO> getAllBookedSlots(@PathVariable("date") String date) throws EntityNotFoundException {
        return callSupportService.getAllBookedSlots(date);
    }



}
