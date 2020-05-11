package ru.shishmakov.persistence.entity;

import java.math.BigDecimal;
import java.time.Instant;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;


import static javax.persistence.GenerationType.IDENTITY;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class AccountAudit {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long fromNumber;

    private Long toNumber;

    @PositiveOrZero
    private BigDecimal amount;

    private String description;

    @Generated(GenerationTime.INSERT)
    @Basic
    @PastOrPresent
    private Instant createdTime;
}
