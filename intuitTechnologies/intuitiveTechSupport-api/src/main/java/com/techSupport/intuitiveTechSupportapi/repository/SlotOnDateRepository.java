package com.techSupport.intuitiveTechSupportapi.repository;

import com.techSupport.intuitiveTechSupportapi.model.SlotDTO;
import com.techSupport.intuitiveTechSupportapi.model.SlotOnDateDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Repository
public interface SlotOnDateRepository extends CrudRepository<SlotOnDateDTO, BigInteger> {


    @Query(value = "select * from date_slots where date = ?1 AND slot_id = ?2 ",nativeQuery = true)
    SlotOnDateDTO findbyDateAndSlotId(Date date, BigInteger id);

    @Query(value = "select * from date_slots where id = ?1",nativeQuery = true)
    SlotOnDateDTO findbyid(BigInteger id);

    List<SlotOnDateDTO> findBydate(Date date);

    /*@Query(value = "select * from date_slots where date = ?1 AND booked_count < ?2",nativeQuery = true)
    List<SlotOnDateDTO> findAvailableSlots(Date date,int maxCount);*/

    @Query(value = "select * from slots where date = ?1 AND start_time after ?2",nativeQuery = true)
    List<SlotOnDateDTO> findByTime(String futureTime);

}
