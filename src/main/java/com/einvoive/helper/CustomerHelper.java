package com.einvoive.helper;

import com.einvoive.model.Customer;
import com.einvoive.model.User;
import com.einvoive.repository.CustomerRepository;
import com.einvoive.repository.UserRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomerHelper {

    @Autowired
    CustomerRepository repository;
    @Autowired
    MongoOperations mongoOperation;

    Gson gson = new Gson();

    public String save(Customer customer){
        try {
            customer.setCustomerId(getAvailableId());
            repository.save(customer);
        }catch(Exception ex){
            return "Customer Not saved"+ ex;
        }
        return "customer saved";
    }

    public String getAllCustomers(String userId){
        List<Customer> customers = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("userId").is(userId));
            System.out.println("QUERY");
            customers = mongoOperation.find(query, Customer.class);
        }catch(Exception ex){
            System.out.println("Error in get Customers:"+ ex);
        }
        return gson.toJson(customers);
    }

    public String getAvailableId() {
        Long total = repository.count();
        int count = total.intValue();
        return count+1+"";

    }
}


   /* Query query = new Query();
            query.addCriteria(Criteria.where("name").is(name).and("password").is(password));
                    //	BasicQuery query1 = new BasicQuery("{ name : '"+name+"'} , { password: '"+password+"'}");
                    System.out.println("ff");
                    User user = mongoOperation.findOne(query, User.class);*/

