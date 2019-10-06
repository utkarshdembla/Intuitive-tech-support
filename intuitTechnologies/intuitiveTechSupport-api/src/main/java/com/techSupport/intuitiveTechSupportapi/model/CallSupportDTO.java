package com.techSupport.intuitiveTechSupportapi.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name="call_support")
public class CallSupportDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private BigInteger id;

    @NotNull
    @Column(name="product_customer_id")
    private BigInteger productCustomerId;

    @NotNull
    @Column(name="date_slot_id")
    private BigInteger dateSlotId;

    @Column(name="trainer_id")
    private BigInteger trainerId;

    @NotNull
    @Column(name="call_status")
    private String callStatus=CallStatus.Booked.name();

    @Column(nullable = false, updatable = false, name = "created_at")
    @Temporal(TemporalType.DATE)
    @CreatedDate
    @JsonIgnore
    private Date createdAt;

    @Column(nullable = false, name = "updated_at")
    @Temporal(TemporalType.DATE)
    @LastModifiedDate
    @JsonIgnore
    private Date updatedAt;

}