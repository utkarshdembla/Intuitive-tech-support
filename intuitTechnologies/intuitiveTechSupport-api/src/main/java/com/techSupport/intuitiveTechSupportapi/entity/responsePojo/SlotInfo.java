package com.techSupport.intuitiveTechSupportapi.entity.responsePojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SlotInfo {

    @JsonProperty("date_slot_id")
    private BigInteger dateSlotId;

    @JsonProperty("slot_id")
    private BigInteger slotId;

    @JsonProperty("start_time")
    private String startTime;

    @JsonProperty("end_time")
    private String endTime;

    @JsonProperty("date")
    private String date;

    @JsonProperty("slots_booked")
    private int slotsBooked;

    @Enumerated(EnumType.STRING)
    @JsonProperty("slot_status")
    private EnumSlotAvailability slotStatus;
}
