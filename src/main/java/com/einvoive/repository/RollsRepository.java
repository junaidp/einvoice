package com.einvoive.repository;

import com.einvoive.model.Rolls;
import com.einvoive.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface RollsRepository extends MongoRepository<Rolls, String> {

    @Query("{ 'rollName' : ?0'}")
    Rolls findUserByName(String rollName);

}
