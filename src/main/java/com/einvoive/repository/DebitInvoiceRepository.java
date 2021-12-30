package com.einvoive.repository;

import com.einvoive.model.DebitInvoice;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DebitInvoiceRepository extends MongoRepository<DebitInvoice, String> {
}
