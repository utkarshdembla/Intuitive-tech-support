package com.techSupport.intuitiveTechSupportapi.repository;

import com.techSupport.intuitiveTechSupportapi.model.CustomerDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerDTO, BigInteger> {
    CustomerDTO findByPhoneNumber(String phonenumber);
    CustomerDTO findByEmail(String email);
    CustomerDTO findByid(BigInteger id);

}
