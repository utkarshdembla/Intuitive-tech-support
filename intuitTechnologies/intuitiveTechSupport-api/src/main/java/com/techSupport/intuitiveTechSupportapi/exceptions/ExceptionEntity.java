package com.techSupport.intuitiveTechSupportapi.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionEntity {

    @JsonProperty(value = "error")
    private String errorName;

    @JsonProperty(value = "message")
    private String message;
}
