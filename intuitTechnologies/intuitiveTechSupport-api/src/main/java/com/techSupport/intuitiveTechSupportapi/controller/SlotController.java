package com.techSupport.intuitiveTechSupportapi.controller;


import com.techSupport.intuitiveTechSupportapi.entity.responsePojo.SlotInfo;
import com.techSupport.intuitiveTechSupportapi.exceptions.EntityNotFoundException;
import com.techSupport.intuitiveTechSupportapi.service.CallSupportService;
import com.techSupport.intuitiveTechSupportapi.service.SlotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/techsupport/slots")
public class SlotController {


    @Autowired
    private CallSupportService callSupportService;

    @Autowired
    private SlotService slotService;

    @GetMapping("/v1/getAvailableSlots/{date}")
    public List<SlotInfo> getAllAvailableSlotsOnDate(@PathVariable("date") String date) throws EntityNotFoundException {
        return slotService.getAllAvailableSlots(date);
    }
}
