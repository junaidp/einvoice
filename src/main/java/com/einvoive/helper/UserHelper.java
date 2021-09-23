package com.einvoive.helper;

import com.einvoive.model.Customer;
import com.einvoive.model.Rolls;
import com.einvoive.model.User;
import com.einvoive.repository.UserRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class UserHelper {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RollsHelper rollsHelper;

    @Autowired
    MongoOperations mongoOperation;

    Gson gson = new Gson();

    private List<User> users;

    public String saveUser(User userEntity){
        String msg = validationBeforeSave(userEntity);
        if(msg == null || msg.isEmpty()) {
//            userEntity.setUserId(getAvaiablaeId());
            userRepository.save(userEntity);
            return "User Saved";
        }
        return msg+"--Already Exists";
    }

    private String validationBeforeSave(User user) {
        String msg = null;
        String msg1 = "";
        String msg2 = "";
        String msg3 = "";
        List<User> phoneList = mongoOperation.find(new Query(Criteria.where("phone").is(user.getPhone())), User.class);
        List<User> userId = mongoOperation.find(new Query(Criteria.where("userId").is(user.getUserId())), User.class);
        List<User> emailList = mongoOperation.find(new Query(Criteria.where("email").is(user.getEmail())), User.class);
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

    public String getUser(String userID){
        users = null;
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

    public String deleteUser(String userID){
        getUser(userID);
        userRepository.deleteAll(users);
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
        try {
            userRepository.save(userEntity);
//            rollsHelper.updateRolls(userEntity.getListRoles(), userEntity.getUserId());
        }catch(Exception ex){
            return "Use Not updated"+ ex;
        }
        return "User updated";
    }
}
