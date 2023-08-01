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

    @Caching(evict = {@CacheEvict(cacheNames = "percentage", allEntries = true)})
    @Scheduled(fixedDelay = 1000 * 60 * 30) // 30 minutes
    public void cleanPercentageCache(){}

    @Cacheable(value="percentage")
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
            BigDecimal lastPercentage = getLastPercentage();
            if(lastPercentage != null)
                return lastPercentage;
            else
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "SERVICE_UNAVAILABLE");
        }

        updateLastPercentage(percentage);
        return percentage;
    }

    public void updateLastPercentage(BigDecimal percentage) {
        ValueStorage valueStorage = valueStorageRepository.findById("percentage")
                .orElse(new ValueStorage("percentage", percentage));
        valueStorage.setValue(percentage);
        valueStorageRepository.save(valueStorage);
    }

    public BigDecimal getLastPercentage() {
        return valueStorageRepository.findById("percentage").orElse(new ValueStorage()).getValue();
    }
}
