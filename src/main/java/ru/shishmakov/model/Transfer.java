package ru.shishmakov.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Transfer {
    private Long from;
    private Long to;
    private BigDecimal amount;
}
