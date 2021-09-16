package com.einvoive.repository;

import com.einvoive.model.Translation;
import com.einvoive.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.beans.Transient;

public interface TranslationRepository extends MongoRepository<Translation, String> {

    @Query("{ 'id' : ?0'}")
    Translation findByIdMatches(String id);
}
