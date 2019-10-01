package com.techSupport.intuitiveTechSupportapi.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleCall {

    @JsonProperty(value = "user_phone")
    private String userPhoneNumber;

    @JsonProperty(value = "product_code")
    private String productCode;

    @JsonProperty(value="date")
    private String date;

    @JsonProperty(value="slot_id")
    private BigInteger slotId;

}
