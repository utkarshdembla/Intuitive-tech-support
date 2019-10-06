package com.techSupport.intuitiveTechSupportapi.entity.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.sql.Time;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CallInfo {

    @JsonProperty("call_status")
    private String callStatus;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("email_id")
    private String emailId;

    @JsonProperty("phone")
    private String phoneNumber;

    @JsonProperty("product_code")
    private String productCode;

    @JsonProperty("start_time")
    private String startTime;

    @JsonProperty("end_time")
    private String endTime;

    @JsonProperty("date")
    private String date;

    @JsonProperty("trainer_id")
    private BigInteger trainerId;

}
