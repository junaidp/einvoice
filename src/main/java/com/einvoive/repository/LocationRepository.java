package com.einvoive.repository;

import com.einvoive.model.Customer;
import com.einvoive.model.Location;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface LocationRepository extends MongoRepository<Location, String> {

    @Query("{ 'locationId' : ?0'}")
    List<Location> findLocationByName(String userId);

}
