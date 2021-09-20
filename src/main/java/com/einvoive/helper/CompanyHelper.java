package com.einvoive.helper;

import com.einvoive.model.Company;
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
        if(validationBeforeSave(company) != null) {
            companyRepository.save(company);
        }
        return "saved";
    }

    private String validationBeforeSave(Company company) {
        String msg = null;
        List<Company> companyIDList = mongoOperation.find(new Query(Criteria.where("companyID").is(company.getCompanyID())), Company.class);
        List<Company> companyNamesList = mongoOperation.find(new Query(Criteria.where("companyName").is(company.getCompanyName())), Company.class);
        List<Company> companyEmailList = mongoOperation.find(new Query(Criteria.where("email").is(company.getEmail())), Company.class);
        if(companyList.size() > 0)
            msg = true;
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
        try {
            companyRepository.save(userCompany);
        }catch(Exception ex){
            return "Company Not Updated"+ ex;
        }
        return "Company updated";
    }

    public String uploadCompanyLogo(String filePath, String companyID){
        String msg = "Unsuccessfull";
        List<Company> companyList = mongoOperation.find(new Query(Criteria.where("companyID").is(companyID)), Company.class);
        for(Company company : companyList)    {
            company.setLogo(filePath);
            msg = "Successfully Uploaded LOGO";
        }
        return msg;
    }

}
