package com.tenpo.tenpochallenge.calculator.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table
public class JournalSumCalculation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(columnDefinition = "DECIMAL(20, 2)")
    private BigDecimal numberOne;

    @Column(columnDefinition = "DECIMAL(20, 2)")
    private BigDecimal numberTwo;

    @Column(columnDefinition = "DECIMAL(20, 2)")
    private BigDecimal percentage;

    @Column(columnDefinition = "DECIMAL(20, 2)")
    private BigDecimal result;

    private Date creationDate;
}
