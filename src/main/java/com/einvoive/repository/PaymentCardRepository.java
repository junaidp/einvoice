package com.einvoive.repository;

import com.einvoive.model.PaymentCard;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PaymentCardRepository extends MongoRepository<PaymentCard, String> {

    @Query("{ 'userId' : ?0'}")
    List<PaymentCard> findByUserId(String userId);

}
