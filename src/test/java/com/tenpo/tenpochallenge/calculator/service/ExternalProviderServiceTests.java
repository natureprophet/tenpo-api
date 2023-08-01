package com.tenpo.tenpochallenge.calculator.service;

import com.mashape.unirest.http.HttpResponse;
import com.tenpo.tenpochallenge.calculator.model.ValueStorage;
import com.tenpo.tenpochallenge.calculator.repository.ValueStorageRepository;
import com.tenpo.tenpochallenge.calculator.service.ExternalProviderService;
import com.tenpo.tenpochallenge.calculator.service.RestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ExternalProviderServiceTests {

    @InjectMocks
    private ExternalProviderService externalProviderService;

    @Mock
    private ValueStorageRepository valueStorageRepository;

    @Mock
    private RestService restService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetPercentage_SuccessfulResponseAndStorePercentage() throws Exception {
        BigDecimal mockPercentage = new BigDecimal("50.0");
        String mockResponseBody = "[50.0]";
        HttpResponse<String> mockResponse = mock(HttpResponse.class);

        when(mockResponse.getStatus()).thenReturn(HttpStatus.OK.value());
        when(mockResponse.getBody()).thenReturn(mockResponseBody);
        when(restService.get(ExternalProviderService.EXTERNAL_SERVICE_API_URL)).thenReturn(mockResponse);

        BigDecimal percentage = externalProviderService.getPercentage();

        assertEquals(mockPercentage, percentage);
        verify(valueStorageRepository, times(1)).save(any(ValueStorage.class));
    }

    @Test
    public void testGetPercentage_ServiceUnavailable_WithLastPercentageStored() throws Exception {
        BigDecimal mockPercentage = new BigDecimal("42.0");

        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.getStatus()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR.value());
        when(restService.get(ExternalProviderService.EXTERNAL_SERVICE_API_URL)).thenReturn(mockResponse);

        BigDecimal lastPercentage = new BigDecimal("42.0");
        when(valueStorageRepository.findById("percentage")).thenReturn(Optional.of(new ValueStorage("percentage", lastPercentage)));

        BigDecimal percentage = externalProviderService.getPercentage();
        assertEquals(mockPercentage, percentage);
        verify(valueStorageRepository, times(0)).save(any(ValueStorage.class));
    }

    @Test
    public void testGetPercentage_ServiceUnavailable_NoLastPercentageStored() throws Exception {
        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.getStatus()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR.value());
        when(restService.get(ExternalProviderService.EXTERNAL_SERVICE_API_URL)).thenReturn(mockResponse);

        when(valueStorageRepository.findById("percentage")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> externalProviderService.getPercentage());
        verify(valueStorageRepository, times(0)).save(any(ValueStorage.class));
    }
}
