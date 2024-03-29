package com.techSupport.intuitiveTechSupportapi.repository;

import com.techSupport.intuitiveTechSupportapi.model.ProductDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface ProductRepo extends JpaRepository<ProductDTO, BigInteger> {

    ProductDTO findByProductCode(String name);
    ProductDTO findByid(BigInteger id);
}
