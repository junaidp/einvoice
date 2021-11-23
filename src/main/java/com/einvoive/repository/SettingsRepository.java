package com.einvoive.repository;

import com.einvoive.model.Settings;
import com.einvoive.model.Translation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface SettingsRepository extends MongoRepository<Settings, String> {

}
