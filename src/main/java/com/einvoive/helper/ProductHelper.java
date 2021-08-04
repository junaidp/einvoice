package com.einvoive.helper;

import com.einvoive.model.Customer;
import com.einvoive.model.Product;
import com.einvoive.repository.ProductRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductHelper {

    @Autowired
    ProductRepository repository;

    @Autowired
    MongoOperations mongoOperation;

    Gson gson = new Gson();

    public String save(Product product){
        try {
            repository.save(product);
            return "product saved";
        }catch(Exception ex){
            return "product Not saved"+ ex;
        }

    }

    public String getProducts(String invoiceId){
        List<Product> products = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("invoiceId").is(invoiceId));
            products = mongoOperation.find(query, Product.class);
        }catch(Exception ex){
            System.out.println("Error in get Products:"+ ex);
        }
        return gson.toJson(products);
    }
}
