package com.techSupport.intuitiveTechSupportapi.repository;

import com.techSupport.intuitiveTechSupportapi.model.CallSupportDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface CallSupportRepository extends JpaRepository<CallSupportDTO, BigInteger> {

    CallSupportDTO findByid(BigInteger id);

    @Query(value = "select * from call_support where id = ?1 AND call_status = ?2 ",nativeQuery = true)
    CallSupportDTO findByidAndcallStatus(BigInteger id, String status);

    @Query(value = "select * from call_support where date_slot_id = ?1",nativeQuery = true)
    List<CallSupportDTO> findAllByDateSlotId(BigInteger dateSlotId);

    @Query(value = "select * from call_support where product_customer_id = ?1",nativeQuery = true)
    List<CallSupportDTO> findAllByProductOfCustomerId(BigInteger productCustomerId);




}
