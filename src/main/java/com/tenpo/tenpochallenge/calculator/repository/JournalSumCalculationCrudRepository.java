package com.tenpo.tenpochallenge.calculator.repository;

import com.tenpo.tenpochallenge.calculator.model.JournalSumCalculation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JournalSumCalculationCrudRepository extends CrudRepository<JournalSumCalculation, Long> {

}
