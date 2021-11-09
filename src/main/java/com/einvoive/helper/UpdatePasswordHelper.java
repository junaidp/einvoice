package com.einvoive.helper;

import com.einvoive.constants.Constants;
import com.einvoive.model.Company;
import com.einvoive.model.Login;
import com.einvoive.model.UpdatePassword;
import com.einvoive.model.User;
import com.einvoive.repository.UserRepository;
import com.einvoive.util.Utility;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Component
public class UpdatePasswordHelper {

    @Autowired
    UserRepository userRepository;
    @Autowired
    CompanyHelper companyHelper;
    @Autowired
    UserHelper userHelper;
    @Autowired
    MongoOperations mongoOperation;
    Gson gson = new Gson();

    public String changePassword(UpdatePassword updatePassword) {
        List<User> userList = new ArrayList<>();
        Company loginCompany = mongoOperation.findOne(new Query((Criteria.where("email").is(updatePassword.getEmail()).and("password").is(updatePassword.getPassword()))), Company.class);
        if(loginCompany != null){
            loginCompany.setPassword(updatePassword.getNewPassword());
            companyHelper.saveCompany(loginCompany);
            Constants.COMPANY_ID = loginCompany.getCompanyID();
            Constants.LOGGED_IN_USER_ID = loginCompany.getId();
             return gson.toJson("Your Company Password has been updated!");
        }
        User savedUser = mongoOperation.findOne(new Query(Criteria.where("email").is(updatePassword.getEmail()).and("password").is(updatePassword.getPassword())), User.class);
        if(savedUser != null){
            savedUser.setPassword(updatePassword.getNewPassword());
            userHelper.saveUser(savedUser);
            Constants.COMPANY_ID = savedUser.getCompanyID();
            Constants.LOGGED_IN_USER_ID = savedUser.getId();
            return gson.toJson("User Password has been updated!");
        }
        else
            return gson.toJson("Invalid Credentials");
    }

}
