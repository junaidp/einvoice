package com.einvoive.helper;

import com.einvoive.model.BankAccount;
import com.einvoive.repository.BankAccountRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BankAccountHelper {

    @Autowired
    BankAccountRepository bankAccountRepository;

    @Autowired
    MongoOperations mongoOperation;

    Gson gson = new Gson();

    public String save(BankAccount bankAccount){
        try {
            bankAccountRepository.save(bankAccount);
            return "BankAccount saved";
        }catch(Exception ex){
            return "BankAccount Not saved"+ ex;
        }

    }
    public String getBankAccounts(String code){
        List<BankAccount> bankAccounts = null;
        try {
            Query query = new Query();
            if(!code.isEmpty())
                query.addCriteria(Criteria.where("code").is(code));
            bankAccounts = mongoOperation.find(query, BankAccount.class);
        }catch(Exception ex){
            System.out.println("Error in get Bank Account:"+ ex);
        }
        return gson.toJson(bankAccounts);
    }

    public String update(BankAccount bankAccount) {
        try {
            bankAccountRepository.save(bankAccount);
            return "BankAccount Updated";
        }catch(Exception ex){
            return "BankAccount Not Updated"+ ex;
        }
    }
}
