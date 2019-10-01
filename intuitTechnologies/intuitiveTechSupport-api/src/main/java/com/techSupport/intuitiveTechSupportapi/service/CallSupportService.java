package com.techSupport.intuitiveTechSupportapi.service;

import com.techSupport.intuitiveTechSupportapi.appConstants.Constants;
import com.techSupport.intuitiveTechSupportapi.exceptions.EntityNotFoundException;
import com.techSupport.intuitiveTechSupportapi.model.CallStatus;
import com.techSupport.intuitiveTechSupportapi.model.CallSupportDTO;
import com.techSupport.intuitiveTechSupportapi.model.SlotOnDateDTO;
import com.techSupport.intuitiveTechSupportapi.repository.CallSupportRepository;
import com.techSupport.intuitiveTechSupportapi.repository.SlotOnDateRepository;
import com.techSupport.intuitiveTechSupportapi.util.DateUtility;
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

    @Autowired
    private DateUtility utility;

    @Autowired
    private SlotOnDateRepository slotOnDateRepository;

    @Autowired
    private CallSupportRepository callSupportRepository;

    public List<SlotOnDateDTO> getAllAvailableSlots(String date) throws EntityNotFoundException {
        try {
            Date targetDate = utility.getDateFromDateString(date, dateFormat);
            return slotOnDateRepository.findAvailableSlots(targetDate, maxCallsInSlots);
        }
        catch (ParseException ex) {
            throw new EntityNotFoundException(ex.getMessage());
        }
        catch (Exception ex) {
            String error = String.format("Entity not found ==> %s",ex.getMessage());
            log.error(error);
            throw new EntityNotFoundException(ex.getMessage());
        }
    }

    public List<SlotOnDateDTO> getAllSlotsOnDate(String date) throws EntityNotFoundException {
        try {
            Date targetDate = utility.getDateFromDateString(date, dateFormat);
            return slotOnDateRepository.findBydate(targetDate);
        }catch (ParseException ex) {
            throw new EntityNotFoundException(ex.getMessage());
        }
        catch (Exception ex) {
            String error = String.format("Entity not found ==> %s",ex.getMessage());
            log.error(error);
            throw new EntityNotFoundException(ex.getMessage());
        }
    }

    public List<CallSupportDTO> getAllCalls() {
            return callSupportRepository.findAll();
    }

    public List<CallSupportDTO> getAllBookedSlots(String date) throws EntityNotFoundException {
        try {
            Date targetDate = utility.getDateFromDateString(date, dateFormat);
            List<SlotOnDateDTO> list = slotOnDateRepository.findBydate(targetDate);
            List<CallSupportDTO> listCallSupportDTO = new ArrayList<>();
            for (SlotOnDateDTO slotDateValue : list) {
                List<CallSupportDTO> callSupportDTO = callSupportRepository.findByDateSlotIdAndcallStatus(slotDateValue.getId(), CallStatus.Booked.name());
                if (callSupportDTO != null)
                    listCallSupportDTO.addAll(callSupportDTO);
            }

            return listCallSupportDTO;
        }catch (ParseException ex) {
            throw new EntityNotFoundException(ex.getMessage());
        }
        catch (Exception ex) {
            String error = String.format("Entity not found ==> %s",ex.getMessage());
            log.error(error);
            throw new EntityNotFoundException(ex.getMessage());
        }
    }
}
