package com.techSupport.intuitiveTechSupportapi.service;

import com.techSupport.intuitiveTechSupportapi.appConstants.Constants;
import com.techSupport.intuitiveTechSupportapi.entity.ResponseEntity.EnumSlotAvailability;
import com.techSupport.intuitiveTechSupportapi.entity.ResponseEntity.SlotInfo;
import com.techSupport.intuitiveTechSupportapi.exceptions.EntityNotFoundException;
import com.techSupport.intuitiveTechSupportapi.model.SlotDTO;
import com.techSupport.intuitiveTechSupportapi.model.SlotOnDateDTO;
import com.techSupport.intuitiveTechSupportapi.repository.SlotOnDateRepository;
import com.techSupport.intuitiveTechSupportapi.repository.SlotRepository;
import com.techSupport.intuitiveTechSupportapi.utility.DateUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class SlotService {

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private SlotOnDateRepository slotOnDateRepository;

    @Autowired
    private DateUtility utility;

    @Value(Constants.dateFormat)
    private String dateFormat;

    @Value(Constants.maximumCallsInSlots)
    private int maxCallsInSlots;
    /*
     * 1)If time has passed by, dont show that slot on the current date
     * 2)Dont show the slots if they are booked
     * */
    public List<SlotInfo> getAllAvailableSlots(String date) throws EntityNotFoundException {
        try {
            log.info("Generating list of slots available for date :: {}",utility.getDateFromDateString(date,dateFormat));
            Date inputDate = utility.getDateFromDateString(date, dateFormat);
            Date currentDate = utility.getDateFromDateString(utility.getDateStringFromDate(utility.getFutureDate(0),dateFormat),dateFormat);

            List<SlotOnDateDTO> listOfSlotsOnDate = new ArrayList<>();
            List<SlotDTO> listOfSlotsBeforeTime = getAllSlots();

            if(inputDate.equals(currentDate)) {
                log.info("Input date is found to be current date");

                for (SlotDTO value : slotRepository.findAll()) {
                    Date startTime = utility.getDateWithTime(value.getStartTime());
                    Date currentTime = utility.getDateWithTime(null);
                    if (startTime.before(currentTime))
                        listOfSlotsBeforeTime.remove(value);
                }

                for(SlotDTO value :listOfSlotsBeforeTime){
                    SlotOnDateDTO slotOnDateDTO = slotOnDateRepository.findbyDateAndSlotId(inputDate,value.getId());
                    listOfSlotsOnDate.add(slotOnDateDTO);
                }
            }
            else if(inputDate.before(currentDate)){
                throw new Exception("Input date cannot be before current date..");
            }
            else{
                listOfSlotsOnDate = getSlotForDate(inputDate);
                if(listOfSlotsOnDate.isEmpty()) {
                    String errorMessage = String.format("Slots not opened for the given input date ==> %s",date);
                    log.error(errorMessage);
                    throw new EntityNotFoundException(errorMessage);
                }
            }

            log.info("List of slots on date :: {}, slots :: {}",date,listOfSlotsOnDate.toString());
            return getListSlotInfo(listOfSlotsOnDate);

        } catch (Exception ex) {
            String error = String.format("Entity not found ==> %s",ex.getMessage());
            log.error(error);
            throw new EntityNotFoundException(ex.getMessage());
        }
    }


    public List<SlotOnDateDTO> getSlotForDate(Date date){
        return slotOnDateRepository.findBydate(date);
    }

    public List<SlotDTO> getAllSlots(){
        return slotRepository.findAll();
    }

    private List<SlotInfo> getListSlotInfo(List<SlotOnDateDTO> listSlotOnDateDTO)
    {
        try {
            List<SlotInfo> listSoltInfo = new ArrayList<>();
            for (SlotOnDateDTO value : listSlotOnDateDTO) {
                listSoltInfo.add(getSlotInfo(value));
            }
            log.info("Slots data retreived :: {}",listSoltInfo.toString());
            return listSoltInfo;
        }
        catch (Exception ex){
            log.error("Some error occured while generating list of slots..");
            throw ex;
        }
    }

    private SlotInfo getSlotInfo(SlotOnDateDTO slotOnDateDTO)
    {
        try {
            SlotDTO slotDTO = slotRepository.findBySlotId(slotOnDateDTO.getSlotId());

            SlotInfo slotInfo = new SlotInfo();
            slotInfo.setSlotsBooked(slotOnDateDTO.getBookedCount());
            slotInfo.setStartTime(utility.getDateStringFromDate(slotDTO.getStartTime(), "hh:mm"));
            slotInfo.setEndTime(utility.getDateStringFromDate(slotDTO.getEndTime(), "hh:mm"));
            slotInfo.setDate(utility.getDateStringFromDate(slotOnDateDTO.getDate(), dateFormat));

            if(slotOnDateDTO.getBookedCount()<maxCallsInSlots)
                slotInfo.setSlotStatus(EnumSlotAvailability.AVAILABLE);
            else
                slotInfo.setSlotStatus(EnumSlotAvailability.FULL);

            return slotInfo;
        }
        catch(Exception ex)
        {
            throw ex;
        }
    }
}