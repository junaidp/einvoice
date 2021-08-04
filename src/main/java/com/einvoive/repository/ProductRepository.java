package com.einvoive.repository;

import com.einvoive.model.Product;
import com.einvoive.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ProductRepository extends MongoRepository<Product, String> {

    @Query("{ 'name' : ?0'}")
    Product findByName(String name);


}
