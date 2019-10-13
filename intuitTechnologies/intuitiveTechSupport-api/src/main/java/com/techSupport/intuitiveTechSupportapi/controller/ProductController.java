package com.techSupport.intuitiveTechSupportapi.controller;


import com.techSupport.intuitiveTechSupportapi.entity.requestPojo.Product;
import com.techSupport.intuitiveTechSupportapi.exceptions.EntitySaveException;
import com.techSupport.intuitiveTechSupportapi.model.ProductDTO;
import com.techSupport.intuitiveTechSupportapi.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/techsupport/product")
public class ProductController {

   @Autowired
   private ProductService productService;

    @GetMapping("/v1/getProduct/{productCode}")
    public ProductDTO getProduct(@PathVariable("productCode") String productCode) {
        return  productService.getProduct(productCode);
    }

    @PostMapping("/v1/addProduct")
    public ProductDTO addProduct(@RequestBody Product product) throws EntitySaveException {
        return productService.addProduct(product);
    }
}
