package com.einvoive.helper;

import com.einvoive.model.Company;
import com.einvoive.model.ErrorCustom;
import com.einvoive.model.Location;
import com.einvoive.repository.CompanyRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CompanyHelper {

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    MongoOperations mongoOperation;

    Gson gson = new Gson();

    public String saveCompany(Company company){
        String msg = validationBeforeSave(company);
        ErrorCustom error = new ErrorCustom();
        String jsonError;
        if(msg == null || msg.isEmpty()) {
            try {
                companyRepository.save(company);
                return "Company Saved";
            } catch (Exception ex) {
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
    }

    private String validationBeforeSave(Company company) {
        String msg = null;
        String msg1 = "";
        String msg2 = "";
        String msg3 = "";
        List<Company> companyIDList = mongoOperation.find(new Query(Criteria.where("companyID").is(company.getCompanyID())), Company.class);
        List<Company> companyNamesList = mongoOperation.find(new Query(Criteria.where("companyName").is(company.getCompanyName())), Company.class);
        List<Company> companyEmailList = mongoOperation.find(new Query(Criteria.where("email").is(company.getEmail())), Company.class);
        if(companyIDList.size() > 0)
            msg1 = "--Company ID";
        if(companyEmailList.size() > 0)
            msg2 = "--Company Email";
        if(companyNamesList.size() > 0)
            msg3 = "--Company Name";
        msg = msg1 + msg2 + msg3;
        return msg;
    }

    public String getAvaiablaeId() {
        Long total = companyRepository.count();
        int count = total.intValue();
        return count+1+"";
    }

    public String singIn(Company company) {
        System.out.println(company.getCompanyID() + "," + company.getPassword());
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(company.getEmail()).and("password").is(company.getPassword()));
        Company savedCompany = mongoOperation.findOne(query, Company.class);
        return gson.toJson(savedCompany);
    }

    public String updateCompany(Company userCompany) {
        return saveCompany(userCompany);
    }

//    public String uploadCompanyLogo(String filePath, String companyID){
//        String msg = "Unsuccessfull";
//        List<Company> companyList = mongoOperation.find(new Query(Criteria.where("companyID").is(companyID)), Company.class);
//        for(Company company : companyList)    {
//            company.setLogo(filePath);
//            msg = "Successfully Uploaded LOGO";
//        }
//        return msg;
//    }

    public String getCompany(String companyID) {
        try{
            Company company = mongoOperation.findOne(new Query(Criteria.where("companyID").is(companyID)),Company.class);
            return gson.toJson(company);
        }
        catch (Exception ex){
            System.out.println("Error in getting Company:"+ ex);
            return gson.toJson(ex.getMessage());
        }
    }
}
