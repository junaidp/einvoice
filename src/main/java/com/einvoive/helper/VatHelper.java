package com.einvoive.helper;

import com.einvoive.model.*;
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
        ErrorCustom error = new ErrorCustom();
        String jsonError;
        Vat vAT = mongoOperation.findOne(new Query(Criteria.where("vatRates").is(vat.getVatRates())
                .and("companyID").is(vat.getCompanyID())), Vat.class);
        if(vAT == null){
            try {
                repository.save(vat);
                return "VAT saved";
            }catch (Exception ex) {
                error.setErrorStatus("Error");
                error.setError(ex.getMessage());
                jsonError = gson.toJson(error);
                return jsonError;
            }
        }
        else{
            error.setErrorStatus("Error");
            error.setError("VAT already exists");
            jsonError = gson.toJson(error);
            return jsonError;
        }
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

    public String deleteVAT(String id){
        List<Vat> vats = mongoOperation.find(new Query(Criteria.where("id").is(id)), Vat.class);
        repository.deleteAll(vats);
        return "VAT deleted";
    }

    public String update(Vat vat) {
        deleteVAT(vat.getId());
        return save(vat);
    }
}
