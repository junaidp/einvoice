package com.einvoive.repository;

import com.einvoive.model.Accounts;
import org.springframework.data.mongodb.repository.Query;

import com.einvoive.model.Customer;
import com.einvoive.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface AccountsRepository extends MongoRepository<Accounts, String> {

    @Query("{ 'code' : ?0'}")
    List<Customer> findByName(String code);

}
