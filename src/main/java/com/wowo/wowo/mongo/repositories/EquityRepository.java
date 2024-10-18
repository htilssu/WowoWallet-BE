package com.wowo.wowo.mongo.repositories;

import com.wowo.wowo.mongo.documents.Equity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface EquityRepository extends MongoRepository<Equity, String> {

    Optional<Equity> findByUser(String user);
    Equity findByUserAndMonthAndYear(String user,
            Integer month,
            Integer year);
}
