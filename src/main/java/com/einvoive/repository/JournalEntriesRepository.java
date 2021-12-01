package com.einvoive.repository;

import com.einvoive.model.JournalEntries;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JournalEntriesRepository extends MongoRepository<JournalEntries, String> {
}
