package com.tenpo.tenpochallenge.calculator.controller;

import com.tenpo.tenpochallenge.calculator.dto.SumRequestDTO;
import com.tenpo.tenpochallenge.calculator.model.JournalSumCalculation;
import com.tenpo.tenpochallenge.calculator.service.CalculatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class CalculatorControllerTests {

    @Mock
    private CalculatorService calculatorService;

    @InjectMocks
    private CalculatorController calculatorController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSumWithPercentage_Successful() {
        // Arrange
        SumRequestDTO sumRequestDTO = new SumRequestDTO(new BigDecimal("10"), new BigDecimal("20"));
        BigDecimal expectedResult = new BigDecimal("33.0");

        when(calculatorService.sumWithPercentage(sumRequestDTO.getNumberOne(), sumRequestDTO.getNumberTwo()))
                .thenReturn(expectedResult);

        // Act
        BigDecimal result = calculatorController.sumWithPercentage(sumRequestDTO);

        // Assert
        assertEquals(expectedResult, result);
    }

    @Test
    public void testSumWithPercentage_TooManyRequests() {
        // Arrange
        SumRequestDTO sumRequestDTO = new SumRequestDTO(new BigDecimal("10"), new BigDecimal("20"));

        when(calculatorService.sumWithPercentage(sumRequestDTO.getNumberOne(), sumRequestDTO.getNumberTwo()))
                .thenThrow(new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "REQUEST_LIMIT_PER_MINUTE_REACHED_OUT"));

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> calculatorController.sumWithPercentage(sumRequestDTO));
    }

    @Test
    public void testGetJournalSumCalculationList_Successful() {
        // Arrange
        int pageNo = 0;
        List<JournalSumCalculation> journalSumCalculations = Arrays.asList(
                new JournalSumCalculation(),
                new JournalSumCalculation()
        );
        Page<JournalSumCalculation> expectedPage = new PageImpl<>(journalSumCalculations);

        when(calculatorService.getJournalSumCalculations(pageNo)).thenReturn(expectedPage);

        // Act
        Page<JournalSumCalculation> resultPage = calculatorController.getJournalSumCalculationList(pageNo);

        // Assert
        assertNotNull(resultPage);
        assertEquals(expectedPage, resultPage);
    }
}
