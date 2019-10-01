package com.techSupport.scheduler.intuitiveTechSupportscheduler.service;

import com.techSupport.scheduler.intuitiveTechSupportscheduler.constant.Constants;
import com.techSupport.scheduler.intuitiveTechSupportscheduler.model.*;
import com.techSupport.scheduler.intuitiveTechSupportscheduler.repository.*;
import com.techSupport.scheduler.intuitiveTechSupportscheduler.utility.DateUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.List;

@Slf4j
@Service
public class ReminderService {

    @Value(Constants.dateFormat)
    private String dateFormat;

    @Value(Constants.timeFormat)
    private String timeFormat;

    @Autowired
    private DateUtility dateUtility;

    @Autowired
    private ProductsOfCustomerRepository productsOfCustomerRepository;

    @Autowired
    private CallSupportRepository callSupportRepository;

    @Autowired
    private SlotOnDateRepository slotOnDateRepository;

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EmailService emailService;

    @Value(Constants.subject)
    private String emailSubject;

    @Value(Constants.body)
    private String emailBody;


    public void sendReminderAlerts() throws ParseException, InterruptedException {

        log.info("Generating slots for sending remainder..");
        SlotDTO slotDTO = getSlotForSendingReminder();

        log.info("Generating list of calls booked on the slot..");
        List<CallSupportDTO> lisOfCalls=processForSlot(slotDTO);

        log.info("Sending email reminders to all customers..");
        for(CallSupportDTO call:lisOfCalls) {
            sendNotification(call.getProductCustomerId(),slotDTO);
        }
    }

    private SlotDTO getSlotForSendingReminder() throws ParseException {
        List<SlotDTO> listOfSlots = slotRepository.findAll();
        String time = dateUtility.getDateStringFromDate(dateUtility.getFutureTime(0),timeFormat);
        String future = dateUtility.getDateStringFromDate(dateUtility.getFutureTime(1),timeFormat);

        SlotDTO slotDTO = slotRepository.findByTime(time,future);

        for(SlotDTO slot:listOfSlots) {
            if(slot.getStartTime().equals(slotDTO.getStartTime()) && slot.getEndTime().equals(slotDTO.getEndTime())){
                log.info("Sending slot :: {}-{} for processing",slot.getStartTime(),slot.getEndTime());
                return slot;
            }
        }
        return slotDTO;
    }

    public List<CallSupportDTO> processForSlot(SlotDTO slotDTO) throws ParseException {
        String today = dateUtility.getDateStringFromDate(dateUtility.getFutureDate(0),dateFormat);
        SlotOnDateDTO slotOnDateDTO = slotOnDateRepository.findbyDateAndSlotId(dateUtility.getDateFromDateString(today, dateFormat),slotDTO.getId());
        List<CallSupportDTO> listOfCalls = callSupportRepository.findByDateSlotIdAndcallStatus(slotOnDateDTO.getId(), CallStatus.Booked.name());
        log.info("List of calls on the given slot.. "+listOfCalls.toString());
        return listOfCalls;
    }

    private void sendNotification(BigInteger productOfCustomerId,SlotDTO slotDTO) throws InterruptedException {

        ProductsOfCustomerDTO productsOfCustomerDTO = productsOfCustomerRepository.findByid(productOfCustomerId);
        ProductDTO productDTO = productRepository.findByid(productsOfCustomerDTO.getProductId());
        CustomerDTO customerDTO = customerRepository.findByid(productsOfCustomerDTO.getCustomerId());
        String body = String.format(emailBody,slotDTO.getStartTime()+"-"+slotDTO.getEndTime(),productDTO.getName());

        log.info("Sending email to customer :: {} for product :: {} for slot :: {}-{}",customerDTO.getEmail(),productDTO.getName(),slotDTO.getStartTime(),slotDTO.getEndTime());
        emailService.sendEmail(customerDTO.getEmail(),emailSubject,body);
        Thread.sleep(2000);
    }

}
