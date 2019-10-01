package com.techSupport.scheduler.intuitiveTechSupportscheduler.repository;

import com.techSupport.scheduler.intuitiveTechSupportscheduler.model.ProductsOfCustomerDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface ProductsOfCustomerRepository extends JpaRepository<ProductsOfCustomerDTO, BigInteger> {

    ProductsOfCustomerDTO findByProductIdAndCustomerId(BigInteger pId,BigInteger cId);

    @Query(value = "select * from products_of_customer where id = ?1",nativeQuery = true)
    ProductsOfCustomerDTO findByid(BigInteger id);
}
