package com.einvoive.repository;

import com.einvoive.model.BankAccount;
import org.springframework.data.mongodb.repository.Query;

import com.einvoive.model.Customer;
import com.einvoive.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface BankAccountRepository extends MongoRepository<BankAccount, String> {

    @Query("{ 'code' : ?0'}")
    List<Customer> findByName(String code);

}
