package com.einvoive.helper;

import com.einvoive.model.Company;
import com.einvoive.model.Login;
import com.einvoive.model.User;
import com.einvoive.repository.UserRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import com.einvoive.constants.Constant;

@Component
public class LoginHelper {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CompanyHelper companyHelper;

    @Autowired
    MongoOperations mongoOperation;

    Gson gson = new Gson();

//    private String companyID;
//
//    private String loggedInUserID;

    public String signIn(Login login) {
        System.out.println(login.getEmail() + "," + login.getPassword());
        Company loginCompany = mongoOperation.findOne(new Query((Criteria.where("email").is(login.getEmail()).and("password").is(login.getPassword()))), Company.class);
        if(loginCompany != null){
            Constant.COMPANY_ID = loginCompany.getCompanyID();
            Constant.LOGGED_IN_USER_ID = loginCompany.getId();
            return gson.toJson(loginCompany);
        }
        else{
        User savedUser = mongoOperation.findOne(new Query(Criteria.where("email").is(login.getEmail()).and("password").is(login.getPassword())), User.class);
//        companyID = savedUser.getCompanyID();
//        loggedInUserID = savedUser.getId();
        return gson.toJson(savedUser);
        }
    }

//    public String getCompanyID() {
//        return companyID;
//    }
//
//    public String getLoggedInUserID() {
//        return loggedInUserID;
//    }
}
