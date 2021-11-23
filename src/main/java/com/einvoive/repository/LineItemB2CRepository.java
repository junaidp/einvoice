package com.einvoive.repository;

import com.einvoive.model.LineItem;
import com.einvoive.model.LineItemB2C;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface LineItemB2CRepository extends MongoRepository<LineItemB2C, String> {

    @Query("{ 'name' : ?0'}")
    LineItem findByName(String name);
}
