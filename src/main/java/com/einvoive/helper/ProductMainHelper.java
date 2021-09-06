package com.einvoive.helper;

import com.einvoive.model.ProductMain;
import com.einvoive.repository.ProductMainRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductMainHelper {

    @Autowired
    ProductMainRepository repository;

    @Autowired
    MongoOperations mongoOperation;

    Gson gson = new Gson();

    public String save(ProductMain product){
        try {
            repository.save(product);
            return "product saved";
        }catch(Exception ex){
            return "product Not saved"+ ex;
        }

    }

    public String getProducts(String companyId){
        List<ProductMain> products = null;
        try {
            Query query = new Query();
            if(!companyId.isEmpty())
             query.addCriteria(Criteria.where("companyID").is(companyId));
            products = mongoOperation.find(query, ProductMain.class);
        }catch(Exception ex){
            System.out.println("Error in get Products:"+ ex);
        }
        return gson.toJson(products);
    }

    public String update(ProductMain product) {
        try {
            repository.save(product);
            return "product updated";
        }catch(Exception ex){
            return "product Not updated "+ ex;
        }
    }
}
