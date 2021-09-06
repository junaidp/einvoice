package com.einvoive.repository;

import com.einvoive.model.LineItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface LineItemRepository extends MongoRepository<LineItem, String> {

    @Query("{ 'name' : ?0'}")
    LineItem findByName(String name);


}
