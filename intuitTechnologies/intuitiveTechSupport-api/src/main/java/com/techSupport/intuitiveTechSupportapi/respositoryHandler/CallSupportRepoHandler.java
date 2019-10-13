package com.techSupport.intuitiveTechSupportapi.respositoryHandler;

import com.techSupport.intuitiveTechSupportapi.model.CallSupportDTO;
import com.techSupport.intuitiveTechSupportapi.model.CustomerDTO;
import com.techSupport.intuitiveTechSupportapi.repository.CallSupportRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

@Component
public class CallSupportRepoHandler {

    @Autowired
    private CallSupportRepo callSupportRepo;

    public CallSupportDTO findById(BigInteger id){
        return callSupportRepo.findByid(id);
    }

    public CallSupportDTO findByCallIdTrainerIdCallStatus(BigInteger callId,BigInteger trainerId,String status){
        return callSupportRepo.findByCallIdTrainerIdCallStatus(callId,trainerId,status);
    }

    public List<CallSupportDTO> findAllByDateSlotId(BigInteger dateSlotId){
        return callSupportRepo.findAllByDateSlotId(dateSlotId);
    }

    public List<CallSupportDTO> findAllByProductofCustomerId(BigInteger productCustomerId){
        return callSupportRepo.findAllByProductOfCustomerId(productCustomerId);
    }

    public List<CallSupportDTO> findAll(){
        return callSupportRepo.findAll();
    }

    public CallSupportDTO saveCallSupport(CallSupportDTO callSupportDTO){
        return callSupportRepo.save(callSupportDTO);
    }
}
