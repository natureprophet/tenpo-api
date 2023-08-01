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

    public Page<JournalSumCalculation> getJournalSumCalculations(int pageNo) {
        return sumCalculationPageableRepository.findAll(PageRequest.of(pageNo, 10, Sort.Direction.DESC, "creationDate"));
    }

    public BigDecimal sumWithPercentage(BigDecimal numberOne, BigDecimal numberTwo) {
        BigDecimal percentage = externalProviderService.getPercentage();
        BigDecimal sum = numberOne.add(numberTwo);

        BigDecimal decimalPercentage = percentage.divide(new BigDecimal("100"));
        BigDecimal sumPercentage = sum.multiply(decimalPercentage);

        JournalSumCalculation journalSumCalculation = new JournalSumCalculation();
        journalSumCalculation.setNumberOne(numberOne);
        journalSumCalculation.setNumberTwo(numberTwo);
        journalSumCalculation.setPercentage(percentage);
        journalSumCalculation.setResult(sum.add(sumPercentage));
        journalSumCalculation.setCreationDate(new Date());
        journalSumCalculationCrudRepository.save(journalSumCalculation);

        return journalSumCalculation.getResult();
    }
}
