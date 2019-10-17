package com.techSupport.intuitiveTechSupportapi.respositoryHandler;

import com.techSupport.intuitiveTechSupportapi.model.SlotOnDateDTO;
import com.techSupport.intuitiveTechSupportapi.repository.SlotOnDateRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Component
public class SlotOnDateRepoHandler {

    @Autowired
    private SlotOnDateRepo slotOnDateRepo;

    public SlotOnDateDTO findByDateAndSlotId(Date date, BigInteger slotId){
        return slotOnDateRepo.findbyDateAndSlotId(date,slotId);
    }

    public SlotOnDateDTO findbyId(BigInteger id){
        return slotOnDateRepo.findbyid(id);
    }

    public List<SlotOnDateDTO> findBydate(Date date){
        return slotOnDateRepo.findBydate(date);
    }

    public SlotOnDateDTO saveSlotOnDate(SlotOnDateDTO slotOnDateDTO){
        return slotOnDateRepo.save(slotOnDateDTO);
    }
}
