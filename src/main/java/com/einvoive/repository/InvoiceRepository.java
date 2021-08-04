package com.einvoive.repository;

import com.einvoive.model.Invoice;
import com.einvoive.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface InvoiceRepository extends MongoRepository<Invoice, String> {

    @Query("{ 'name' : ?0'}")
    Invoice findInvoiceByName(String name);


}
