package com.techSupport.intuitiveTechSupportapi.respositoryHandler;

import com.techSupport.intuitiveTechSupportapi.model.ProductDTO;
import com.techSupport.intuitiveTechSupportapi.repository.ProductRepo;
import com.techSupport.intuitiveTechSupportapi.repository.ProductsOfCustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class ProductRepoHandler {


    @Autowired
    private ProductRepo productRepository;

    @Autowired
    private ProductsOfCustomerRepo productsOfCustomerRepository;

    public ProductDTO findByProductCode(String code){
        return productRepository.findByProductCode(code);
    }

    public ProductDTO findByProductId(BigInteger productId){
        return productRepository.findByid(productId);
    }

    /*
    * ProductsOfCustomerDTO findByProductIdAndCustomerId(BigInteger pId,BigInteger cId);

    @Query(value = "select * from products_of_customer where id = ?1",nativeQuery = true)
    ProductsOfCustomerDTO findByid(BigInteger id);

    @Query(value = "select * from products_of_customer where customer_id = ?1",nativeQuery = true)
    List<ProductsOfCustomerDTO> findByCustomerId(BigInteger cId);
    * */
    public ProductDTO saveProduct(ProductDTO productDTO){
        return productRepository.save(productDTO);
    }

}
