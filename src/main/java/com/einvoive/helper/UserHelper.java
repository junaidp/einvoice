package com.einvoive.helper;

import com.einvoive.model.User;
import com.einvoive.repository.UserRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;


@Component
public class UserHelper {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MongoOperations mongoOperation;

    Gson gson = new Gson();

    public String saveUser(User userEntity){
        userEntity.setUserId(getAvaiablaeId());
        userRepository.save(userEntity);

        return "saved";
    }

    public String getUser(){

        return "saved";
    }

    public String getAvaiablaeId() {
        Long total = userRepository.count();
        int count = total.intValue();
        return count+1+"";

    }

    public String singIn(User user) {
        System.out.println(user.getName() + "," + user.getPassword());
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(user.getName()).and("password").is(user.getPassword()));
        User savedUser = mongoOperation.findOne(query, User.class);
        return gson.toJson(savedUser);
    }

    public String updateUser(User userEntity) {
        try {
            userRepository.save(userEntity);
        }catch(Exception ex){
            return "Use Not updated"+ ex;
        }
        return "User updated";
    }
}
