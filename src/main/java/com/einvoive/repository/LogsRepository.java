package com.einvoive.repository;

import com.einvoive.model.Logs;
import com.einvoive.model.Translation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface LogsRepository extends MongoRepository <Logs, String> {
    @Query("{ 'id' : ?0'}")
    Translation findByIdMatches(String id);
}
