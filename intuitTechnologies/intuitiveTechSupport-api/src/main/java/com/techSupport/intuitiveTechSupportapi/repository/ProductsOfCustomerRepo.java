package com.techSupport.intuitiveTechSupportapi.repository;

import com.techSupport.intuitiveTechSupportapi.model.ProductsOfCustomerDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface ProductsOfCustomerRepo extends JpaRepository<ProductsOfCustomerDTO, BigInteger> {

    ProductsOfCustomerDTO findByProductIdAndCustomerId(BigInteger pId,BigInteger cId);

    @Query(value = "select * from products_of_customer where id = ?1",nativeQuery = true)
    ProductsOfCustomerDTO findByid(BigInteger id);

    @Query(value = "select * from products_of_customer where customer_id = ?1",nativeQuery = true)
    List<ProductsOfCustomerDTO> findByCustomerId(BigInteger cId);
}
