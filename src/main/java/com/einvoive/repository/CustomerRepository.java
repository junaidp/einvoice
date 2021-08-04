package com.einvoive.repository;

import com.einvoive.model.Customer;
import com.einvoive.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface CustomerRepository extends MongoRepository<Customer, String> {

    @Query("{ 'userId' : ?0'}")
    List<Customer> findCustomerByName(String userId);

}
