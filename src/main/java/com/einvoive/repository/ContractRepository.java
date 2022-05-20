package com.einvoive.repository;

import com.einvoive.model.Contract;
import com.einvoive.model.CreditInvoice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ContractRepository extends MongoRepository<Contract, String> {

}
