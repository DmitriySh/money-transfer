package ru.shishmakov.model;

import lombok.Data;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@Entity
public class Account {
    @Id
    @GeneratedValue
    private Long id;
    private BigDecimal amount;
    @Basic
    private Instant lastUpdate;
}
