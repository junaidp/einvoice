package com.einvoive.helper;

import com.einvoive.constants.Constants;
import com.einvoive.model.*;
import com.einvoive.repository.CompanyRepository;
import com.einvoive.repository.UserRepository;
import com.einvoive.util.Utility;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private Logger logger = LoggerFactory.getLogger(UpdatePasswordHelper.class);
    @Autowired
    UserRepository userRepository;
    @Autowired
    CompanyHelper companyHelper;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    UserHelper userHelper;
    @Autowired
    MongoOperations mongoOperation;
    Gson gson = new Gson();
    @Autowired
    private LogsHelper logsHelper;

    public String changePassword(UpdatePassword updatePassword) {
        Company loginCompany = mongoOperation.findOne(new Query((Criteria.where("email").is(updatePassword.getEmail()).and("password").is(updatePassword.getPassword()))), Company.class);
        if(loginCompany != null){
            loginCompany.setPassword(updatePassword.getNewPassword());
            companyRepository.save(loginCompany);
            logger.info("Change Password request for Company "+loginCompany.getCompanyName());
            logsHelper.save(new Logs("Change Password request for Company "+loginCompany.getCompanyName()," and Email "+loginCompany.getEmail()));
            return gson.toJson("Your Company Password has been updated!");
        }
        User savedUser = mongoOperation.findOne(new Query(Criteria.where("email").is(updatePassword.getEmail()).and("password").is(updatePassword.getPassword())), User.class);
        if(savedUser != null){
            savedUser.setPassword(updatePassword.getNewPassword());
            userRepository.save(savedUser);
            logger.info("Change Password request for User "+savedUser.getName());
            logsHelper.save(new Logs("Change Password request for User "+savedUser.getName()," and Email "+loginCompany.getEmail()));
            return gson.toJson("User Password has been updated!");
        }
        else
            return gson.toJson("Invalid Credentials");
    }

}
