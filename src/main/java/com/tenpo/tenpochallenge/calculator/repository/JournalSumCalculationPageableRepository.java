package com.tenpo.tenpochallenge.calculator.repository;

import com.tenpo.tenpochallenge.calculator.model.JournalSumCalculation;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JournalSumCalculationPageableRepository extends PagingAndSortingRepository<JournalSumCalculation, Long> {

}
