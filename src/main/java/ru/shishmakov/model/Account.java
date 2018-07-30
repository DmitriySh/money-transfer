package ru.shishmakov.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue
    private Long id;
    private BigDecimal amount;
    @Basic
    private Instant lastUpdate;
}
