package com.techSupport.intuitiveTechSupportapi.service;

import com.techSupport.intuitiveTechSupportapi.appConstants.Constants;
import com.techSupport.intuitiveTechSupportapi.entity.ResponseEntity.CallInfo;
import com.techSupport.intuitiveTechSupportapi.exceptions.DataGenerationException;
import com.techSupport.intuitiveTechSupportapi.exceptions.EntityNotFoundException;
import com.techSupport.intuitiveTechSupportapi.model.*;
import com.techSupport.intuitiveTechSupportapi.repository.*;
import com.techSupport.intuitiveTechSupportapi.utility.DateUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

@Slf4j
@Service
public class CallSupportService {

    @Value(Constants.maximumCallsInSlots)
    private int maxCallsInSlots;

    @Value(Constants.dateFormat)
    private String dateFormat;

    @Value(Constants.timeFormat)
    private String timeFormat;

    @Autowired
    private DateUtility utility;

    @Autowired
    private SlotOnDateRepository slotOnDateRepository;

    @Autowired
    private CallSupportRepository callSupportRepository;

    @Autowired
    private ProductsOfCustomerRepository productsOfCustomerRepository;

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;


    public List<CallInfo> getAllCallsInfoByDate(String date) throws DataGenerationException, EntityNotFoundException, ParseException {

            log.info("Generating all calls on date ==> {}",date);
            Date inputDate = utility.getDateFromDateString(date, dateFormat);
            List<SlotOnDateDTO> listOfSlotOnDate = slotOnDateRepository.findBydate(inputDate);
            log.info("Slots on data data received ==> {}",listOfSlotOnDate.toString());

            List<CallSupportDTO> listCallSupportDTO = new ArrayList<>();

            for (SlotOnDateDTO value : listOfSlotOnDate) {
                listCallSupportDTO.addAll(callSupportRepository.findAllByDateSlotId(value.getId()));
            }

            log.info("Call Support Data information ==> {}", listCallSupportDTO.toString());
            return getCallInfoList(listCallSupportDTO);

    }


    public List<CallInfo> getAllCallsInfoByName(String searchString) throws EntityNotFoundException, DataGenerationException {

            log.info("Generating all calls for customer..");
            CustomerDTO customerDTO = customerService.getCustomer(searchString);
            log.info("Customer date received ==> {}", customerDTO.toString());
            List<ProductsOfCustomerDTO> listProductsOfCustomer = productsOfCustomerRepository.findByCustomerId(customerDTO.getId());
            List<CallSupportDTO> listCallSupportDTO = new ArrayList<>();

            for (ProductsOfCustomerDTO value : listProductsOfCustomer) {
                listCallSupportDTO.addAll(callSupportRepository.findAllByProductOfCustomerId(value.getId()));
            }

            log.info("Call Support Data information ==> {}", listCallSupportDTO.toString());
            return getCallInfoList(listCallSupportDTO);

    }

    public List<CallInfo> getAllCalls() throws DataGenerationException, EntityNotFoundException {
            return getCallInfoList(callSupportRepository.findAll());
    }



    private List<CallInfo> getCallInfoList(List<CallSupportDTO> listCallSupportDTO) throws EntityNotFoundException, DataGenerationException {
        try {
            log.info("Generating call records...");
            List<CallInfo> lisCallInfo = new ArrayList<>();

            if (listCallSupportDTO.isEmpty()) {
                log.info("No call data has been received..");
                throw new EntityNotFoundException("No data received from the database");
            }

            for (CallSupportDTO value : listCallSupportDTO) {
                lisCallInfo.add(getCallInfo(value));
            }

            log.info("List of call records :: {}",lisCallInfo.toString());
            return lisCallInfo;
        }
        catch (EntityNotFoundException ex){throw ex;}
        catch (Exception ex){
            String errorMessage = "Not able to generate date from call records";
            log.error(errorMessage);
            throw new DataGenerationException(errorMessage);
        }
    }

    private CallInfo getCallInfo(CallSupportDTO callSupportDTO) throws DataGenerationException {
        try{
            CallInfo callInfo = new CallInfo();
            ProductsOfCustomerDTO productsOfCustomerDTO =  productsOfCustomerRepository.findByid(callSupportDTO.getProductCustomerId());
            SlotOnDateDTO slotOnDateDTO = slotOnDateRepository.findbyid(callSupportDTO.getDateSlotId());

            CustomerDTO customerDTO = customerService.getCustomer(productsOfCustomerDTO);
            ProductDTO productDTO = productService.getProduct(productsOfCustomerDTO);
            SlotDTO slotDTO = slotRepository.findBySlotId(slotOnDateDTO.getSlotId());

            callInfo.setCallStatus(callSupportDTO.getCallStatus());
            callInfo.setEmailId(customerDTO.getEmail());
            callInfo.setPhoneNumber(customerDTO.getPhoneNumber());
            callInfo.setFirstName(customerDTO.getFirstName());
            callInfo.setLastName(customerDTO.getLastName());
            callInfo.setStartTime(utility.getDateStringFromDate(slotDTO.getStartTime(),timeFormat));
            callInfo.setEndTime(utility.getDateStringFromDate(slotDTO.getEndTime(),timeFormat));
            callInfo.setProductCode(productDTO.getProductCode());
            callInfo.setDate(utility.getDateStringFromDate(slotOnDateDTO.getDate(),dateFormat));
            callInfo.setTrainerId(callSupportDTO.getTrainerId());

            log.info("Returning call information :: {}",callInfo.toString());
            return callInfo;
        }
        catch (Exception ex) {
            String errorMessage = "Some error occured while getting/converting data to response format";
            log.error(errorMessage);
            throw new DataGenerationException(errorMessage);
        }
    }

}
