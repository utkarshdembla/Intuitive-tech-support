package com.techSupport.intuitiveTechSupportapi.serviceHandler;

import com.techSupport.intuitiveTechSupportapi.appConstants.Constants;
import com.techSupport.intuitiveTechSupportapi.entity.requestPojo.ScheduleCall;
import com.techSupport.intuitiveTechSupportapi.exceptions.BookSlotException;
import com.techSupport.intuitiveTechSupportapi.exceptions.CancelSlotException;
import com.techSupport.intuitiveTechSupportapi.exceptions.EmailNotificationException;
import com.techSupport.intuitiveTechSupportapi.model.*;
import com.techSupport.intuitiveTechSupportapi.respositoryHandler.*;
import com.techSupport.intuitiveTechSupportapi.service.NotificationService;
import com.techSupport.intuitiveTechSupportapi.utility.DateUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class CustomerServiceHandler {

    @Value(Constants.maximumCallsInSlots)
    private int maxNoOfSlots;

    @Value(Constants.dateFormat)
    private String dateFormat;

    @Value(Constants.bookingFreezeTime)
    private int bookingFreezeTime;

    @Value(Constants.timeFormat)
    private String timeFormat;

    @Value(Constants.bookingConfirmationSubject)
    private String bookingConfirmationSubject;

    @Value(Constants.bookingConfirmationBody)
    private String bookingConfirmationBody;

    @Value(Constants.bookingCancellationSubject)
    private String bookingCancellationSubject;

    @Value(Constants.bookingCancellationBody)
    private String bookingCancellationBody;

    @Autowired
    private DateUtility dateUtility;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CallSupportRepoHandler callSupportRepoHandler;

    @Autowired
    private ProductOfCustomerRepoHandler productOfCustomerRepoHandler;

    @Autowired
    private SlotRepoHandler slotRepoHandler;

    @Autowired
    private SlotOnDateRepoHandler slotOnDateRepoHandler;

    @Autowired
    private CustomerRepoHandler customerRepoHandler;

    @Autowired
    private ProductRepoHandler productRepoHandler;


    public void ifSoltCanBeBooked(ScheduleCall scheduleCall) throws BookSlotException {
        try {
            String today = dateUtility.getDateStringFromDate(dateUtility.getFutureDate(0), dateFormat);

            SlotOnDateDTO slotOnDateDTO = slotOnDateRepoHandler.findByDateAndSlotId(dateUtility.getDateFromDateString(scheduleCall.getDate(), dateFormat), scheduleCall.getSlotId());
            if (slotOnDateDTO == null) {
                String errorMessage = String.format("Slot does not exist for slotId :: %s and date :: %s", scheduleCall.getSlotId(), scheduleCall.getDate());
                log.error(errorMessage);
                throw new BookSlotException(errorMessage);
            }

            if (slotOnDateDTO.getBookedCount() == maxNoOfSlots) {
                String errorMessage = String.format("Slot are full for slotId :: %s and date :: %s", scheduleCall.getSlotId(), scheduleCall.getDate());
                log.error(errorMessage);
                throw new BookSlotException(errorMessage);
            }

            if(dateUtility.getDateFromDateString(scheduleCall.getDate(),dateFormat).before(dateUtility.getDateFromDateString(today,dateFormat)))
                throw new BookSlotException("Customer cannot book the slot on previous dates");

            CustomerDTO customerDTO = customerRepoHandler.findByPhoneNumber(scheduleCall.getUserPhoneNumber());
            List<CallSupportDTO> listForCallsOnDaySlot = callSupportRepoHandler.findAllByDateSlotId(slotOnDateDTO.getId());

            for(CallSupportDTO value:listForCallsOnDaySlot){
                ProductsOfCustomerDTO productsOfCustomerDTOs=productOfCustomerRepoHandler.findById(value.getProductCustomerId());
                if(customerDTO.getId().equals(productsOfCustomerDTOs.getCustomerId()) &&
                        !value.getCallStatus().equalsIgnoreCase(CallStatus.Cancelled.name())){
                    throw new BookSlotException("User cannot book multiple calls in same slot");
                }
            }

            if (today.equals(scheduleCall.getDate())) {
                log.info("Checking time constraints for slot booking,if slot to be booked is within range of 2 hours");
                String time = dateUtility.getDateStringFromDate(dateUtility.getFutureTime(0), timeFormat);
                String future = dateUtility.getDateStringFromDate(dateUtility.getFutureTime(bookingFreezeTime), timeFormat);

                List<SlotDTO> slotList = slotRepoHandler.findBetweenStartTimeEndTime(time,future);
                for (SlotDTO slotValue : slotList) {
                    SlotDTO slotDTO = slotRepoHandler.findBySlotId(scheduleCall.getSlotId());
                    if (slotDTO.getStartTime().equals(slotValue.getStartTime()) && slotDTO.getEndTime().equals(slotValue.getEndTime()))
                        throw new BookSlotException("Customer cannot book the slot, too late to book the slot");
                }
            }

        }
        catch (BookSlotException ex) {
            log.error(ex.getMessage());
            throw ex;
        }
        catch (Exception ex){
            String errorMessage = String.format("Some exception occured while checking if slot can be booked.. with error ====> %s",ex.getMessage());
            throw new BookSlotException(errorMessage);
        }
    }


    public void ifSlotCanBeCancelled(BigInteger callId) throws CancelSlotException {
        try {
            String time = dateUtility.getDateStringFromDate(dateUtility.getFutureTime(0), timeFormat);
            String future = dateUtility.getDateStringFromDate(dateUtility.getFutureTime(bookingFreezeTime), timeFormat);
            String today = dateUtility.getDateStringFromDate(dateUtility.getFutureDate(0), dateFormat);

            CallSupportDTO callSupportDTO = callSupportRepoHandler.findById(callId);

            if (callSupportDTO == null)
                throw new CancelSlotException("Call doesnt exist,cant cancel!!");

            SlotOnDateDTO slotOnDateDTO = slotOnDateRepoHandler.findbyId(callSupportDTO.getDateSlotId());

            if (callSupportDTO.getCallStatus().equalsIgnoreCase(CallStatus.Cancelled.name()))
                throw new CancelSlotException("Slot already cancelled by the user,cant cancel!!");

            if (slotOnDateDTO.getBookedCount() == 0)
                throw new CancelSlotException("Slot already empty,cant cancel!!");


            if (dateUtility.getDateFromDateString(dateUtility.getDateStringFromDate(slotOnDateDTO.getDate(), dateFormat), dateFormat).before(dateUtility.getDateFromDateString(today, dateFormat)))
                throw new BookSlotException("Customer cannot cancel the slot on previous dates");


            if (dateUtility.getDateStringFromDate(slotOnDateDTO.getDate(), dateFormat).equals(today)) {
                SlotDTO slotDTO = slotRepoHandler.findBySlotId(slotOnDateDTO.getSlotId());
                List<SlotDTO> slotList = slotRepoHandler.findBetweenStartTimeEndTime(time, future);

                for (SlotDTO slotValue : slotList) {
                    if (slotDTO.getStartTime().equals(slotValue.getStartTime()) && slotDTO.getEndTime().equals(slotValue.getEndTime())) {
                        throw new CancelSlotException("Customer cannot cancel the slot!!");
                    }
                }

            }
        } catch (CancelSlotException ex) {
            log.error(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            String errorMessage = String.format("Some exception occured while checking for cancelling slot.. with error ====> %s", ex.getMessage());
            log.error(errorMessage);
            throw new CancelSlotException(errorMessage);
        }

    }

        public void sendEmailNotification(CallSupportDTO callSupportDTO,CallStatus callStatus) throws
                EmailNotificationException {
            try {
                log.info("Generating data for send email notification for call :: {} for status :: {}",callSupportDTO.toString(),callStatus.name());
                SlotOnDateDTO slotOnDateDTO = slotOnDateRepoHandler.findbyId(callSupportDTO.getDateSlotId());
                SlotDTO slotDTO = slotRepoHandler.findBySlotId(slotOnDateDTO.getSlotId());

                ProductsOfCustomerDTO productsOfCustomerDTO = productOfCustomerRepoHandler.findById(callSupportDTO.getProductCustomerId());
                ProductDTO productDTO = productRepoHandler.findByProductId(productsOfCustomerDTO.getProductId());
                CustomerDTO customerDTO = customerRepoHandler.findById(productsOfCustomerDTO.getCustomerId());

                sendNotification(customerDTO, productDTO, slotDTO, slotOnDateDTO.getDate(), callStatus);
            }
            catch (EmailNotificationException ex){
                log.error(ex.getMessage());
                throw ex;
            }
            catch (ParseException ex) {
                String errorMessage = String.format("Parsing error occured while sending notification.. ==> %s",ex.getMessage());
                log.error(errorMessage);
                throw new EmailNotificationException(errorMessage);
            }
            catch (Exception ex)
            {
                String errorMessage = String.format("Some error occured while sending notification.. ==> %s",ex.getMessage());
                log.error(errorMessage);
                throw new EmailNotificationException(errorMessage);
            }

        }

        private void sendNotification(CustomerDTO customerDTO, ProductDTO productDTO, SlotDTO slotDTO, Date date, CallStatus callStatus) throws ParseException, EmailNotificationException {
            try {
                if (callStatus.equals(CallStatus.Booked)) {
                    String body = String.format(bookingConfirmationBody, dateUtility.getDateStringFromDate(slotDTO.getStartTime(),timeFormat) + "-" + dateUtility.getDateStringFromDate(slotDTO.getEndTime(),timeFormat), productDTO.getName(), dateUtility.getDateStringFromDate(date, dateFormat));
                    notificationService.sendEmail(customerDTO.getEmail(), bookingConfirmationSubject, body);
                    log.info("Email notification sent!!");
                    return;
                }
                if (callStatus.equals(CallStatus.Cancelled)) {
                    String body = String.format(bookingCancellationBody, dateUtility.getDateStringFromDate(slotDTO.getStartTime(),timeFormat) + "-" + dateUtility.getDateStringFromDate(slotDTO.getEndTime(),timeFormat), productDTO.getName(), dateUtility.getDateStringFromDate(date, dateFormat));
                    notificationService.sendEmail(customerDTO.getEmail(), bookingCancellationSubject, body);
                    log.info("Email notification sent!!");
                    return;
                }
            }
            catch (Exception ex)
            {
                String errorMessage = String.format("Some error occured while sending notification.. ==> %s",ex.getMessage());
                log.error(errorMessage);
                throw new EmailNotificationException(errorMessage);
            }
        }








}
