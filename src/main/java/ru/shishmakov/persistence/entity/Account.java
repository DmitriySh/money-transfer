package ru.shishmakov.persistence.entity;

import java.math.BigDecimal;
import java.time.Instant;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import static javax.persistence.GenerationType.IDENTITY;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Account {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long accNumber;

    @PositiveOrZero
    private BigDecimal amount;

    @NotNull
    @CreatedDate
    @PastOrPresent
    private Instant createdTime;

    @NotNull
    @LastModifiedDate
    @PastOrPresent
    private Instant updatedTime;
}
