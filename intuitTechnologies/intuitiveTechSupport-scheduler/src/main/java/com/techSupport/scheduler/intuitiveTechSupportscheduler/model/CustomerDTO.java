package com.techSupport.scheduler.intuitiveTechSupportscheduler.model;

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
@Table(name="customer")
public class CustomerDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private BigInteger id;

    @NotNull
    @Column(name="firstname")
    private String firstName;

    @NotNull
    @Column(name="lastname")
    private String lastName;

    @NotNull
    @Column(name="email")
    private String email;

    @NotNull
    @Column(name="phonenumber")
    private String phoneNumber;

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
