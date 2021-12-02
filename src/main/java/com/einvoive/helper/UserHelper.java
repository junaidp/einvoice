package com.einvoive.helper;

import com.einvoive.model.ErrorCustom;
import com.einvoive.model.User;
import com.einvoive.repository.UserRepository;
import com.google.gson.Gson;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class UserHelper {

    @Autowired
    MongoClient mongoClient;

    @Value("${spring.data.mongodb.database}")
    private String db;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MongoOperations mongoOperation;

    Gson gson = new Gson();

//    private List<User> users;

    public String saveUser(User userEntity){
        String msg = validationBeforeSave(userEntity);
        ErrorCustom error = new ErrorCustom();
        String jsonError;
        if(msg == null || msg.isEmpty()) {
//            userEntity.setUserId(getAvaiablaeId());
            try {
//                userEntity.setPassword(Utility.encrypt(userEntity.getPassword()));
                userRepository.save(userEntity);
                return "User Saved";
            }
            catch (Exception ex){
                error.setErrorStatus("Error");
                error.setError(ex.getMessage());
                jsonError = gson.toJson(error);
                return jsonError;
            }
        }
        else{
            error.setErrorStatus("Error");
            error.setError(msg+"--Already Exists");
            jsonError = gson.toJson(error);
            return jsonError;
        }
//        return msg+"--Already Exists";
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
            System.out.println("Error in getting Users:"+ ex);
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
            System.out.println("Error in getting Users:"+ ex);
        }
        return gson.toJson(users);
    }

    public String deleteUser(String iD){
        List<User> userList = mongoOperation.find(new Query(Criteria.where("id").is(iD)), User.class);
        userRepository.deleteAll(userList);
        return "User deleted";
    }

    public String getAvaiablaeId() {
        Long total = userRepository.count();
        int count = total.intValue();
        return count+1+"";
    }

    public String signInUser(User user) {
        System.out.println(user.getEmail() + "," + user.getPassword());
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(user.getEmail()).and("password").is(user.getPassword()));
        User savedUser = mongoOperation.findOne(query, User.class);
//        rollsHelper.getRolls(savedUser.getUserId());
//        for(Rolls roll:rollsHelper.getRollsArrayList()){
//         savedUser.getListRoles().add(roll.getRollName());
//        }
        return gson.toJson(savedUser);
    }

    public String updateUser(User userEntity) {
        User userSaved = mongoOperation.findOne(new Query(Criteria.where("id").is(userEntity.getId())), User.class);
        userEntity.setPassword(userSaved.getPassword());
        userRepository.delete(userSaved);
        return saveUser(userEntity);
    }

    //TODO better to use one updateUser Method , but not sure why we are deleting in the above method
    //TODO CONFIRM If we nee to send here id , OR UserID  (dont know why we have 2)
    public void updateUserForToken(String id, String randomNumber) {
        User user = mongoOperation.findOne(new Query(Criteria.where("userId").is(id)), User.class);
//        userRepository.save(user);
        user.setLoginToken(randomNumber);
        updateUser(user);
    }

    public String getUserToken(String email, String token){
       User user = mongoOperation.findOne(new Query(Criteria.where("email").is(email)), User.class);
       if(token.equals(user.getLoginToken())){
           return gson.toJson(user);
        }
       else return gson.toJson("Wrong token entered");
    }

    public void updateRecord(){
        MongoDatabase mongoDatabase = mongoClient.getDatabase(db);
      //  mongoDatabase.getCollection("user").updateOne(user)

    }
}
