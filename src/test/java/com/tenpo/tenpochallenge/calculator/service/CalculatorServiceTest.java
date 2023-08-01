package com.tenpo.tenpochallenge.calculator.service;

import com.tenpo.tenpochallenge.calculator.model.JournalSumCalculation;
import com.tenpo.tenpochallenge.calculator.repository.JournalSumCalculationCrudRepository;
import com.tenpo.tenpochallenge.calculator.repository.JournalSumCalculationPageableRepository;
import com.tenpo.tenpochallenge.calculator.service.CalculatorService;
import com.tenpo.tenpochallenge.calculator.service.ExternalProviderService;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class CalculatorServiceTest {

    @Test
    public void testGetJournalSumCalculations() {
        // Arrange
        ExternalProviderService externalProviderService = mock(ExternalProviderService.class);
        JournalSumCalculationCrudRepository journalSumCalculationCrudRepository = mock(JournalSumCalculationCrudRepository.class);
        JournalSumCalculationPageableRepository sumCalculationPageableRepository = mock(JournalSumCalculationPageableRepository.class);

        CalculatorService calculatorService = new CalculatorService(
                externalProviderService, journalSumCalculationCrudRepository, sumCalculationPageableRepository);

        int pageNo = 0;
        List<JournalSumCalculation> journalSumCalculations = Arrays.asList(
                new JournalSumCalculation(),
                new JournalSumCalculation()
        );
        Page<JournalSumCalculation> expectedPage = new PageImpl<>(journalSumCalculations);

        when(sumCalculationPageableRepository.findAll(any(PageRequest.class))).thenReturn(expectedPage);

        // Act
        Page<JournalSumCalculation> resultPage = calculatorService.getJournalSumCalculations(pageNo);

        // Assert
        assertNotNull(resultPage);
        assertEquals(expectedPage, resultPage);
    }

    @Test
    public void testSumWithPercentage() {
        // Arrange
        ExternalProviderService externalProviderService = mock(ExternalProviderService.class);
        JournalSumCalculationCrudRepository journalSumCalculationCrudRepository = mock(JournalSumCalculationCrudRepository.class);
        JournalSumCalculationPageableRepository sumCalculationPageableRepository = mock(JournalSumCalculationPageableRepository.class);

        CalculatorService calculatorService = new CalculatorService(
                externalProviderService, journalSumCalculationCrudRepository, sumCalculationPageableRepository);

        BigDecimal numberOne = new BigDecimal("10");
        BigDecimal numberTwo = new BigDecimal("20");
        BigDecimal percentage = new BigDecimal("50");

        when(externalProviderService.getPercentage()).thenReturn(percentage);

        // Act
        BigDecimal result = calculatorService.sumWithPercentage(numberOne, numberTwo);

        // Assert
        BigDecimal expectedSumWithPercentage = new BigDecimal("45.0");

        assertEquals(expectedSumWithPercentage, result);
        verify(externalProviderService, times(1)).getPercentage();
        verify(journalSumCalculationCrudRepository, times(1)).save(any());
    }
}
