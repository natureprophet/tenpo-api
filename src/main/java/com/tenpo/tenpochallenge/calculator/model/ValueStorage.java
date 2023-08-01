package com.tenpo.tenpochallenge.calculator.model;


import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.math.BigDecimal;

@RedisHash("ValueStorage")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ValueStorage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    private BigDecimal value;
}