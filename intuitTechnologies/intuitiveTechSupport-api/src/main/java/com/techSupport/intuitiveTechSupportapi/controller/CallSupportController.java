package com.techSupport.intuitiveTechSupportapi.controller;

import com.techSupport.intuitiveTechSupportapi.entity.ResponseEntity.CallInfo;
import com.techSupport.intuitiveTechSupportapi.exceptions.DataGenerationException;
import com.techSupport.intuitiveTechSupportapi.exceptions.EntityNotFoundException;
import com.techSupport.intuitiveTechSupportapi.service.CallSupportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/techsupport/call")
public class CallSupportController {

    @Autowired
    private CallSupportService callSupportService;

    @GetMapping("/v1/getAllCallsInfo")
    public List<CallInfo> getAllCallsInfo() throws DataGenerationException, EntityNotFoundException {
        return callSupportService.getAllCalls();
    }

    @GetMapping("/v1/getAllCallsInfoByDate/{date}")
    public List<CallInfo> getAllCallsInfoByDate(@PathVariable("date") String date) throws ParseException, EntityNotFoundException, DataGenerationException
    {return callSupportService.getAllCallsInfoByDate(date);}

    @GetMapping("/v1/getAllCallsInfoByName/{name}")
    public List<CallInfo> getAllCallsInfoByName(@PathVariable("name") String name) throws EntityNotFoundException, DataGenerationException
    {return callSupportService.getAllCallsInfoByName(name);}

}
