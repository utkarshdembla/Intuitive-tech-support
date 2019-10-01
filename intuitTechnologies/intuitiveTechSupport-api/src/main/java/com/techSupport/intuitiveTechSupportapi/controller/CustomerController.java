package com.techSupport.intuitiveTechSupportapi.controller;


import com.techSupport.intuitiveTechSupportapi.entity.Customer;
import com.techSupport.intuitiveTechSupportapi.entity.ScheduleCall;
import com.techSupport.intuitiveTechSupportapi.exceptions.*;
import com.techSupport.intuitiveTechSupportapi.model.*;
import com.techSupport.intuitiveTechSupportapi.repository.CustomerRepository;
import com.techSupport.intuitiveTechSupportapi.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


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
