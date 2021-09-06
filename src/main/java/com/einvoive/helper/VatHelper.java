package com.einvoive.helper;

import com.einvoive.model.Customer;
import com.einvoive.model.Vat;
import com.einvoive.repository.CustomerRepository;
import com.einvoive.repository.VatRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class VatHelper {

    @Autowired
    VatRepository repository;
    @Autowired
    MongoOperations mongoOperation;

    Gson gson = new Gson();

    public String save(Vat vat){
        try {

            repository.save(vat);
        }catch(Exception ex){
            return "vat Not saved"+ ex;
        }
        return "vat saved";
    }

    public String getAllVats(String companyID){
        List<Vat> vats = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("companyID").is(companyID));
            vats = mongoOperation.find(query, Vat.class);
        }catch(Exception ex){
            System.out.println("Error in get vats:"+ ex);
        }
        return gson.toJson(vats);
    }

    public String update(Vat vat) {
        try {
            repository.save(vat);
        }catch(Exception ex){
            return "vat Not updated"+ ex;
        }
        return "vat updated";
    }
}
