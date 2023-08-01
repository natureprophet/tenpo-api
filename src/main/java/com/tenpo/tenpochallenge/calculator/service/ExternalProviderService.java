package com.tenpo.tenpochallenge.calculator.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.tenpo.tenpochallenge.calculator.model.ValueStorage;
import com.tenpo.tenpochallenge.calculator.repository.ValueStorageRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
public class ExternalProviderService {

    public static final String EXTERNAL_SERVICE_API_URL = "https://www.randomnumberapi.com/api/v1.0/random?min=10&max=100";
    public static final int MAX_RETRIES = 3;

    private final ValueStorageRepository valueStorageRepository;
    private final RestService restService;

    public ExternalProviderService(ValueStorageRepository valueStorageRepository,
                                   RestService restService) {
        this.valueStorageRepository = valueStorageRepository;
        this.restService = restService;
    }

    // Evicts the percentage cache to refresh the value after every 30 minutes.
    @Caching(evict = {@CacheEvict(cacheNames = "percentage", allEntries = true)})
    @Scheduled(fixedDelay = 1000 * 60 * 30) // 30 minutes
    public void cleanPercentageCache() {
    }

    // Retrieves the percentage value from an external API and caches it for subsequent calls.
    @Cacheable(value = "percentage")
    public BigDecimal getPercentage() {
        int retries = 0;
        BigDecimal percentage = null;
        while (retries < MAX_RETRIES) {
            try {
                HttpResponse<String> response = restService.get(EXTERNAL_SERVICE_API_URL);
                if (response.getStatus() == HttpStatus.OK.value()) {
                    percentage = new BigDecimal(response.getBody().replaceAll("[\\[\\]]", "")); // "[50]"
                    break;
                } else {
                    retries++;
                }
            } catch (Exception e) {
                retries++;
            }
        }

        if (percentage == null) {
            // If unable to fetch the percentage from the external API, attempt to retrieve the last cached value.
            BigDecimal lastPercentage = getLastPercentage();
            if (lastPercentage != null)
                return lastPercentage;
            else
                // If no cached value is available, throw a "Service Unavailable" exception.
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "SERVICE_UNAVAILABLE");
        }

        // Cache the newly fetched percentage value and update the last cached value.
        updateLastPercentage(percentage);
        return percentage;
    }

    // Saves the updated percentage value to the database.
    public void updateLastPercentage(BigDecimal percentage) {
        ValueStorage valueStorage = valueStorageRepository.findById("percentage")
                .orElse(new ValueStorage("percentage", percentage));
        valueStorage.setValue(percentage);
        valueStorageRepository.save(valueStorage);
    }

    // Retrieves the last cached percentage value from the database.
    public BigDecimal getLastPercentage() {
        return valueStorageRepository.findById("percentage").orElse(new ValueStorage()).getValue();
    }
}