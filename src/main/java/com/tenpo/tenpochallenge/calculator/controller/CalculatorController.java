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

    private final int REQUEST_RATE_LIMITE_PER_MINUTE = 3;

    final CalculatorService calculatorService;

    private final Bucket bucket;

    public CalculatorController(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;

        // Set rate limiting configuration for API requests
        Bandwidth limit = Bandwidth.classic(REQUEST_RATE_LIMITE_PER_MINUTE,
                Refill.greedy(REQUEST_RATE_LIMITE_PER_MINUTE, Duration.ofMinutes(1)));
        this.bucket = Bucket.builder()
                .addLimit(limit)
                .build();
    }

    @PostMapping("/sum")
    public BigDecimal sumWithPercentage(@RequestBody SumRequestDTO sumRequestDTO) {
        // Try to consume a token from the rate limiter bucket
        if (bucket.tryConsume(1)) {
            // If a token is available, calculate and return the sum with percentage
            return calculatorService.sumWithPercentage(
                    sumRequestDTO.getNumberOne(), sumRequestDTO.getNumberTwo());
        } else {
            // If rate limit is exceeded, throw a "Too Many Requests" exception
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                    "REQUEST_LIMIT_PER_MINUTE_REACHED_OUT");
        }
    }

    @GetMapping("/journals")
    public Page<JournalSumCalculation> getJournalSumCalculationList(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo) {
        // Retrieve a paginated list of JournalSumCalculation objects
        return calculatorService.getJournalSumCalculations(pageNo);
    }
}
