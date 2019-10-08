package com.techSupport.scheduler.intuitiveTechSupportscheduler.repository;

import com.techSupport.scheduler.intuitiveTechSupportscheduler.model.TrainerDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface TrainerRepository extends JpaRepository<TrainerDTO, BigInteger> {
}
