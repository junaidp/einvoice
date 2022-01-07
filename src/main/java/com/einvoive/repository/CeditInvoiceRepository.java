package com.einvoive.repository;

import com.einvoive.model.CreditInvoice;
import com.einvoive.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface CeditInvoiceRepository extends MongoRepository<CreditInvoice, String> {

    @Query("{ 'companyID' : ?0'}")
    CreditInvoice findCreditInvoiceByID(String companyID);

}
