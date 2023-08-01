package com.tenpo.tenpochallenge.calculator.repository;

import com.tenpo.tenpochallenge.calculator.model.ValueStorage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ValueStorageRepository extends CrudRepository<ValueStorage, String> {

}
