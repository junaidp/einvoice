package com.einvoive.helper;

import com.einvoive.model.*;
import com.einvoive.repository.AccountsRepository;
import com.einvoive.repository.BankAccountRepository;
import com.einvoive.util.Utility;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private Gson gson = new Gson();
    @Autowired
    MongoOperations mongoOperation;
    @Autowired
    private LogsHelper logsHelper;
    @Autowired
    CompanyHelper companyHelper;
    private Logger logger = LoggerFactory.getLogger(BankAccountHelper.class);

    public String saveBank(BankAccount bankAccountSave){
        ErrorCustom error = new ErrorCustom();
        String jsonError;
        BankAccount bankAccount = mongoOperation.findOne(new Query(Criteria.where("ibanNumber").is(bankAccountSave.getIbanNumber())), BankAccount.class);
        if(bankAccount == null ){
            try {
                bankAccountRepository.save(bankAccountSave);
                logsHelper.save(new Logs("Bank Account added for "+ Utility.getCompanyName(bankAccountSave.getCompanyID(), mongoOperation), Utility.getCompanyName(bankAccountSave.getCompanyID(), mongoOperation)+ " has added a new Bank Account "+ bankAccountSave.getBankName()+", IBAN "+bankAccountSave.getIbanNumber()+", address "+bankAccountSave.getAddress()));
                return "Bank Account saved";
            }catch(Exception ex){
                error.setErrorStatus("Error");
                error.setError(ex.getMessage());
                jsonError = gson.toJson(error);
                return jsonError;
            }
        }
        else{
            error.setError("Bank Account Already Exists");
            jsonError = gson.toJson(error);
            return jsonError;
        }
    }

    public String getBankaccounts(String companyID){
        List<BankAccount> accountsList = null;
        try {
            Query query = new Query();
            if(!companyID.isEmpty())
                query.addCriteria(Criteria.where("companyID").is(companyID));
            accountsList = mongoOperation.find(query, BankAccount.class);
        }catch(Exception ex){
            System.out.println("Error in get Bank Account:"+ ex);
        }
        return gson.toJson(accountsList);
    }

    public String getBankaccountsByID(String id) {
        BankAccount account = null;
        try {
            account = mongoOperation.findOne(new Query(Criteria.where("id").is(id)), BankAccount.class);
        } catch (Exception ex) {
            System.out.println("Error in get Bank Account:" + ex);
        }
        return gson.toJson(account);
    }

    public String deleteBankAccounts(String iD){
        BankAccount bankAccount = mongoOperation.findOne(new Query(Criteria.where("id").is(iD)), BankAccount.class);
        bankAccountRepository.delete(bankAccount);
        return "Account deleted";
    }

    public String updateBankAccount(BankAccount bankAccount){
        deleteBankAccounts(bankAccount.getId());
        return saveBank(bankAccount);
    }


}
