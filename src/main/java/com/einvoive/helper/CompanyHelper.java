package com.einvoive.helper;

import com.einvoive.model.Company;
import com.einvoive.model.ErrorCustom;
import com.einvoive.model.Translation;
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
    TranslationHelper translationHelper;

    @Autowired
    MongoOperations mongoOperation;

    Gson gson = new Gson();

    public String saveCompany(Company companyEnglish, Company companyArabic){
        String msg = validationBeforeSave(companyEnglish);
        ErrorCustom error = new ErrorCustom();
        String jsonError;
        if(msg == null || msg.isEmpty()) {
            try {
                saveCompanyArabic(companyEnglish, companyArabic);
                companyRepository.save(companyEnglish);
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

    private void saveCompanyArabic(Company companyEnglish, Company companyArabic){
        translationHelper.mergeAndSave(companyEnglish.getFirstName(), companyArabic.getFirstName());
        translationHelper.mergeAndSave(companyEnglish.getLastName(), companyArabic.getLastName());
        translationHelper.mergeAndSave(companyEnglish.getCompanyName(), companyArabic.getCompanyName());
        translationHelper.mergeAndSave(companyEnglish.getAddress1(), companyArabic.getAddress1());
        translationHelper.mergeAndSave(companyEnglish.getAddress2(), companyArabic.getAddress2());
        translationHelper.mergeAndSave(companyEnglish.getNotes(), companyArabic.getNotes());
        translationHelper.mergeAndSave(companyEnglish.getCountry(), companyArabic.getCountry());
        translationHelper.mergeAndSave(companyEnglish.getState(), companyArabic.getState());
        translationHelper.mergeAndSave(companyEnglish.getCity(), companyArabic.getCity());
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
        Company companyEnglish = mongoOperation.findOne(query, Company.class);
        Company companyArabic = getCompanyArabic(companyEnglish);
        return gson.toJson(companyEnglish);
    }

    private Company getCompanyArabic(Company companyEnglish) {
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
        return companyArabic;
    }

    public String updateCompany(Company companyEnglish, Company userCompanyArabic) {
        Company company = mongoOperation.findOne(new Query(Criteria.where("companyID").is(companyEnglish.getCompanyID())),Company.class);
        companyRepository.delete(company);
        return saveCompany(company, userCompanyArabic);
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
