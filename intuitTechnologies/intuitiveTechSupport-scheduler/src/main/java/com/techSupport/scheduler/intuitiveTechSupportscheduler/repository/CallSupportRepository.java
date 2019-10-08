package com.techSupport.scheduler.intuitiveTechSupportscheduler.repository;

import com.techSupport.scheduler.intuitiveTechSupportscheduler.model.CallSupportDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface CallSupportRepository extends JpaRepository<CallSupportDTO, BigInteger> {

    CallSupportDTO findByid(BigInteger id);

    @Query(value = "select * from call_support where date_slot_id = ?1 AND call_status = ?2 ",nativeQuery = true)
    List<CallSupportDTO> findByDateSlotIdAndcallStatus(BigInteger id, String status);

    @Query(value = "select * from call_support where date_slot_id = ?1 AND trainer_id=?2 ",nativeQuery = true)
    CallSupportDTO findByDateSlotIdAndTrainerId(BigInteger id, BigInteger trainerId);

}
