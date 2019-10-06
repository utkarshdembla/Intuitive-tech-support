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
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name="slots")
public class SlotDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private BigInteger id;

    @NotNull
    @Column(name="start_time")
    private Date startTime;

    @NotNull
    @Column(name="end_time")
    private Date endTime;

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
