package com.tenpo.tenpochallenge.calculator.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SumRequestDTO {
    private BigDecimal numberOne;
    private BigDecimal numberTwo;
}
