package com.techSupport.intuitiveTechSupportapi.service;

import com.techSupport.intuitiveTechSupportapi.appConstants.Constants;
import com.techSupport.intuitiveTechSupportapi.entity.requestPojo.Customer;
import com.techSupport.intuitiveTechSupportapi.entity.requestPojo.ScheduleCall;
import com.techSupport.intuitiveTechSupportapi.exceptions.*;
import com.techSupport.intuitiveTechSupportapi.model.*;
import com.techSupport.intuitiveTechSupportapi.respositoryHandler.*;
import com.techSupport.intuitiveTechSupportapi.serviceHandler.CustomerServiceHandler;
import com.techSupport.intuitiveTechSupportapi.utility.DateUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.text.ParseException;

@Slf4j
@Service
public class CustomerService {

    @Value(Constants.maximumCallsInSlots)
    private int maxNoOfSlots;

    @Value(Constants.bookingFreezeTime)
    private int bookingFreezeTime;

    @Value(Constants.dateFormat)
    private String dateFormat;

    @Value(Constants.timeFormat)
    private String timeFormat;

    @Autowired
    private DateUtility dateUtility;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CallSupportService callSupportService;

    @Autowired
    private SlotService slotService;

    @Autowired
    private SlotOnDateRepoHandler slotOnDateRepoHandler;

    @Autowired
    private SlotRepoHandler slotRepoHandler;

    @Autowired
    private CallSupportRepoHandler callSupportRepoHandler;

    @Autowired
    private CustomerRepoHandler customerRepoHandler;

    @Autowired
    private ProductRepoHandler productRepoHandler;

    @Autowired
    private ProductOfCustomerRepoHandler productOfCustomerRepoHandler;

    @Autowired
    private CustomerServiceHandler customerServiceHandler;



    public CustomerDTO getCustomer(String searchString) throws EntityNotFoundException {
        try {
            log.info("Searching customer by :: {}", searchString);
            CustomerDTO customerDTO = customerRepoHandler.findByEmail(searchString);

            if (customerDTO == null)
                customerDTO = customerRepoHandler.findByPhoneNumber(searchString);

            log.info("Customer retrieved :: {}", customerDTO.toString());
            return customerDTO;
        }catch (Exception ex){
            String errorMessage="Error occured while fetching customer..";
            log.error(errorMessage);
            throw new EntityNotFoundException(errorMessage);
        }
    }


    public CallSupportDTO bookCall(ScheduleCall scheduleCall) throws BookSlotException, EmailNotificationException {

        try {
            log.info("Schedule Call information received :: {}", scheduleCall.toString());

            log.info("Checking validation conditions");
            customerServiceHandler.ifSoltCanBeBooked(scheduleCall);

            log.info("Generating call support data from received information");
            CallSupportDTO callSupportDTO = getCallSupportDetailsForBooking(scheduleCall);

            log.info("Saving call support information {} ",callSupportDTO.toString());
            callSupportDTO = callSupportService.saveCallSupportDto(callSupportDTO);

            log.info("Booking successfully made, updating booking count in slot dto table");
            SlotOnDateDTO slotOnDateDTO = slotOnDateRepoHandler.findbyId(callSupportDTO.getDateSlotId());
            slotService.updateSlotOnBookingCancellation(slotOnDateDTO,1);

            log.info("Saving slot dto table {}",slotOnDateDTO.toString());
            slotOnDateRepoHandler.saveSlotOnDate(slotOnDateDTO);

            log.info("Sending email notification..");
            customerServiceHandler.sendEmailNotification(callSupportDTO, CallStatus.Booked);

            return callSupportDTO;

        }catch (BookSlotException|EmailNotificationException ex) {
            log.error(ex.getMessage());
            throw ex;
        }
        catch (Exception ex) {
            String errorMessage = String.format("Some exception occured while booking slot.. with error ====> %s",ex.getMessage());
            log.error(errorMessage);
            throw new BookSlotException(errorMessage);
        }

    }



    public CallSupportDTO cancelCall(BigInteger callId) throws CancelSlotException, EmailNotificationException {

        try {

            log.info("Checking validation conditions");
            customerServiceHandler.ifSlotCanBeCancelled(callId);

            log.info("Generating call support data from received information");
            CallSupportDTO callSupportDTO = getCallSupportDetailsForCancellation(callId);

            log.info("Saving call support information {} ",callSupportDTO.toString());
            callSupportDTO = callSupportService.saveCallSupportDto(callSupportDTO);

            log.info("Cancellation successfully made, updating booking count in slot dto table");
            SlotOnDateDTO slotOnDateDTO = slotOnDateRepoHandler.findbyId(callSupportDTO.getDateSlotId());
            slotService.updateSlotOnBookingCancellation(slotOnDateDTO,-1);

            log.info("Saving slot dto table {}",slotOnDateDTO.toString());
            slotOnDateRepoHandler.saveSlotOnDate(slotOnDateDTO);

            log.info("Sending email notification..");
            customerServiceHandler.sendEmailNotification(callSupportDTO, CallStatus.Cancelled);


            return callSupportDTO;
        }
        catch (EmailNotificationException ex)
        {
            log.error(ex.getMessage());
            throw ex;
        }
        catch(Exception ex)
        {
            String errorMessage = String.format("Some exception occured while cancelling slot.. with error ====> %s",ex.getMessage());
            log.error(errorMessage);
            throw new CancelSlotException(errorMessage);
        }
    }


    private CallSupportDTO getCallSupportDetailsForBooking(ScheduleCall scheduleCall) throws EntityNotFoundException {
        try {
            log.info("Generating call support data from schedule call input :: {}",scheduleCall.toString());
            CallSupportDTO callSupportDTO = new CallSupportDTO();
            SlotOnDateDTO slotOnDateDTO = slotOnDateRepoHandler.findByDateAndSlotId(dateUtility.getDateFromDateString(scheduleCall.getDate(), dateFormat), scheduleCall.getSlotId());
            callSupportDTO.setDateSlotId(slotOnDateDTO.getId());
            CustomerDTO customerDTO = getCustomer(scheduleCall.getUserPhoneNumber());
            ProductDTO productDTO = productRepoHandler.findByProductCode(scheduleCall.getProductCode());
            ProductsOfCustomerDTO productsOfCustomerDTO = productOfCustomerRepoHandler.findByProductIdAndCustomerId(productDTO.getId(), customerDTO.getId());
            callSupportDTO.setProductCustomerId(productsOfCustomerDTO.getId());
            callSupportDTO.setCallStatus(CallStatus.Booked.name());

            log.info("Call Support data generated successfully :: {}",callSupportDTO.toString());
            return callSupportDTO;
        }catch (ParseException ex) {
            String errorMessage = "Parsing exception occured.. please check the date format!!";
            log.error(errorMessage);
            throw new EntityNotFoundException(errorMessage);
        }
        catch (EntityNotFoundException ex) {
            throw ex;
        }
        catch (Exception ex) {
            String errorMessage = "Some internal error occured while generating call records data!!";
            log.error(errorMessage);
            throw new EntityNotFoundException(errorMessage);
        }
    }



    private CallSupportDTO getCallSupportDetailsForCancellation(BigInteger callId) throws EntityNotFoundException {
        try {
            log.info("Generating call support data from callId :: {}",callId);
            CallSupportDTO callSupportDTO = callSupportRepoHandler.findById(callId);
            callSupportDTO.setCallStatus(CallStatus.Cancelled.name());
            return callSupportDTO;
        }catch (Exception ex) {
            String errorMessage = "Not able to get data for call support ";
            log.error(errorMessage);
            throw new EntityNotFoundException(errorMessage);
        }
    }



    //To-Do if api has to be added
    public CustomerDTO generateCustomerData(Customer customer) throws EntitySaveException {
        try{
            log.info("Customer information received :: {}",customer.toString());
            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setFirstName(customer.getFirstName());
            customerDTO.setLastName(customer.getLastName());
            customerDTO.setEmail(customer.getEmail());
            customerDTO.setPhoneNumber(customer.getPhoneNumber());
            return customerDTO;
        }
        catch (Exception ex){
            String errorMessage = "Error occured while saving customer..";
            log.error(errorMessage);
            throw new EntitySaveException(errorMessage);
        }
    }

    public CustomerDTO saveCustomer(Customer customer) throws EntitySaveException {
        try {
            CustomerDTO customerDTO = generateCustomerData(customer);
            log.info("Customer information saved in database :: {}", customerDTO.toString());
            return customerDTO;
        }
        catch(Exception ex) {
            String errorMessage = "Error occured while saving customer..";
            log.error(errorMessage);
            throw new EntitySaveException(errorMessage);
        }
    }


}





