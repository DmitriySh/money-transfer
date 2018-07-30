package ru.shishmakov.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@Entity
public class Account {
    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;
    private Long accNumber;
    @PositiveOrZero
    private BigDecimal amount;
    @Basic
    private Instant lastUpdate;
}
