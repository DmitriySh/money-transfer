package ru.shishmakov.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Transfer {
    @Nullable
    private Long from;
    @Nullable
    private Long to;
    private BigDecimal amount;
}
