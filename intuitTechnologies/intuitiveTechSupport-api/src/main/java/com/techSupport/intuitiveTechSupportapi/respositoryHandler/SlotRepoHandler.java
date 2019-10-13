package com.techSupport.intuitiveTechSupportapi.respositoryHandler;

import com.techSupport.intuitiveTechSupportapi.model.SlotDTO;
import com.techSupport.intuitiveTechSupportapi.repository.SlotRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

@Component
public class SlotRepoHandler {

    @Autowired
    private SlotRepo slotRepository;

    public SlotDTO findBySlotId(BigInteger slotId){
        return slotRepository.findBySlotId(slotId);
    }

    public List<SlotDTO> findBetweenStartTimeEndTime(String startTime,String endTime){
        return slotRepository.findByTime(startTime,endTime);
    }

    public SlotDTO saveSlot(SlotDTO slotDTO){
        return slotRepository.save(slotDTO);
    }

    public List<SlotDTO> findAllSlots(){
        return slotRepository.findAll();
    }
}
