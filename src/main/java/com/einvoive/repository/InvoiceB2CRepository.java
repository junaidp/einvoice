package com.einvoive.repository;

import com.einvoive.model.Invoice;
import com.einvoive.model.InvoiceB2C;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface InvoiceB2CRepository extends MongoRepository<InvoiceB2C, String> {

    @Query("{ 'name' : ?0'}")
    Invoice findInvoiceByName(String name);

    @Query("{ 'customerID' : ?0'}")
    Invoice findInvoiceBycustomerID(String customerID);

    @Query("find({state:'ACTIVE'}).sort({created:-1}).limit(1)")
    Invoice findOneActive(Sort sort);

}
