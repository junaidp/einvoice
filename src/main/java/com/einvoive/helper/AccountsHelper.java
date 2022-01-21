package com.einvoive.helper;

import com.einvoive.model.Accounts;
import com.einvoive.model.ErrorCustom;
import com.einvoive.model.Logs;
import com.einvoive.model.ProductMain;
import com.einvoive.repository.AccountsRepository;
import com.einvoive.repository.ProductMainRepository;
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
public class AccountsHelper {

    @Autowired
    AccountsRepository accountsRepository;

    @Autowired
    ProductMainRepository productMainRepository;

    @Autowired
    MongoOperations mongoOperation;

    Gson gson = new Gson();

    private Logger logger = LoggerFactory.getLogger(AccountsHelper.class);
    @Autowired
    private LogsHelper logsHelper;
    @Autowired
    CompanyHelper companyHelper;

    public String save(Accounts accounts){
        ErrorCustom error = new ErrorCustom();
        String jsonError;
        Accounts accounts1 = mongoOperation.findOne(new Query(Criteria.where("name").is(accounts.getName())
                .and("companyID").is(accounts.getCompanyID())), Accounts.class);
        Accounts accounts2 = mongoOperation.findOne(new Query(Criteria.where("code").is(accounts.getCode())
                .and("companyID").is(accounts.getCompanyID())), Accounts.class);
        if(accounts1 == null && accounts2 == null){
            try {
                accountsRepository.save(accounts);
                logsHelper.save(new Logs("Account added for "+ Utility.getCompanyName(accounts.getCompanyID(), mongoOperation), Utility.getCompanyName(accounts.getCompanyID(), mongoOperation)+ " has added a new Account "+ accounts.getName()+", code "+accounts.getCode()+", account type "+accounts.getAccountType()));
                logger.info("Account added for "+ Utility.getCompanyName(accounts.getCompanyID(), mongoOperation));
                return "BankAccount saved";
            }catch(Exception ex){
                error.setErrorStatus("Error");
                error.setError(ex.getMessage());
                jsonError = gson.toJson(error);
                logger.info("Account not saved: "+ex.getMessage());
                return jsonError;
            }
        }
        else{
            error.setErrorStatus("Error");
            if(accounts1 != null) {
                error.setError("Name Already Exists");
                logger.info(accounts1.getName()+ "Name Already Exists");
            }
            else {
                error.setError("Code Already Exists");
                logger.info(accounts1.getCode()+ "Code Already Exists");
            }
            jsonError = gson.toJson(error);
            return jsonError;
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
            System.out.println("Error in get Bank Account: "+ ex.getMessage());
            logger.info("Error in get Bank Account: "+ ex.getMessage());
        }
        return gson.toJson(accountsList);
    }

    public String deleteAccount(String id){
        ErrorCustom error = new ErrorCustom();
        String jsonError;
        Accounts accounts = mongoOperation.findOne(new Query(Criteria.where("id").is(id)), Accounts.class);
        if(accounts != null){
            List<ProductMain> productMainList = mongoOperation.find(new Query(Criteria.where("assignedChartofAccounts").is(accounts.getName())), ProductMain.class);
            if (productMainList.isEmpty()) {
                accountsRepository.delete(accounts);
                logger.info("Deleting account "+accounts.getName()+" for Company "+Utility.getCompanyName(accounts.getCompanyID(), mongoOperation));
                return "Account deleted";
            }
            else{
               error.setErrorStatus("Error");
               error.setError(accounts.getName() + ": already exists in Products");
               logger.info(accounts.getName() + ": already exists in Products");
               jsonError = gson.toJson(error);
               return jsonError;
                }
        }
        else {
            error.setErrorStatus("Error");
            error.setError("This account :" + accounts.getName() + ": does not exists");
            logger.info("This account :" + accounts.getName() + ": does not exists");
            jsonError = gson.toJson(error);
            return jsonError;
        }
    }

    public String update(Accounts accounts) {
        Accounts accountExist = mongoOperation.findOne(new Query(Criteria.where("id").is(accounts.getId())), Accounts.class);
        logger.info("Updation request for Account: "+accountExist.getName());
        accountsRepository.delete(accountExist);
        if(!accounts.getName().equalsIgnoreCase(accountExist.getName())) {
            List<ProductMain> productMainList = mongoOperation.find(new Query(Criteria.where("assignedChartofAccounts").is(accountExist.getName())), ProductMain.class);
            for(ProductMain product : productMainList){
                product.setAssignedChartofAccounts(accounts.getName());
                productMainRepository.save(product);
            }
        }
        return save(accounts);
    }
}
