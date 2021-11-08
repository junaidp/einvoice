package com.einvoive.repository;

import com.einvoive.model.RecordPayment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface RecordPaymentRepository extends MongoRepository<RecordPayment, String>
    {

        @Query("{ 'invoiceNo' : ?0'}")
        List<RecordPayment> findByName(String invoiceNo);
}
