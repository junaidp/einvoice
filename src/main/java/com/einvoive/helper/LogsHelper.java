package com.einvoive.helper;

import com.einvoive.model.ErrorCustom;
import com.einvoive.model.Logs;
import com.einvoive.model.Translation;
import com.einvoive.repository.LogsRepository;
import com.einvoive.repository.TranslationRepository;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class LogsHelper {
    @Autowired
    LogsRepository logsRepository;
    @Autowired
    MongoOperations mongoOperation;
    private Gson gson = new Gson();
    private Logger logger = LoggerFactory.getLogger(LogsHelper.class);

    public String save(Logs logs){
        ErrorCustom error = new ErrorCustom();
        String jsonError;
        try{
            logsRepository.save(logs);
            return "Logs saved";
        }catch (Exception ex)
        {
            logger.info("Exception in saving Logs "+ex.getMessage());
            error.setErrorStatus("Error");
            error.setError(ex.getMessage());
            jsonError = gson.toJson(error);
            return jsonError;
        }
    }

}
