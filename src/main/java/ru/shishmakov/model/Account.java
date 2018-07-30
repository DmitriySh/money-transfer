package ru.shishmakov.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

import static javax.persistence.TemporalType.TIMESTAMP;

@Data
@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue
    private Long id;
    private BigDecimal amount;
    @Temporal(TIMESTAMP)
    private Date lastUpdate;

}
