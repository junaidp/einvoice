package com.einvoive.helper;

import com.einvoive.model.Customer;
import com.einvoive.model.ErrorCustom;
import com.einvoive.model.Logs;
import com.einvoive.repository.CustomerRepository;
import com.einvoive.util.Utility;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomerHelper {

    @Autowired
    CustomerRepository repository;
    @Autowired
    TranslationHelper translationHelper;
    @Autowired
    MongoOperations mongoOperation;
    @Autowired
    LogsHelper logsHelper;
    @Autowired
    CompanyHelper companyHelper;
    Gson gson = new Gson();
    private Logger logger = LoggerFactory.getLogger(Customer.class);

    public String save(Customer customerEnglish, Customer customerArabic) {
        ErrorCustom error = new ErrorCustom();
        String jsonError;
        String msg = validationBeforeSave(customerEnglish);
        if(msg == null || msg.isEmpty()) {
            try {
                saveCustomerArabic(customerEnglish, customerArabic);
                repository.save(customerEnglish);
                logsHelper.save(new Logs("Customer added for "+ Utility.getCompanyName(customerEnglish.getCompanyID(), mongoOperation), " A new Customer "+customerEnglish.getCustomer()+" has been added for company "+Utility.getCompanyName(customerEnglish.getCompanyID(), mongoOperation)));
                return "Customer Saved";
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

    private String validationBeforeSave(Customer customer) {
        String msg = null;
        String msg1 = "";
        String msg2 = "";
        String msg3 = "";
        String msg4 = "";
//        List<Customer> phoneList = mongoOperation.find(new Query(Criteria.where("phone").is(customer.getPhone())
//                .and("companyID").is(customer.getCompanyID())), Customer.class);
//        List<Customer> phoneMainList = mongoOperation.find(new Query(Criteria.where("phoneMain").is(customer.getPhoneMain())
//                .and("companyID").is(customer.getCompanyID())), Customer.class);
//        List<Customer> emailList = mongoOperation.find(new Query(Criteria.where("email").is(customer.getEmail())
//                .and("companyID").is(customer.getCompanyID())), Customer.class);
        List<Customer> customerVatNo = mongoOperation.find(new Query(Criteria.where("vatNumber_Customer").is(customer.getVatNumber_Customer())
                .and("companyID").is(customer.getCompanyID())), Customer.class);
//        if(phoneList.size() > 0)
//            msg1 = "--Customer Phone No";
//        if(emailList.size() > 0)
//            msg2 = "--Customer Email";
//        if(phoneMainList.size() > 0)
//            msg3 = "--Customer Phone Main No";
        if(customerVatNo.size() > 0)
            msg4 = "--Customer VAT No";
//        msg = msg1 + msg2 + msg3 + msg4;
        return msg4;
    }

    public String update(Customer customerEnglish, Customer customerArabic){
        deleteCustomers(customerEnglish.getId());
//        saveCustomerArabic(customerEnglish, customerArabic);
        return save(customerEnglish, customerArabic);
    }

    private void saveCustomerArabic(Customer customerEnglish, Customer customerArabic){
        translationHelper.mergeAndSave(customerEnglish.getFirstName(), customerArabic.getFirstName());
        translationHelper.mergeAndSave(customerEnglish.getLastName(), customerArabic.getLastName());
        translationHelper.mergeAndSave(customerEnglish.getCustomer(), customerArabic.getCustomer());
        translationHelper.mergeAndSave(customerEnglish.getBillingAddress1(), customerArabic.getBillingAddress1());
        translationHelper.mergeAndSave(customerEnglish.getNotes(), customerArabic.getNotes());
        translationHelper.mergeAndSave(customerEnglish.getShippingAddress1(), customerArabic.getShippingAddress1());
        translationHelper.mergeAndSave(customerEnglish.getShippingAddress2(), customerArabic.getShippingAddress2());
        translationHelper.mergeAndSave(customerEnglish.getShippingCountry(), customerArabic.getShippingCountry());
        translationHelper.mergeAndSave(customerEnglish.getShippingProvince(), customerArabic.getShippingProvince());
        translationHelper.mergeAndSave(customerEnglish.getShippingCity(), customerArabic.getShippingCity());
        translationHelper.mergeAndSave(customerEnglish.getShippingPostal(), customerArabic.getShippingPostal());
        translationHelper.mergeAndSave(customerEnglish.getDeliveryInstructions(), customerArabic.getDeliveryInstructions());
    }

    private Customer getCustomerArabic(Customer customerEnglish) {
        Customer customerArabic = new Customer();
        customerArabic.setFirstName(translationHelper.checkAndGetTranslation(customerEnglish.getFirstName()));
        customerArabic.setLastName(translationHelper.checkAndGetTranslation(customerEnglish.getLastName()));
        customerArabic.setCustomer(translationHelper.checkAndGetTranslation(customerEnglish.getCustomer()));
        customerArabic.setBillingAddress1(translationHelper.checkAndGetTranslation(customerEnglish.getBillingAddress1()));
        customerArabic.setNotes(translationHelper.checkAndGetTranslation(customerEnglish.getNotes()));
        customerArabic.setShippingAddress1(translationHelper.checkAndGetTranslation(customerEnglish.getShippingAddress1()));
        customerArabic.setShippingAddress2(translationHelper.checkAndGetTranslation(customerEnglish.getShippingAddress2()));
        customerArabic.setShippingCountry(translationHelper.checkAndGetTranslation(customerEnglish.getShippingCountry()));
        customerArabic.setShippingProvince(translationHelper.checkAndGetTranslation(customerEnglish.getShippingProvince()));
        customerArabic.setShippingCity(translationHelper.checkAndGetTranslation(customerEnglish.getShippingCity()));
        customerArabic.setShippingPostal(translationHelper.checkAndGetTranslation(customerEnglish.getShippingPostal()));
        customerArabic.setDeliveryInstructions(translationHelper.checkAndGetTranslation(customerEnglish.getDeliveryInstructions()));
        return customerArabic;
    }

    public String getAllCustomers(String comapnyID){
        List<List<Customer>> listCustomers = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("companyID").is(comapnyID));
            System.out.println("QUERY");
            List<Customer>customers = mongoOperation.find(query, Customer.class);
            listCustomers = getCustomersInLanguages(customers);
        }catch(Exception ex){
            System.out.println("Error in get Customers:"+ ex);
        }
        return gson.toJson(listCustomers);
    }

    //only in English
    public String getAllCustomersEnglish(String comapnyID){
        List<Customer>customers = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("companyID").is(comapnyID));
            System.out.println("QUERY");
            customers = mongoOperation.find(query, Customer.class);
        }catch(Exception ex){
            System.out.println("Error in get Customers English:"+ ex);
        }
        return gson.toJson(customers);
    }

    private List<List<Customer>> getCustomersInLanguages(List<Customer> customers){
       List<List<Customer>> listCustomers = new ArrayList<>();
        List<Customer> customersArabic = new ArrayList<>();
        for(Customer customerEng : customers)
            customersArabic.add(getCustomerArabic(customerEng));
        if(customers != null && customersArabic != null){
            listCustomers.add(customers);
            listCustomers.add(customersArabic);
        }
        return listCustomers;
    }

    public String getCustomer(String customerID){
        List<Customer> listCustomers = new ArrayList<>();
        Customer customerEnglish = null;
        Customer customerArabic = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("id").is(customerID));
            System.out.println("QUERY");
            customerEnglish = mongoOperation.findOne(query, Customer.class);
            if(customerEnglish != null) {
                customerArabic = getCustomerArabic(customerEnglish);
                listCustomers.add(customerEnglish);
            }
        }catch(Exception ex){
            System.out.println("Error in get Customers:"+ ex);
        }
        listCustomers.add(customerArabic);
        return gson.toJson(listCustomers);
    }

  /*  public String getTopCustomers(){
        List<Customer> customers = null;
        try {
            Query query = new Query();
            query.limit(10)
                    .addCriteria(Criteria.where("companyID")).ord;
            System.out.println("QUERY");
            customers = mongoOperation..find(query, Customer.class);
        }catch(Exception ex){
            System.out.println("Error in get Customers:"+ ex);
        }
        return gson.toJson(customers);
    }*/

    public String getAvailableId() {
        Long total = repository.count();
        int count = total.intValue();
        return count+1+"";

    }

    public String deleteCustomers(String id){
        Customer customer = mongoOperation.findById(id, Customer.class);
        if(customer != null)
            repository.delete(customer);
        return "Customer deleted";
    }

}



   /* Query query = new Query();
            query.addCriteria(Criteria.where("name").is(name).and("password").is(password));
                    //	BasicQuery query1 = new BasicQuery("{ name : '"+name+"'} , { password: '"+password+"'}");
                    System.out.println("ff");
                    User user = mongoOperation.findOne(query, User.class);*/

