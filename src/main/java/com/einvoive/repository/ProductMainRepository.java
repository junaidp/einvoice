package com.einvoive.repository;

import com.einvoive.model.Product;
import com.einvoive.model.ProductMain;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ProductMainRepository extends MongoRepository<ProductMain, String> {

    @Query("{ 'name' : ?0'}")
    Product findByName(String name);


}
