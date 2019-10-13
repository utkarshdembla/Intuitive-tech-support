package com.techSupport.intuitiveTechSupportapi.controller;


import com.techSupport.intuitiveTechSupportapi.entity.requestPojo.Customer;
import com.techSupport.intuitiveTechSupportapi.entity.requestPojo.ScheduleCall;
import com.techSupport.intuitiveTechSupportapi.exceptions.*;
import com.techSupport.intuitiveTechSupportapi.model.*;
import com.techSupport.intuitiveTechSupportapi.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;


@Slf4j
@RestController
@RequestMapping("/techsupport/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/v1/getCustomer/{searchString}")
    public CustomerDTO getCustomer(@PathVariable("searchString") String searchString) throws EntityNotFoundException {
        return customerService.getCustomer(searchString);
    }
    @PostMapping("/v1/addCustomer")
    public CustomerDTO saveCustomer(@RequestBody Customer customer) throws EntitySaveException {
      return customerService.saveCustomer(customer);
    }

    @PostMapping("/v1/bookCall")
    public CallSupportDTO bookCall(@RequestBody ScheduleCall scheduleCall) throws BookSlotException, EmailNotificationException {
        return customerService.bookCall(scheduleCall);
    }

    @PostMapping("/v1/cancelCall/{callId}")
    public CallSupportDTO cancelCall(@PathVariable("callId") BigInteger callId) throws CancelSlotException, EmailNotificationException {
        return customerService.cancelCall(callId);
    }


}
