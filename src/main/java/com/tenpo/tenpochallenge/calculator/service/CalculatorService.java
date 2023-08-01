package com.tenpo.tenpochallenge.calculator.service;

import com.tenpo.tenpochallenge.calculator.model.JournalSumCalculation;
import com.tenpo.tenpochallenge.calculator.repository.JournalSumCalculationCrudRepository;
import com.tenpo.tenpochallenge.calculator.repository.JournalSumCalculationPageableRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

@Service
public class CalculatorService {

    final ExternalProviderService externalProviderService;

    final JournalSumCalculationCrudRepository journalSumCalculationCrudRepository;

    final JournalSumCalculationPageableRepository sumCalculationPageableRepository;

    public CalculatorService(ExternalProviderService externalProviderService,
                             JournalSumCalculationCrudRepository journalSumCalculationCrudRepository,
                             JournalSumCalculationPageableRepository sumCalculationPageableRepository) {
        this.externalProviderService = externalProviderService;
        this.journalSumCalculationCrudRepository = journalSumCalculationCrudRepository;
        this.sumCalculationPageableRepository = sumCalculationPageableRepository;
    }

    // Retrieves a paginated list of JournalSumCalculation objects sorted by creation date in descending order.
    public Page<JournalSumCalculation> getJournalSumCalculations(int pageNo) {
        return sumCalculationPageableRepository.findAll(PageRequest.of(pageNo, 10, Sort.Direction.DESC, "creationDate"));
    }

    // Calculates the sum of two numbers with a percentage and saves the result asynchronously to the database.
    // The percentage is obtained from an external provider service.
    public BigDecimal sumWithPercentage(BigDecimal numberOne, BigDecimal numberTwo) {
        BigDecimal percentage = externalProviderService.getPercentage();
        BigDecimal sum = numberOne.add(numberTwo);

        // Calculate the actual percentage (divide by 100) and the sum with percentage.
        BigDecimal decimalPercentage = percentage.divide(new BigDecimal("100"));
        BigDecimal sumPercentage = sum.multiply(decimalPercentage);

        // Asynchronously save the calculation details to the database using CompletableFuture.
        CompletableFuture.runAsync(() -> {
            JournalSumCalculation journalSumCalculation = new JournalSumCalculation();
            journalSumCalculation.setNumberOne(numberOne);
            journalSumCalculation.setNumberTwo(numberTwo);
            journalSumCalculation.setPercentage(percentage);
            journalSumCalculation.setResult(sum.add(sumPercentage));
            journalSumCalculation.setCreationDate(new Date());
            journalSumCalculationCrudRepository.save(journalSumCalculation);
        });

        // Return the result of the sum with percentage.
        return sum.add(sumPercentage);
    }
}
