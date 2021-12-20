package com.einvoive.helper;

import com.einvoive.model.Company;
import com.einvoive.model.Login;
import com.einvoive.model.Logs;
import com.einvoive.model.User;
import com.einvoive.repository.UserRepository;
import com.einvoive.util.EmailSender;
import com.einvoive.util.Utility;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

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

    @Autowired
    EmailSender emailSender;

    Gson gson = new Gson();

    @Autowired
    LogsHelper logsHelper;

    private boolean validated = false;

    private Logger logger = LoggerFactory.getLogger(LoginHelper.class);


//    private String companyID;
//
//    private String loggedInUserID;

    public String signIn(Login login) {
        List<Company> companyList = new ArrayList<>();
        List<User> userList = new ArrayList<>();
        logger.info("Inside SignIn for: " + login.getEmail() + "," + login.getPassword());
        Company loginCompany = mongoOperation.findOne(new Query(Criteria.where("email").is(login.getEmail()).and("password").is(login.getPassword())), Company.class);
        if (loginCompany != null) {
            //DIrect Login
//            companyList.add(loginCompany);
//            try{
//                companyList.add(companyHelper.getCompanyArabic(loginCompany));
//            } catch (Exception ex) {
//                System.out.println(ex.getMessage());
//            }
//            return gson.toJson(companyList);
            saveCompanyTokenAndEmail(loginCompany);
            companyList.add(loginCompany);
            logsHelper.save(new Logs("SingIn request for "+loginCompany.getCompanyName(),"A taken has been sent to "+loginCompany.getEmail()));
            return gson.toJson(companyList);
        }
        User savedUser = mongoOperation.findOne(new Query(Criteria.where("email").is(login.getEmail()).and("password").is(login.getPassword())), User.class);
        if (savedUser != null) {
            saveTokenAndEmail(savedUser);
            userList.add(savedUser);
            logsHelper.save(new Logs("SingIn request for "+savedUser.getName()," A taken has been sent to "+savedUser.getEmail()));
            return gson.toJson(userList);
        } else
            return gson.toJson("Invalid Credentials");
    }

    //User
    private void saveTokenAndEmail(User savedUser) {
        String randomNumber = Utility.getRandomNumber();
        savedUser.setLoginToken(randomNumber);
        userHelper.updateUserForToken(savedUser);
        emailSender.sendEmail(savedUser.getEmail(), "Gofatoorah Login Verification", "Login Token :" + randomNumber);

    }

    //Company
    private void saveCompanyTokenAndEmail(Company company) {
        String randomNumber = Utility.getRandomNumber();
        company.setLoginToken(randomNumber);
        companyHelper.updateCompanyForToken(company);
        emailSender.sendEmail(company.getEmail(), "Gofatoorah Login Verification", "Login Token :" + randomNumber);
    }

    public String getLoginToken(String email, String token) {
        User user = mongoOperation.findOne(new Query(Criteria.where("email").is(email)), User.class);
        if (user != null && token.equals(user.getLoginToken())) {
            logsHelper.save(new Logs("SingIn token validation for "+user.getName(),"Token Authenticated against "+user.getEmail()));
            return gson.toJson(user);
        }
        Company company = mongoOperation.findOne(new Query(Criteria.where("email").is(email)), Company.class);
        if (company != null && token.equals(company.getLoginToken())) {
            logsHelper.save(new Logs("SingIn token validation for "+company.getCompanyName(),"Token Authenticated against "+company.getEmail()));
            return gson.toJson(company);
        } else return gson.toJson("Wrong token entered");
    }

    //forget password
    public String forgetPassword(Login login) {
        Company loginCompany = mongoOperation.findOne(new Query(Criteria.where("email").is(login.getEmail())), Company.class);
        if (loginCompany != null) {
            saveCompanyTokenAndEmail(loginCompany);

            return gson.toJson("A code has been sent to your email.");
        }
        User savedUser = mongoOperation.findOne(new Query(Criteria.where("email").is(login.getEmail())), User.class);
        if (savedUser != null) {
            saveTokenAndEmail(savedUser);
            return gson.toJson("A code has been sent to your email.");
        } else
            return gson.toJson("Invalid Email address");
    }

    //update password
    public String validateUpdatePassword(Login login) {
        Company loginCompany = mongoOperation.findOne(new Query(Criteria.where("email").is(login.getEmail()).and("loginToken").is(login.getPassword())), Company.class);
        if (loginCompany != null) {
            validated = true;
            logsHelper.save(new Logs("Forget Password request validated for "+loginCompany.getCompanyName()," against "+loginCompany.getEmail()));
            return gson.toJson("Validation Successfull");
        }
        User savedUser = mongoOperation.findOne(new Query(Criteria.where("email").is(login.getEmail()).and("loginToken").is(login.getPassword())), User.class);
        if (savedUser != null) {
            validated = true;
            logsHelper.save(new Logs("Forget Password request validated for "+savedUser.getName()," against "+savedUser.getEmail()));
            return gson.toJson("Validation Successfull");
        } else
            return gson.toJson("Invalid Token");
    }

    //update password
    public String resetPassword(Login login) {
        Company loginCompany = mongoOperation.findOne(new Query(Criteria.where("email").is(login.getEmail())), Company.class);
        if (loginCompany != null && validated) {
            logsHelper.save(new Logs("Forget Password request for "+loginCompany.getCompanyName()," and token has been sent to "+loginCompany.getEmail()));
            return companyHelper.resetCompanyPassword(login);
        }
        User savedUser = mongoOperation.findOne(new Query(Criteria.where("email").is(login.getEmail())), User.class);
        if (savedUser != null && validated) {
            logsHelper.save(new Logs("Forget Password request for "+Utility.getUserName(savedUser.getUserId(), mongoOperation)," nd token has been sent to "+savedUser.getEmail()));
            return userHelper.resetUserPassword(login);
        } else
            return gson.toJson("Invalid Credentials");
    }

}
