package com.techSupport.intuitiveTechSupportapi.repository;

import com.techSupport.intuitiveTechSupportapi.model.SlotDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface SlotRepository extends JpaRepository<SlotDTO, BigInteger> {

    @Query(value = "select * from slots where id = ?1",nativeQuery = true)
    SlotDTO findBySlotId(BigInteger id);

    @Query(value = "select * from slots where start_time between ?1 AND ?2",nativeQuery = true)
    List<SlotDTO> findByTime(String time, String endTime);

  /*  @Query(value = "select * from slots where start_time > ?2",nativeQuery = true)
    List<SlotDTO> findByTime(String time, String endTime);*/

    @Query(value = "select * from slots where start_time > ?2",nativeQuery = true)
    List<SlotDTO> findSlotsToBook( String endTime);


}
