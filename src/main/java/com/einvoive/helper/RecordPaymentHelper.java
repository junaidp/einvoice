package com.einvoive.helper;

import com.einvoive.model.Accounts;
import com.einvoive.model.ErrorCustom;
import com.einvoive.model.ProductMain;
import com.einvoive.model.RecordPayment;
import com.einvoive.repository.RecordPaymentRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class RecordPaymentHelper {

    @Autowired
    RecordPaymentRepository repository;

    @Autowired
    MongoOperations mongoOperation;

    Gson gson = new Gson();

    public String save(RecordPayment recordPayment){
        ErrorCustom error = new ErrorCustom();
        String jsonError;
            try {
                repository.save(recordPayment);
                return "RecordPayment saved";
            }catch(Exception ex){
                error.setErrorStatus("Error");
                error.setError(ex.getMessage());
                jsonError = gson.toJson(error);
                return jsonError;
            }
     }

    public String update(RecordPayment recordPayment) {
        RecordPayment recordPayment1 = mongoOperation.findOne(new Query(Criteria.where("id").is(recordPayment.getId())), RecordPayment.class);
        repository.delete(recordPayment1);
        return save(recordPayment);
    }
}
