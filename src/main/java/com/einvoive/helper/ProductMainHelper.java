package com.einvoive.helper;

import com.einvoive.model.Company;
import com.einvoive.model.ErrorCustom;
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
        ErrorCustom error = new ErrorCustom();
        String jsonError;
        ProductMain productMain = mongoOperation.findOne(new Query(Criteria.where("productName").is(product.getProductName())), ProductMain.class);
        if(productMain != null){
            error.setErrorStatus("Error");
            error.setError("Product Name Already Exists");
            jsonError = gson.toJson(error);
            return jsonError;
        }
        else {
            try {
                repository.save(product);
                return "product saved";
            } catch (Exception ex) {
                error.setErrorStatus("error");
                error.setError(ex.getMessage());
                jsonError = gson.toJson(error);
                return jsonError;
            }
        }
    }

    public String getTopSaledProducts(String companyId){
        List<ProductMain> products = null;
        try {
            Query query = new Query();
            if(!companyId.isEmpty())
                query.limit(10).addCriteria(Criteria.where("companyID").is(companyId));
            products = mongoOperation.find(query, ProductMain.class);
        }catch(Exception ex){
            System.out.println("Error in get Products:"+ ex);
        }
        return gson.toJson(products);
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

    public String deleteProduct(String productID){
        List<ProductMain> products = mongoOperation.find(new Query(Criteria.where("id").is(productID)), ProductMain.class);
        repository.deleteAll(products);
        return "product deleted";
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
