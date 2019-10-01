package com.techSupport.intuitiveTechSupportapi.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @JsonProperty(value = "product_code")
    private String productCode;

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "cost")
    private Double cost;
}
