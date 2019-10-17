package com.techSupport.intuitiveTechSupportapi.service;

import com.techSupport.intuitiveTechSupportapi.appConstants.Constants;
import com.techSupport.intuitiveTechSupportapi.entity.responsePojo.EnumSlotAvailability;
import com.techSupport.intuitiveTechSupportapi.entity.responsePojo.SlotInfo;
import com.techSupport.intuitiveTechSupportapi.exceptions.DataGenerationException;
import com.techSupport.intuitiveTechSupportapi.exceptions.EntityNotFoundException;
import com.techSupport.intuitiveTechSupportapi.model.SlotDTO;
import com.techSupport.intuitiveTechSupportapi.model.SlotOnDateDTO;
import com.techSupport.intuitiveTechSupportapi.repository.SlotOnDateRepo;
import com.techSupport.intuitiveTechSupportapi.repository.SlotRepo;
import com.techSupport.intuitiveTechSupportapi.respositoryHandler.SlotOnDateRepoHandler;
import com.techSupport.intuitiveTechSupportapi.respositoryHandler.SlotRepoHandler;
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
    private SlotRepo slotRepository;

    @Autowired
    private SlotOnDateRepo slotOnDateRepository;

    @Autowired
    private SlotRepoHandler slotRepoHandler;

    @Autowired
    private SlotOnDateRepoHandler slotOnDateRepoHandler;

    @Autowired
    private DateUtility utility;

    @Value(Constants.dateFormat)
    private String dateFormat;

    @Value(Constants.timeFormat)
    private String timeFormat;

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

                for (SlotDTO value : slotRepoHandler.findAllSlots()) {
                    Date startTime = utility.getDateWithTime(value.getStartTime());
                    Date currentTime = utility.getDateWithTime(null);
                    if (startTime.before(currentTime))
                        listOfSlotsBeforeTime.remove(value);
                }

                for(SlotDTO value :listOfSlotsBeforeTime){
                    SlotOnDateDTO slotOnDateDTO = slotOnDateRepoHandler.findByDateAndSlotId(inputDate,value.getId());
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
        return slotOnDateRepoHandler.findBydate(date);
    }

    public List<SlotDTO> getAllSlots(){ return slotRepoHandler.findAllSlots(); }

    private List<SlotInfo> getListSlotInfo(List<SlotOnDateDTO> listSlotOnDateDTO) throws DataGenerationException {
        try {
            List<SlotInfo> listSoltInfo = new ArrayList<>();
            for (SlotOnDateDTO value : listSlotOnDateDTO) {
                listSoltInfo.add(getSlotInfo(value));
            }
            log.info("Slots data retreived :: {}",listSoltInfo.toString());
            return listSoltInfo;
        }
        catch (Exception ex){
            String errorMessage = "Some error occured while generating list of slots..";
            log.error(errorMessage);
            throw new DataGenerationException(errorMessage);
        }
    }

    private SlotInfo getSlotInfo(SlotOnDateDTO slotOnDateDTO) throws DataGenerationException {
        try {
            SlotDTO slotDTO = slotRepoHandler.findBySlotId(slotOnDateDTO.getSlotId());

            if(slotDTO==null)
                throw new EntityNotFoundException("No slot found");

            SlotInfo slotInfo = new SlotInfo();
            slotInfo.setDateSlotId(slotOnDateDTO.getId());
            slotInfo.setSlotId(slotDTO.getId());
            slotInfo.setSlotsBooked(slotOnDateDTO.getBookedCount());
            slotInfo.setStartTime(utility.getDateStringFromDate(slotDTO.getStartTime(), timeFormat));
            slotInfo.setEndTime(utility.getDateStringFromDate(slotDTO.getEndTime(), timeFormat));
            slotInfo.setDate(utility.getDateStringFromDate(slotOnDateDTO.getDate(), dateFormat));

            if(slotOnDateDTO.getBookedCount()<maxCallsInSlots)
                slotInfo.setSlotStatus(EnumSlotAvailability.AVAILABLE);
            else
                slotInfo.setSlotStatus(EnumSlotAvailability.FULL);

            return slotInfo;
        }
        catch(Exception ex)
        {
            String errorMessage = "Not able to generate the slots info data";
            log.error(errorMessage);
            throw new DataGenerationException(errorMessage);
        }
    }

    public SlotOnDateDTO updateSlotOnBookingCancellation(SlotOnDateDTO slotOnDateDTO,int count){
        slotOnDateDTO.setBookedCount(slotOnDateDTO.getBookedCount() + count);
        return slotOnDateDTO;

    }
}
