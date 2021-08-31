package com.einvoive.helper;

import com.einvoive.model.Company;
import com.einvoive.repository.CompanyRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class CompanyHelper {

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    MongoOperations mongoOperation;

    Gson gson = new Gson();

    public String saveCompany(Company company){
        company.setCompanyID(getAvaiablaeId());
        companyRepository.save(company);
        return "saved";
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

}
