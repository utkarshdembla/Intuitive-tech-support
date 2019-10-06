package com.techSupport.intuitiveTechSupportapi.service;

import com.techSupport.intuitiveTechSupportapi.entity.Product;
import com.techSupport.intuitiveTechSupportapi.exceptions.EntitySaveException;
import com.techSupport.intuitiveTechSupportapi.model.ProductDTO;
import com.techSupport.intuitiveTechSupportapi.model.ProductsOfCustomerDTO;
import com.techSupport.intuitiveTechSupportapi.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.text.ParseException;

@Slf4j
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public ProductDTO getProduct(String productCode) {
        try {
            return productRepository.findByProductCode(productCode);
        }catch (Exception ex)
        {
            String error = String.format("Entity not found ==> %s",ex.getMessage());
            log.error(error);
            throw new EntityNotFoundException(ex.getMessage());
        }
    }

    public ProductDTO addProduct(Product product) throws EntitySaveException {
        try {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setProductCode(product.getProductCode());
            productDTO.setName(product.getName());
            productDTO.setCost(product.getCost());
            return saveProduct(productDTO);
        }
        catch (Exception ex) {
            String error = String.format("Entity not save ==> %s",ex.getMessage());
            log.error(error);
            throw new EntitySaveException(ex.getMessage());
        }
    }

    private ProductDTO saveProduct(ProductDTO productDTO) {
        return productRepository.save(productDTO);
    }

    public ProductDTO getProduct(ProductsOfCustomerDTO productsOfCustomerDTO) {
        return productRepository.findByid(productsOfCustomerDTO.getProductId());
    }
}
