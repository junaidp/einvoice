package com.einvoive.helper;

import com.einvoive.model.ErrorCustom;
import com.einvoive.model.Rolls;
import com.einvoive.model.User;
import com.einvoive.repository.RollsRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RollsHelper {

    @Autowired
    RollsRepository rollsRepository;

    @Autowired
    MongoOperations mongoOperation;

    Gson gson = new Gson();

    public List<Rolls> getRollsArrayList() {
        return rollsArrayList;
    }

    List<Rolls> rollsArrayList;

    public String saveRolls(Rolls rolls){
        ErrorCustom error = new ErrorCustom();
        String jsonError;
        Rolls roll = mongoOperation.findOne(new Query(Criteria.where("rollName").is(rolls.getRollName())), Rolls.class);
        if(roll != null){
            rolls.setId(getAvaiableId());
            rollsRepository.save(rolls);
            return "saved";
        }
        else{
            error.setErrorStatus("error");
            error.setError("Roll Name already saved");
            jsonError = gson.toJson(error);
            return jsonError;
        }
    }

    public String getAvaiableId() {
        Long total = rollsRepository.count();
        int count = total.intValue();
        return count+1+"";

    }

    public String getRolls(String userID){
       rollsArrayList = null;
        try {
            Query query = new Query();
            if(!userID.isEmpty())
                query.addCriteria(Criteria.where("companyID").is(userID));
            rollsArrayList = mongoOperation.find(query, Rolls.class);
        }catch(Exception ex){
            System.out.println("Error in get Products:"+ ex);
        }
        return gson.toJson(rollsArrayList);
    }

    public String deleteRolls(String userID){
        if(rollsArrayList == null){
           getRolls(userID) ;
        }
        rollsRepository.deleteAll(rollsArrayList);
        return "deletedAll";
    }


    public String updateRolls(List<String> listRollsNames, String userID) {
        try {
            deleteRolls(userID);
            for(String name : listRollsNames) {
                Rolls rolls = new Rolls();
                rolls.setUserId(userID);
                rolls.setRollName(name);
                rollsRepository.save(rolls);
            }
        }catch(Exception ex){
            return "Use Not updated"+ ex;
        }
        return "User updated";
    }

}
