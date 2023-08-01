package com.tenpo.tenpochallenge.calculator.controller;

import com.tenpo.tenpochallenge.calculator.dto.SumRequestDTO;
import com.tenpo.tenpochallenge.calculator.model.JournalSumCalculation;
import com.tenpo.tenpochallenge.calculator.service.CalculatorService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.Duration;

@RestController
@RequestMapping("/api/v1/calculator")
public class CalculatorController {

    final CalculatorService calculatorService;

    private final Bucket bucket;

    public CalculatorController(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
        Bandwidth limit = Bandwidth.classic(3, Refill.greedy(3, Duration.ofMinutes(1)));
        this.bucket = Bucket.builder()
                .addLimit(limit)
                .build();
    }

    @PostMapping("/sum")
    public BigDecimal sumWithPercentage(@RequestBody SumRequestDTO sumRequestDTO) {
        if(bucket.tryConsume(1))
            return calculatorService.sumWithPercentage(
                    sumRequestDTO.getNumberOne(), sumRequestDTO.getNumberTwo());
        throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                "REQUEST_LIMIT_PER_MINUTE_REACHED_OUT");
    }

    @GetMapping("/journals")
    public Page<JournalSumCalculation> getJournalSumCalculationList(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo) {
        return calculatorService.getJournalSumCalculations(pageNo);
    }
}
