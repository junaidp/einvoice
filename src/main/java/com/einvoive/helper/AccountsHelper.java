package com.einvoive.helper;

import com.einvoive.model.Accounts;
import com.einvoive.model.ProductMain;
import com.einvoive.repository.AccountsRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccountsHelper {

    @Autowired
    AccountsRepository accountsRepository;

    @Autowired
    MongoOperations mongoOperation;

    Gson gson = new Gson();

    public String save(Accounts accounts){
        try {
            accountsRepository.save(accounts);
            return "BankAccount saved";
        }catch(Exception ex){
            return "BankAccount Not saved"+ ex;
        }

    }
    public String getAccounts(String companyID){
        List<Accounts> accountsList = null;
        try {
            Query query = new Query();
            if(!companyID.isEmpty())
                query.addCriteria(Criteria.where("companyID").is(companyID));
            accountsList = mongoOperation.find(query, Accounts.class);
        }catch(Exception ex){
            System.out.println("Error in get Bank Account:"+ ex);
        }
        return gson.toJson(accountsList);
    }

    public String deleteAccount(String id){
        List<Accounts> accounts = mongoOperation.find(new Query(Criteria.where("id").is(id)), Accounts.class);
        accountsRepository.deleteAll(accounts);
        return "Account deleted";
    }

    public String update(Accounts accounts) {
        try {
            accountsRepository.save(accounts);
            return "BankAccount Updated";
        }catch(Exception ex){
            return "BankAccount Not Updated"+ ex;
        }
    }
}
