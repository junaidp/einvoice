package com.einvoive.repository;

import com.einvoive.model.Company;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface CompanyRepository extends MongoRepository<Company, String> {

@Query("{ 'companyID' : ?0'}")
    Company findUserBycompanyID(String companyID);

}
