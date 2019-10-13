package com.techSupport.intuitiveTechSupportapi.respositoryHandler;

import com.techSupport.intuitiveTechSupportapi.model.ProductsOfCustomerDTO;
import com.techSupport.intuitiveTechSupportapi.repository.ProductsOfCustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

@Component
public class ProductOfCustomerRepoHandler {

    @Autowired
    private ProductsOfCustomerRepo productsOfCustomerRepository;

    public ProductsOfCustomerDTO findByProductIdAndCustomerId(BigInteger productId,BigInteger customerId){
        return productsOfCustomerRepository.findByProductIdAndCustomerId(productId,customerId);
    }

    public ProductsOfCustomerDTO findById(BigInteger id){
        return productsOfCustomerRepository.findByid(id);
    }

    public List<ProductsOfCustomerDTO> findByCustomerId(BigInteger customerId){
        return productsOfCustomerRepository.findByCustomerId(customerId);
    }

    public ProductsOfCustomerDTO saveProductOfCustomer(ProductsOfCustomerDTO productsOfCustomerDTO){
        return productsOfCustomerRepository.save(productsOfCustomerDTO);
    }
}
