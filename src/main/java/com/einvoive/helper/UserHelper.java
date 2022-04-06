package com.einvoive.helper;

import com.einvoive.authenticator.CredentialRepository;
import com.einvoive.authenticator.UserTOTP;
import com.einvoive.model.*;
import com.einvoive.repository.UserRepository;
import com.einvoive.util.EmailSender;
import com.einvoive.util.Utility;
import com.google.gson.Gson;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.ConcurrentMap;


@Component
public class UserHelper {

    @Autowired
    MongoClient mongoClient;

    @Value("${spring.data.mongodb.database}")
    private String db;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailSender emailSender;

    @Autowired
    MongoOperations mongoOperation;

    @Autowired
    CompanyHelper companyHelper;

    @Autowired
    LogsHelper logsHelper;

    Gson gson = new Gson();

    private Logger logger = LoggerFactory.getLogger(UserHelper.class);

//    private List<User> users;

    public String saveUser(User userEntity, boolean sendEmail){
        String msg = validationBeforeSave(userEntity);
        ErrorCustom error = new ErrorCustom();
        String jsonError;
        if(msg == null || msg.isEmpty()) {
            try {
                Company company = companyHelper.getCompanyObject(userEntity.getCompanyID());
                if(company.getLimitUsers() == null || Integer.parseInt(company.getLimitUsers()) > getCompanyTotalUsers(userEntity.getCompanyID())) {
                    if(userEntity.getPassword().length() != 64)
                        userEntity.setPassword(Utility.encrypt(userEntity.getPassword()));
                     userRepository.save(userEntity);
                    logsHelper.save(new Logs("New user added for "+company.getCompanyName(), company.getCompanyName()+ " has added a new user "+ userEntity.getName()));
                    if(sendEmail)
                        emailSender.sendEmail(userEntity.getEmail(), "Account Created", "Your account has been created successfully. Please log in using these credential.\n Email Address is: "+userEntity.getEmail()+ "\n Password is: "+userEntity.getPassword());
                    return "User Saved";
                }else{
                    error.setErrorStatus("Error");
                    logger.info("Sorry Company has a limit of generating "+company.getLimitUsers()+" Users");
                    error.setError("Sorry Company has a limit of generating "+company.getLimitUsers()+" Users");
                    jsonError = gson.toJson(error);
                    return jsonError;
                }
            }
            catch (Exception ex){
                logger.info("Exception in save User: "+ex.getMessage());
                error.setErrorStatus("Error");
                error.setError(ex.getMessage());
                jsonError = gson.toJson(error);
                return jsonError;
            }
        }
        else{
            error.setErrorStatus("Error");
            logger.info(msg+"--Already Exists");
            error.setError(msg+"--Already Exists");
            jsonError = gson.toJson(error);
            return jsonError;
        }
//        return msg+"--Already Exists";
    }

    private int getCompanyTotalUsers(String companyID){
        Query query = new Query();
        query.addCriteria(Criteria.where("companyID").is(companyID));
        List<User>users = mongoOperation.find(query, User.class);
        return users.size();
    }

    private String validationBeforeSave(User user) {
        String msg = null;
        String msg1 = "";
        String msg2 = "";
        String msg3 = "";
        List<User> phoneList = mongoOperation.find(new Query(Criteria.where("phone").is(user.getPhone())
                .and("companyID").is(user.getCompanyID())), User.class);
        List<User> userId = mongoOperation.find(new Query(Criteria.where("userId").is(user.getUserId())
                .and("companyID").is(user.getCompanyID())), User.class);
        List<User> emailList = mongoOperation.find(new Query(Criteria.where("email").is(user.getEmail())
                .and("companyID").is(user.getCompanyID())), User.class);
        if(phoneList.size() > 0)
            msg1 = "--User Phone No";
        if(emailList.size() > 0)
            msg2 = "--User Email";
        if(userId.size() > 0)
            msg3 = "--User ID";
        msg = msg1 + msg2 + msg3;
        return msg;
    }

    public String getAllUsers(String companyID){
        List<User> userList = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("companyID").is(companyID));
            System.out.println("QUERY");
            userList = mongoOperation.find(query, User.class);
        }catch(Exception ex){
            logger.info("Error in getting Users:"+ ex.getMessage());
            System.out.println("Error in getting Users:"+ ex.getMessage());
        }
        return gson.toJson(userList);
    }

    public String getUserByUserID(String userID){
        List<User> users = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("id").is(userID));
            System.out.println("QUERY");
            users = mongoOperation.find(query, User.class);
        }catch(Exception ex){
            logger.info("Error in getting Users:"+ ex.getMessage());
            System.out.println("Error in getting Users:"+ ex.getMessage());
        }
        return gson.toJson(users);
    }

    public String deleteUser(String iD){
        List<User> userList = mongoOperation.find(new Query(Criteria.where("id").is(iD)), User.class);
        userRepository.deleteAll(userList);
        logger.info("User deleted: "+userList.get(0).getName());
        return "User deleted";
    }

    public String getAvaiablaeId() {
        Long total = userRepository.count();
        int count = total.intValue();
        return count+1+"";
    }

//    public String signInUser(User user) {
//        System.out.println(user.getEmail() + "," + user.getPassword());
//        Query query = new Query();
//        query.addCriteria(Criteria.where("email").is(user.getEmail()).and("password").is(user.getPassword()));
//        User savedUser = mongoOperation.findOne(query, User.class);
//        rollsHelper.getRolls(savedUser.getUserId());
//        for(Rolls roll:rollsHelper.getRollsArrayList()){
//         savedUser.getListRoles().add(roll.getRollName());
//        }
//        return gson.toJson(savedUser);
//    }

    public String updateUser(User userEntity) {
        User userSaved = mongoOperation.findOne(new Query(Criteria.where("id").is(userEntity.getId())), User.class);
        userEntity.setPassword(userSaved.getPassword());
        userRepository.delete(userSaved);
        logger.info("User updation: "+userEntity.getName());
        return saveUser(userEntity, false);
    }

    //TODO better to use one updateUser Method , but not sure why we are deleting in the above method
    //TODO CONFIRM If we nee to send here id , OR UserID  (dont know why we have 2)
    public void updateUserForToken(User savedUser) {
        Update update = new Update();
        update.set("loginToken", savedUser.getLoginToken());
        mongoOperation.updateFirst(new Query(Criteria.where("userId").is(savedUser.getUserId())), update, User.class);
        logger.info("Token: " + savedUser.getLoginToken() +" saved for user : " + savedUser.getUserId());
    }

    public String resetUserPassword(Login login) {
        Update update = new Update();
        try {
            update.set("password", Utility.encrypt(login.getPassword()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            logger.info("No Hashing Algorithm Exception: "+e.getMessage());
        }
        mongoOperation.updateFirst(new Query(Criteria.where("email").is(login.getEmail())), update, User.class);
        logger.info(" Password updated for user : " + login.getEmail());
        return gson.toJson("Password updated Successfully : " + login.getEmail());
    }

    public void updateRecord(){
        MongoDatabase mongoDatabase = mongoClient.getDatabase(db);
      //  mongoDatabase.getCollection("user").updateOne(user)
    }

    public void add2FactorAuthentication(String email) {
        User user = mongoOperation.findOne(new Query(Criteria.where("email").is(email)), User.class);
        user.setTwoFactorAuthentication(true);
        mongoOperation.save(user);
    }


    public void saveAuthenticatorInfo(UserTOTP userTOTP) {
        User user = mongoOperation.findOne(new Query(Criteria.where("email").is(userTOTP.getUsername())), User.class);
        user.setUserTOTP(userTOTP);
        mongoOperation.save(user);
    }

    public UserTOTP getUserAuthenticatorInfo(String email){
        User user = mongoOperation.findOne(new Query(Criteria.where("email").is(email)), User.class);
        return user.getUserTOTP();
    }
}
