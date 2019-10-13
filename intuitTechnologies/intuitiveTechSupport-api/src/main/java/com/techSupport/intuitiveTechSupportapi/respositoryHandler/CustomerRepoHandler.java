package com.techSupport.intuitiveTechSupportapi.respositoryHandler;

import com.techSupport.intuitiveTechSupportapi.model.CustomerDTO;
import com.techSupport.intuitiveTechSupportapi.repository.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class CustomerRepoHandler {

    @Autowired
    private CustomerRepo customerRepository;

    public CustomerDTO findById(BigInteger customerId){
        return customerRepository.findByid(customerId);
    }

    public CustomerDTO findByPhoneNumber(String phonenumber){
        return customerRepository.findByPhoneNumber(phonenumber);
    }

    public CustomerDTO findByEmail(String email){
        return customerRepository.findByEmail(email);
    }

    public CustomerDTO saveCustomer(CustomerDTO customerDTO){
        return customerRepository.save(customerDTO);
    }
}
