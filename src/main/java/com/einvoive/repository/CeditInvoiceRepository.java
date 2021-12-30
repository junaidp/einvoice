package com.einvoive.repository;

import com.einvoive.model.CreditInvoice;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CeditInvoiceRepository extends MongoRepository<CreditInvoice, String> {
}
