package com.einvoive.repository;

import com.einvoive.model.Company;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface CompanyRepository extends MongoRepository<Company, String> {

@Query("{ 'companyName' : ?0'}")
    Company findUserByName(String companyName);

}
