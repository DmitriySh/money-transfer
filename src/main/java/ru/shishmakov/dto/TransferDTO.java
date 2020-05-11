package ru.shishmakov.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;


import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransferDTO {
    @Nullable
    private Long from;
    @Nullable
    private Long to;
    private BigDecimal amount;
}
