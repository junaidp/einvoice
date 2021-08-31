package com.einvoive.helper;

import com.einvoive.model.BankAccount;
import com.einvoive.model.Revenue;
import com.einvoive.repository.RevenueRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RevenueHelper {

    @Autowired
    RevenueRepository revenueRepository;

    @Autowired
    MongoOperations mongoOperation;

    Gson gson = new Gson();

    public String save(Revenue revenue){
        try {
            revenueRepository.save(revenue);
            return "Revenue saved";
        }catch(Exception ex){
            return "Revenue Not saved"+ ex;
        }

    }
    public String getRevenues(String code){
        List<Revenue> revenues = null;
        try {
            Query query = new Query();
            if(!code.isEmpty())
                query.addCriteria(Criteria.where("code").is(code));
            revenues = mongoOperation.find(query, Revenue.class);
        }catch(Exception ex){
            System.out.println("Error in get Revenue:"+ ex);
        }
        return gson.toJson(revenues);
    }

    public String update(Revenue revenue) {
        try {
            revenueRepository.save(revenue);
            return "Revenue Updated";
        }catch(Exception ex){
            return "Revenue Not Updated"+ ex;
        }
    }
}
