package com.techSupport.intuitiveTechSupportapi.service;

import com.techSupport.intuitiveTechSupportapi.exceptions.BookSlotException;
import com.techSupport.intuitiveTechSupportapi.model.CallStatus;
import com.techSupport.intuitiveTechSupportapi.model.CallSupportDTO;
import com.techSupport.intuitiveTechSupportapi.repository.CallSupportRepository;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigInteger;

@Slf4j
@Service
public class TrainerService {

    @Autowired
    private CallSupportRepository callSupportRepository;

    public CallSupportDTO bookSession(BigInteger id) throws BookSlotException {
       try {
           CallSupportDTO callSupportDTO = callSupportRepository.findByidAndcallStatus(id, CallStatus.Booked.name());
           if (callSupportDTO != null) {
               callSupportDTO.setCallStatus(CallStatus.Started.name());
               return callSupportRepository.save(callSupportDTO);
           } else {
               String errorMessage = "Not available for booking session";
               throw new BookSlotException(errorMessage);
           }
       }catch (BookSlotException ex) {
           log.error(ex.getMessage());
           throw ex;
       }catch (Exception ex)
       {
           String errorMessage = "Not available for booking session";
           log.error(errorMessage);
           throw new BookSlotException(errorMessage);
       }
    }

    public CallSupportDTO completeSession(BigInteger id) throws BookSlotException {
        try {
            CallSupportDTO callSupportDTO = callSupportRepository.findByidAndcallStatus(id, CallStatus.Started.name());
            if (callSupportDTO != null) {
                callSupportDTO.setCallStatus(CallStatus.Completed.name());
                return callSupportRepository.save(callSupportDTO);
            } else {
                String errorMessage = "Not available for completing session";
                throw new BookSlotException(errorMessage);
            }
        }catch (BookSlotException ex) {
            log.error(ex.getMessage());
            throw ex;
        }catch (Exception ex)
        {
            String errorMessage = "Not available for completing session";
            log.error(errorMessage);
            throw new BookSlotException(errorMessage);
        }
    }
}