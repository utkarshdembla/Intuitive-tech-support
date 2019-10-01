package com.techSupport.scheduler.intuitiveTechSupportscheduler.repository;

import com.techSupport.scheduler.intuitiveTechSupportscheduler.model.SlotDTO;
import com.techSupport.scheduler.intuitiveTechSupportscheduler.model.SlotOnDateDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public interface SlotOnDateRepository extends JpaRepository<SlotOnDateDTO, BigInteger> {

    @Query(value = "select * from date_slots where date = ?1 AND slot_id = ?2 ",nativeQuery = true)
    SlotOnDateDTO findbyDateAndSlotId(Date date, BigInteger id);

    @Query(value = "select * from date_slots where id = ?1",nativeQuery = true)
    SlotOnDateDTO findbyid(BigInteger id);

    @Query(value = "select * from date_slots where date = ?1",nativeQuery = true)
    List<SlotOnDateDTO> findBydate(String date);

    @Query(value = "select * from date_slots where date = ?1 AND booked_count < ?2",nativeQuery = true)
    List<SlotOnDateDTO> findAvailableSlots(Date date,int maxCount);

    @Override
    void deleteById(BigInteger bigInteger);
}
