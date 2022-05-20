package com.einvoive.repository;

import com.einvoive.model.ContractItem;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContractItemRepository extends MongoRepository<ContractItem, String> {
}
