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
@Table(name="product")
public class ProductDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private BigInteger id;

    @NotNull
    @Column(name="product_code")
    private String productCode;

    @NotNull
    @Column(name="name")
    private String name;

    @NotNull
    @Column(name="cost")
    private Double cost;

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
