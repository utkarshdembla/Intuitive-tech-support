package com.techSupport.intuitiveTechSupportapi.service;

import com.techSupport.intuitiveTechSupportapi.appConstants.Constants;
import com.techSupport.intuitiveTechSupportapi.entity.Customer;
import com.techSupport.intuitiveTechSupportapi.entity.ScheduleCall;
import com.techSupport.intuitiveTechSupportapi.exceptions.*;
import com.techSupport.intuitiveTechSupportapi.model.*;
import com.techSupport.intuitiveTechSupportapi.repository.*;
import com.techSupport.intuitiveTechSupportapi.util.DateUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.sql.Time;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class CustomerService {

    @Value(Constants.bookingConfirmationSubject)
    private String bookingConfirmationSubject;

    @Value(Constants.bookingConfirmationBody)
    private String bookingConfirmationBody;

    @Value(Constants.bookingCancellationSubject)
    private String bookingCancellationSubject;

    @Value(Constants.bookingCancellationBody)
    private String bookingCancellationBody;

    @Autowired
    private DateUtility utility;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private SlotOnDateRepository slotOnDateRepository;

    @Autowired
    private CallSupportRepository callSupportRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductsOfCustomerRepository productsOfCustomerRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private SlotRepository slotRepository;

    @Value(Constants.maximumCallsInSlots)
    private int maxNoOfSlots;

    @Value(Constants.dateFormat)
    private String dateFormat;

    @Value(Constants.timeFormat)
    private String timeFormat;

    //this method returns the customer, search parameters can be:: {email,phonenumber}

    public CustomerDTO getCustomer(String searchString) throws EntityNotFoundException {
        try {
            log.info("Searching customer by :: {}", searchString);
            CustomerDTO customerDTO = customerRepository.findByEmail(searchString);

            if (customerDTO == null)
                customerDTO = customerRepository.findByPhoneNumber(searchString);

            log.info("Customer retrieved :: {}", customerDTO.toString());
            return customerDTO;
        }catch (Exception ex){
            String errorMessage="Error occured while fetching customer..";
            log.error(errorMessage);
            throw new EntityNotFoundException(errorMessage);
        }
    }

    public CustomerDTO saveCustomer(Customer customer) throws EntitySaveException {
        try {
            log.info("Customer information received :: {}",customer.toString());
            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setFirstName(customer.getFirstName());
            customerDTO.setLastName(customer.getLastName());
            customerDTO.setEmail(customer.getEmail());
            customerDTO.setPhoneNumber(customer.getPhoneNumber());
            customerDTO = customerRepository.save(customerDTO);
            log.info("Customer information saved in database :: {}", customerDTO.toString());
            return customerDTO;
        }
        catch(Exception ex) {
            String errorMessage = "Error occured while saving customer..";
            log.error(errorMessage);
            throw new EntitySaveException(errorMessage);
        }
    }

    private void ifSoltCanBeBooked(ScheduleCall scheduleCall) throws BookSlotException {
        try {
            String time = utility.getDateStringFromDate(utility.getFutureTime(0), timeFormat);
            String future = utility.getDateStringFromDate(utility.getFutureTime(2), timeFormat);
            String today = utility.getDateStringFromDate(utility.getFutureDate(0), dateFormat);

            if (today.equals(scheduleCall.getDate())) {
                List<SlotDTO> slotList = slotRepository.findByTime(time, future);

                for (SlotDTO slotValue : slotList) {
                    SlotDTO slotDTO = slotRepository.findBySlotId(scheduleCall.getSlotId());
                    if (slotDTO.getStartTime().equals(slotValue.getStartTime()) && slotDTO.getEndTime().equals(slotValue.getEndTime()))
                        throw new BookSlotException("Customer cannot book the slot!!");
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

    public CallSupportDTO bookCall(ScheduleCall scheduleCall) throws BookSlotException, EmailNotificationException {

        try {
            log.info("Schedule Call information received :: {}", scheduleCall.toString());

            ifSoltCanBeBooked(scheduleCall);
            CallSupportDTO callSupportDTO = new CallSupportDTO();

            SlotOnDateDTO slotOnDateDTO = slotOnDateRepository.findbyDateAndSlotId(utility.getDateFromDateString(scheduleCall.getDate(), dateFormat), scheduleCall.getSlotId());
            if (slotOnDateDTO == null) {
                String errorMessage = String.format("Slot does not exist for slotId :: %s and date :: %s", scheduleCall.getSlotId(), scheduleCall.getDate());
                log.error(errorMessage);
                throw new BookSlotException(errorMessage);
            }
            if (slotOnDateDTO.getBookedCount() < maxNoOfSlots) {
                callSupportDTO.setDateSlotId(slotOnDateDTO.getId());
            } else {
                String errorMessage = String.format("Slot are full for slotId :: %s and date :: %s", scheduleCall.getSlotId(), scheduleCall.getDate());
                log.error(errorMessage);
                throw new BookSlotException(errorMessage);
            }

            CustomerDTO customerDTO = getCustomer(scheduleCall.getUserPhoneNumber());
            ProductDTO productDTO = productRepository.findByProductCode(scheduleCall.getProductCode());
            ProductsOfCustomerDTO productsOfCustomerDTO = productsOfCustomerRepository.findByProductIdAndCustomerId(productDTO.getId(), customerDTO.getId());

            if (callSupportRepository.findIfSlotBookedIsUnique(productsOfCustomerDTO.getId(), slotOnDateDTO.getId()) != null) {
                throw new BookSlotException("Slot already booked by user");
            }

            callSupportDTO.setProductCustomerId(productsOfCustomerDTO.getId());
            callSupportDTO = callSupportRepository.save(callSupportDTO);

            slotOnDateDTO.setBookedCount(slotOnDateDTO.getBookedCount() + 1);
            log.info("Saving slotOnDate :: {}",slotOnDateDTO.toString());
            slotOnDateRepository.save(slotOnDateDTO);
            sendEmailNotification(callSupportDTO, CallStatus.Booked);
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
            ifSlotCanBeBookedAndCancelled(callId);

            CallSupportDTO callSupportDTO = callSupportRepository.findByid(callId);
            callSupportDTO.setCallStatus(CallStatus.Cancelled.name());
            callSupportRepository.save(callSupportDTO);

            SlotOnDateDTO slotOnDateDTO = slotOnDateRepository.findbyid(callSupportDTO.getDateSlotId());
            slotOnDateDTO.setBookedCount(slotOnDateDTO.getBookedCount() - 1);
            slotOnDateRepository.save(slotOnDateDTO);

            sendEmailNotification(callSupportDTO, CallStatus.Cancelled);
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


    private void ifSlotCanBeBookedAndCancelled(BigInteger callId) throws CancelSlotException {
        try {
            String time = utility.getDateStringFromDate(utility.getFutureTime(0), timeFormat);
            String future = utility.getDateStringFromDate(utility.getFutureTime(2), timeFormat);
            String today = utility.getDateStringFromDate(utility.getFutureDate(0), dateFormat);

            CallSupportDTO callSupportDTO = callSupportRepository.findByid(callId);
            SlotOnDateDTO slotOnDateDTO = slotOnDateRepository.findbyid(callSupportDTO.getDateSlotId());

            if(slotOnDateDTO.getBookedCount()==0) {
                throw new CancelSlotException("Slot already empty,cant cancel!!");
            }

            if (utility.getDateStringFromDate(slotOnDateDTO.getDate(), dateFormat).equals(today)) {
                SlotDTO slotDTO = slotRepository.findBySlotId(slotOnDateDTO.getSlotId());
                List<SlotDTO> slotList = slotRepository.findByTime(time, future);

                for (SlotDTO slotValue : slotList) {
                    if (slotDTO.getStartTime().equals(slotValue.getStartTime()) && slotDTO.getEndTime().equals(slotValue.getEndTime())) {
                        throw new CancelSlotException("Customer cannot cancel the slot!!");
                    }
                }

            }
        }
        catch (CancelSlotException ex){
            log.error(ex.getMessage());
            throw ex;
        }catch (Exception ex){
            String errorMessage = String.format("Some exception occured while checking for cancelling slot.. with error ====> %s",ex.getMessage());
            log.error(errorMessage);
            throw new CancelSlotException(errorMessage);
        }
    }

    //generate contents for emailNotification
    private void sendEmailNotification(CallSupportDTO callSupportDTO,CallStatus callStatus) throws EmailNotificationException {
        try {
            SlotOnDateDTO slotOnDateDTO = slotOnDateRepository.findbyid(callSupportDTO.getDateSlotId());
            SlotDTO slotDTO = slotRepository.findBySlotId(slotOnDateDTO.getSlotId());

            ProductsOfCustomerDTO productsOfCustomerDTO = productsOfCustomerRepository.findByid(callSupportDTO.getProductCustomerId());
            ProductDTO productDTO = productRepository.findByid(productsOfCustomerDTO.getProductId());
            CustomerDTO customerDTO = customerRepository.findByid(productsOfCustomerDTO.getCustomerId());

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
                String body = String.format(bookingConfirmationBody, slotDTO.getStartTime() + "-" + slotDTO.getEndTime(), productDTO.getName(), utility.getDateStringFromDate(date, dateFormat));
                notificationService.sendEmail(customerDTO.getEmail(), bookingConfirmationSubject, body);
                return;
            }
            if (callStatus.equals(CallStatus.Cancelled)) {
                String body = String.format(bookingCancellationBody, slotDTO.getStartTime() + "-" + slotDTO.getEndTime().toString(), productDTO.getName(), utility.getDateStringFromDate(date, dateFormat));
                notificationService.sendEmail(customerDTO.getEmail(), bookingCancellationSubject, body);
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
