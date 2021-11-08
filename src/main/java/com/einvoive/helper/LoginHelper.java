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

import java.util.ArrayList;
import java.util.List;

@Component
public class LoginHelper {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CompanyHelper companyHelper;

    @Autowired
    UserHelper userHelper;

    @Autowired
    MongoOperations mongoOperation;

    Gson gson = new Gson();

//    private String companyID;
//
//    private String loggedInUserID;

    public String signIn(Login login) {
        List<Company> companyList = new ArrayList<>();
        List<User> userList = new ArrayList<>();
        System.out.println(login.getEmail() + "," + login.getPassword());
        Company loginCompany = mongoOperation.findOne(new Query((Criteria.where("email").is(login.getEmail()).and("password").is(login.getPassword()))), Company.class);
        if(loginCompany != null){
            companyList.add(loginCompany);
            Constant.COMPANY_ID = loginCompany.getCompanyID();
            Constant.LOGGED_IN_USER_ID = loginCompany.getId();
            try{
                companyList.add(companyHelper.getCompanyArabic(loginCompany));
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            return gson.toJson(companyList);
        }
        User savedUser = mongoOperation.findOne(new Query(Criteria.where("email").is(login.getEmail()).and("password").is(login.getPassword())), User.class);
        if(savedUser != null){
           userList.add(savedUser);
           Constant.COMPANY_ID = savedUser.getCompanyID();
           Constant.LOGGED_IN_USER_ID = savedUser.getId();
//           try{
//               userList.add(userHelper.get(loginCompany));
//              } catch (Exception ex) {
//                System.out.println(ex.getMessage());
//              }
              return gson.toJson(userList);
        }
        else
            return gson.toJson("Invalid Credentials");
    }

//    public String getCompanyID() {
//        return companyID;
//    }
//
//    public String getLoggedInUserID() {
//        return loggedInUserID;
//    }
}
