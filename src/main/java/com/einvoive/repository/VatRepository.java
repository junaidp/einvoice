package com.einvoive.repository;

import com.einvoive.model.Customer;
import com.einvoive.model.Vat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface VatRepository extends MongoRepository<Vat, String> {

    @Query("{ 'userId' : ?0'}")
    List<Vat> findCustomerByName(String userId);

}
