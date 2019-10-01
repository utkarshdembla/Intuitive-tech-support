package com.techSupport.intuitiveTechSupportapi.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @JsonProperty(value = "firstname")
    private String firstName;

    @JsonProperty(value = "lastname")
    private String lastName;

    @JsonProperty(value = "phonenumber")
    private String phoneNumber;

    @JsonProperty(value = "email")
    private String email;
}
