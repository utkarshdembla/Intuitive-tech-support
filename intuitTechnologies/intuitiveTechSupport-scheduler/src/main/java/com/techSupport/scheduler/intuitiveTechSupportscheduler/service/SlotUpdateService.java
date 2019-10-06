package com.techSupport.scheduler.intuitiveTechSupportscheduler.service;

import com.techSupport.scheduler.intuitiveTechSupportscheduler.constant.Constants;
import com.techSupport.scheduler.intuitiveTechSupportscheduler.model.SlotDTO;
import com.techSupport.scheduler.intuitiveTechSupportscheduler.model.SlotOnDateDTO;
import com.techSupport.scheduler.intuitiveTechSupportscheduler.repository.SlotOnDateRepository;
import com.techSupport.scheduler.intuitiveTechSupportscheduler.repository.SlotRepository;
import com.techSupport.scheduler.intuitiveTechSupportscheduler.utility.DateUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class SlotUpdateService {

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private SlotOnDateRepository slotOnDateRepository;

    @Value(Constants.slotDaysOpen)
    private int slotDaysOpen;

    @Value(Constants.dateFormat)
    private String dateFormat;

    @Autowired
    private DateUtility dateUtility;

    public void slotUpdate() throws ParseException {
        List<SlotDTO> slots = slotRepository.findAll();
        log.info("Deleting today's slots..");
        deleteTodaySlot();

        log.info("Adding slots..");
        addSlots(slots);

    }

    private void addSlots(List<SlotDTO> slots) throws ParseException {
        int count = 1;

        while(count<=slotDaysOpen) {

            String date = dateUtility.getDateStringFromDate(dateUtility.getFutureDate(count),dateFormat);
            if (slotOnDateRepository.findBydate(date).isEmpty()) {
                for (SlotDTO value : slots) {
                    SlotOnDateDTO slotOnDateDTO = new SlotOnDateDTO();
                    slotOnDateDTO.setSlotId(value.getId());
                    slotOnDateDTO.setDate(dateUtility.getDateFromDateString(date, dateFormat));
                    slotOnDateDTO.setBookedCount(0);
                    log.info("Adding slotOnDate :: {}",slotOnDateDTO.toString());
                    slotOnDateRepository.save(slotOnDateDTO);
                }
            }
            count++;
        }
    }

    private void deleteTodaySlot() throws ParseException {

        String date = dateUtility.getDateStringFromDate(dateUtility.getFutureDate(1),dateFormat);
        Date today = dateUtility.getDateFromDateString(date,dateFormat);

        List<SlotOnDateDTO> listSlotonDate = slotOnDateRepository.findForAllPreviousDates(today);

        if(!listSlotonDate.isEmpty()){
            for(SlotOnDateDTO slotOnDateDTO : listSlotonDate)
            {
                log.info("Deleting slot-on-date {}",slotOnDateDTO.toString());
                slotOnDateRepository.deleteById(slotOnDateDTO.getId());
            }
        }
    }

}
