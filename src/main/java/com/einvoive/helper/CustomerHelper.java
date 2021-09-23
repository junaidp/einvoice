package com.einvoive.helper;

import com.einvoive.model.Company;
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

    List<Customer> listCustomers = null;

    public String save(Customer customer) {
        String msg = validationBeforeSave(customer);
        if(msg == null || msg.isEmpty()) {
            repository.save(customer);
            return "Customer Saved";
        }
        return msg+"--Already Exists";
    }

    private String validationBeforeSave(Customer customer) {
        String msg = null;
        String msg1 = "";
        String msg2 = "";
        String msg3 = "";
        List<Customer> phoneList = mongoOperation.find(new Query(Criteria.where("phone").is(customer.getPhone())), Customer.class);
        List<Customer> phoneMainList = mongoOperation.find(new Query(Criteria.where("phoneMain").is(customer.getPhoneMain())), Customer.class);
        List<Customer> emailList = mongoOperation.find(new Query(Criteria.where("email").is(customer.getEmail())), Customer.class);
        if(phoneList.size() > 0)
            msg1 = "--Customer Phone No";
        if(emailList.size() > 0)
            msg2 = "--Customer Email";
        if(phoneMainList.size() > 0)
            msg3 = "--Customer Phone Main No";
        msg = msg1 + msg2 + msg3;
        return msg;
    }

    public String update(Customer customer){
        try {
            repository.save(customer);
        }catch(Exception ex){
            return "Customer Not updated"+ ex;
        }
        return "customer updated";
    }

    public String getAllCustomers(String comapnyID){
        List<Customer>customers = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("companyID").is(comapnyID));
            System.out.println("QUERY");
            customers = mongoOperation.find(query, Customer.class);
        }catch(Exception ex){
            System.out.println("Error in get Customers:"+ ex);
        }
        return gson.toJson(customers);
    }

    public String getCustomer(String customerID){
        listCustomers = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("id").is(customerID));
            System.out.println("QUERY");
            listCustomers = mongoOperation.find(query, Customer.class);
        }catch(Exception ex){
            System.out.println("Error in get Customers:"+ ex);
        }
        return gson.toJson(listCustomers);
    }

  /*  public String getTopCustomers(){
        List<Customer> customers = null;
        try {
            Query query = new Query();
            query.limit(10)
                    .addCriteria(Criteria.where("companyID")).ord;
            System.out.println("QUERY");
            customers = mongoOperation..find(query, Customer.class);
        }catch(Exception ex){
            System.out.println("Error in get Customers:"+ ex);
        }
        return gson.toJson(customers);
    }*/

    public String getAvailableId() {
        Long total = repository.count();
        int count = total.intValue();
        return count+1+"";

    }

    public String deleteCustomers(String customerID){
        getCustomer(customerID);
        repository.deleteAll(listCustomers);
        return "Customer deleted";
    }

}



   /* Query query = new Query();
            query.addCriteria(Criteria.where("name").is(name).and("password").is(password));
                    //	BasicQuery query1 = new BasicQuery("{ name : '"+name+"'} , { password: '"+password+"'}");
                    System.out.println("ff");
                    User user = mongoOperation.findOne(query, User.class);*/

