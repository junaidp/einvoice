package com.einvoive.helper;

import com.einvoive.model.*;
import com.einvoive.repository.CompanyRepository;
import com.einvoive.util.Translator;
import com.einvoive.util.Utility;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class CompanyHelper {

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    TranslationHelper translationHelper;

    @Autowired
    MongoOperations mongoOperation;

    @Autowired
    LogsHelper logsHelper;

    Gson gson = new Gson();

    private Logger logger = LoggerFactory.getLogger(CompanyHelper.class);

    public String saveCompany(Company companyEnglish){
        String msg = validationBeforeSave(companyEnglish);
        ErrorCustom error = new ErrorCustom();
        String jsonError;
        if(msg == null || msg.isEmpty()) {
            try {
                if(companyEnglish.getPassword().length() != 64)
                    companyEnglish.setPassword(Utility.encrypt(companyEnglish.getPassword()));
                companyEnglish.setCreationDate(new Date());
                companyRepository.save(companyEnglish);
                Company companySaved = mongoOperation.findOne(new Query(Criteria.where("companyID").is(companyEnglish.getCompanyID())), Company.class);
                logsHelper.save(new Logs("Comapny added "+companyEnglish.getCompanyName(), companyEnglish.getCompanyName()+ " has been added with companyID "+ companyEnglish.getCompanyID()+", users limit "+companyEnglish.getLimitUsers()+", Invoices limit "+companyEnglish.getLimitInvoices()));
                logger.info(companyEnglish.getCompanyName()+" saved");
                return gson.toJson(companySaved);
            } catch (Exception ex) {
                logger.info(ex.getMessage());
                error.setErrorStatus("Error");
                error.setError(ex.getMessage());
                jsonError = gson.toJson(error);
                return jsonError;
            }
        }
        else{
            error.setErrorStatus("Error");
            error.setError(msg+" already exists");
            logger.info(msg+" already exists");
            jsonError = gson.toJson(error);
            return jsonError;
         }
    }

    public String getLastCompanyId() {
        String company = null;
        try {
            Query query = new Query();
            query.with(Sort.by(Sort.Direction.DESC, "companyID")).limit(1);
            // invoiceRepository.findAll(Sort.by(Sort.Direction.DESC, "invoiceNumber"));
            company = mongoOperation.find(query, Company.class).get(0).getCompanyID();
            return company;
        }catch(Exception ex){
            System.out.println("Error in getLastCompanyId:"+ ex.getMessage());
            logger.info("Error in getLastCompanyId:"+ ex.getMessage());
            return "Error in getLastCompanyId:"+ ex.getMessage();
        }
    }

    private void saveCompanyArabic(Company companyEnglish, Company companyArabic){
        translationHelper.mergeAndSave(companyEnglish.getFirstName(), companyArabic.getFirstName());
        translationHelper.mergeAndSave(companyEnglish.getLastName(), companyArabic.getLastName());
        translationHelper.mergeAndSave(companyEnglish.getCompanyName(), companyArabic.getCompanyName());
        translationHelper.mergeAndSave(companyEnglish.getAddress1(), companyArabic.getAddress1());
        translationHelper.mergeAndSave(companyEnglish.getAddress2(), companyArabic.getAddress2());
        translationHelper.mergeAndSave(companyEnglish.getNotes(), companyArabic.getNotes());
        translationHelper.mergeAndSave(companyEnglish.getCountry(), companyArabic.getCountry());
        translationHelper.mergeAndSave(companyEnglish.getState(), companyArabic.getState());
        translationHelper.mergeAndSave(companyEnglish.getPostalCode(), companyArabic.getPostalCode());
        translationHelper.mergeAndSave(companyEnglish.getCity(), companyArabic.getCity());
        translationHelper.mergeAndSave(companyEnglish.getDistrictNeighborhood(), companyArabic.getDistrictNeighborhood());
        //translationHelper.mergeAndSave(companyEnglish.gePostal(),companyArabic.getPostalCode());
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

    //Email Verification token
//    public void updateUserForToken(String companyID, String randomNumber) {
//        Company company = mongoOperation.findOne(new Query(Criteria.where("companyID").is(companyID)), Company.class);
//        company.setLoginToken(randomNumber);
//        updateCompanyEnglish(company);
//    }

    public String getAvaiablaeId() {
        Long total = companyRepository.count();
        int count = total.intValue();
        return count+1+"";
    }

//    public String singIn(Company company) {
//        System.out.println(company.getCompanyID() + "," + company.getPassword());
//        Query query = new Query();
//        query.addCriteria(Criteria.where("email").is(company.getEmail()).and("password").is(company.getPassword()));
//        Company companyEnglish = mongoOperation.findOne(query, Company.class);
//        Company companyArabic = getCompanyArabic(companyEnglish);
//        return gson.toJson(companyEnglish);
//    }

    public Company getCompanyArabic(Company companyEnglish) {
        Company companyArabic = new Company();
        companyArabic.setFirstName(translationHelper.getTranslationMain(companyEnglish.getFirstName()));
        companyArabic.setLastName(translationHelper.getTranslationMain(companyEnglish.getLastName()));
        companyArabic.setCompanyName(translationHelper.getTranslationMain(companyEnglish.getCompanyName()));
        companyArabic.setNotes(translationHelper.getTranslationMain(companyEnglish.getNotes()));
        companyArabic.setCountry(translationHelper.getTranslationMain(companyEnglish.getCountry()));
        companyArabic.setState(translationHelper.getTranslationMain(companyEnglish.getState()));
        companyArabic.setCity(translationHelper.getTranslationMain(companyEnglish.getCity()));
        companyArabic.setAddress1(translationHelper.getTranslationMain(companyEnglish.getAddress1()));
        companyArabic.setAddress2(translationHelper.getTranslationMain(companyEnglish.getAddress2()));
        companyArabic.setPostalCode(translationHelper.getTranslationMain(companyEnglish.getPostalCode()));
        companyArabic.setDistrictNeighborhood(translationHelper.getTranslationMain(companyEnglish.getDistrictNeighborhood()));
        return companyArabic;
    }

    public String updateCompany(Company companyEnglish , Company comapnyArabic) {
        Company company = mongoOperation.findOne(new Query(Criteria.where("companyID").is(companyEnglish.getCompanyID())),Company.class);
        companyEnglish.setLimitUsers(company.getLimitUsers());
        companyEnglish.setLimitInvoices(company.getLimitInvoices());
        companyRepository.delete(company);
        saveCompany(companyEnglish);
        saveCompanyArabic(companyEnglish, comapnyArabic);
        logger.info(companyEnglish.getCompanyName()+" record updated");
        return gson.toJson("Company Updated");
    }

    public void updateCompanyForToken(Company companyEnglish) {
        Update update = new Update();
        update.set("loginToken", companyEnglish.getLoginToken());
        mongoOperation.updateFirst(new Query(Criteria.where("companyID").is(companyEnglish.getCompanyID())), update, Company.class);
        logger.info("Token: " + companyEnglish.getLoginToken() +" saved for Comapny: "+ companyEnglish.getCompanyName());
    }

    public String resetCompanyPassword(Login login) {
        Update update = new Update();
        try {
            update.set("password", Utility.encrypt(login.getPassword()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            logger.info("No Hashing Algorithm Exception "+e.getMessage());
        }
        mongoOperation.updateFirst(new Query(Criteria.where("email").is(login.getEmail())), update, Company.class);
        logger.info(" Password updated for Company : " + login.getEmail());
        return gson.toJson("Password updated successfully : " + login.getEmail());
    }

    public Company getCompanyObject(String companyID){
        Company company = mongoOperation.findOne(new Query(Criteria.where("companyID").is(companyID)),Company.class);
        return company;
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
        List<Company> companyList = new ArrayList<>();
        try{
            Company companyEnglish = mongoOperation.findOne(new Query(Criteria.where("companyID").is(companyID)),Company.class);
            Company companyArabic = getCompanyArabic(companyEnglish);
            if(companyEnglish != null)
                companyList.add(companyEnglish);
            if(companyArabic != null)
                companyList.add(companyArabic);
            return gson.toJson(companyList);
        }
        catch (Exception ex){
            System.out.println("Error in getting Company:"+ ex.getMessage());
            logger.info("Error in getting Company:"+ ex.getMessage());
            return gson.toJson(ex.getMessage());
        }
    }
}
