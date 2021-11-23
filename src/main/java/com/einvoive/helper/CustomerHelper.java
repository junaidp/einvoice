package com.einvoive.helper;

import com.einvoive.model.Customer;
import com.einvoive.model.ErrorCustom;
import com.einvoive.repository.CustomerRepository;
import com.google.gson.Gson;
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

    Gson gson = new Gson();
//    private List<Customer> customers;

    public String save(Customer customerEnglish, Customer customerArabic) {
        ErrorCustom error = new ErrorCustom();
        String jsonError;
        String msg = validationBeforeSave(customerEnglish);
        if(msg == null || msg.isEmpty()) {
            try {
                saveCustomerArabic(customerEnglish, customerArabic);
                repository.save(customerEnglish);
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
        List<Customer> phoneList = mongoOperation.find(new Query(Criteria.where("phone").is(customer.getPhone())
                .and("companyID").is(customer.getCompanyID())), Customer.class);
        List<Customer> phoneMainList = mongoOperation.find(new Query(Criteria.where("phoneMain").is(customer.getPhoneMain())
                .and("companyID").is(customer.getCompanyID())), Customer.class);
        List<Customer> emailList = mongoOperation.find(new Query(Criteria.where("email").is(customer.getEmail())
                .and("companyID").is(customer.getCompanyID())), Customer.class);
        List<Customer> customerVatNo = mongoOperation.find(new Query(Criteria.where("vatNumber_Customer").is(customer.getVatNumber_Customer())
                .and("companyID").is(customer.getCompanyID())), Customer.class);
        if(phoneList.size() > 0)
            msg1 = "--Customer Phone No";
        if(emailList.size() > 0)
            msg2 = "--Customer Email";
        if(phoneMainList.size() > 0)
            msg3 = "--Customer Phone Main No";
        if(customerVatNo.size() > 0)
            msg3 = "--Customer VAT No";
        msg = msg1 + msg2 + msg3;
        return msg;
    }

    public String update(Customer customerEnglish, Customer customerArabic){
        deleteCustomers(customerEnglish.getId());
        return save(customerEnglish, customerArabic);
    }

    private void saveCustomerArabic(Customer customerEnglish, Customer customerArabic){
        translationHelper.mergeAndSave(customerEnglish.getFirstName(), customerArabic.getFirstName());
        translationHelper.mergeAndSave(customerEnglish.getLastName(), customerArabic.getLastName());
        translationHelper.mergeAndSave(customerEnglish.getCustomer(), customerArabic.getCustomer());
        translationHelper.mergeAndSave(customerEnglish.getBillingAddress1(), customerArabic.getBillingAddress1());
        translationHelper.mergeAndSave(customerEnglish.getBillingAddress2(), customerArabic.getBillingAddress2());
        translationHelper.mergeAndSave(customerEnglish.getNotes(), customerArabic.getNotes());
        translationHelper.mergeAndSave(customerEnglish.getBillingCountry(), customerArabic.getBillingCountry());
        translationHelper.mergeAndSave(customerEnglish.getBillingProvince(), customerArabic.getBillingProvince());
        translationHelper.mergeAndSave(customerEnglish.getBillingCity(), customerArabic.getBillingCity());
        translationHelper.mergeAndSave(customerEnglish.getBillingPostal(), customerArabic.getBillingPostal());
        translationHelper.mergeAndSave(customerEnglish.getShippingAddress1(), customerArabic.getShippingAddress1());
        translationHelper.mergeAndSave(customerEnglish.getShippingAddress2(), customerArabic.getShippingAddress2());
        translationHelper.mergeAndSave(customerEnglish.getShippingName(), customerArabic.getShippingName());
        translationHelper.mergeAndSave(customerEnglish.getShippingCountry(), customerArabic.getShippingCountry());
        translationHelper.mergeAndSave(customerEnglish.getShippingProvince(), customerArabic.getShippingProvince());
        translationHelper.mergeAndSave(customerEnglish.getShippingCity(), customerArabic.getShippingCity());
        translationHelper.mergeAndSave(customerEnglish.getShippingPostal(), customerArabic.getShippingPostal());
        translationHelper.mergeAndSave(customerEnglish.getDeliveryInstructions(), customerArabic.getDeliveryInstructions());
    }

    private Customer getCustomerArabic(Customer customerEnglish) {
        Customer customerArabic = new Customer();
        customerArabic.setFirstName(translationHelper.getTranslationMain(customerEnglish.getFirstName()));
        customerArabic.setLastName(translationHelper.getTranslationMain(customerEnglish.getLastName()));
        customerArabic.setCustomer(translationHelper.getTranslationMain(customerEnglish.getCustomer()));
        customerArabic.setBillingAddress1(translationHelper.getTranslationMain(customerEnglish.getBillingAddress1()));
        customerArabic.setBillingAddress2(translationHelper.getTranslationMain(customerEnglish.getBillingAddress2()));
        customerArabic.setNotes(translationHelper.getTranslationMain(customerEnglish.getNotes()));
        customerArabic.setBillingCountry(translationHelper.getTranslationMain(customerEnglish.getBillingCountry()));
        customerArabic.setBillingProvince(translationHelper.getTranslationMain(customerEnglish.getBillingProvince()));
        customerArabic.setBillingCity(translationHelper.getTranslationMain(customerEnglish.getBillingCity()));
        customerArabic.setBillingPostal(translationHelper.getTranslationMain(customerEnglish.getBillingPostal()));
        customerArabic.setShippingAddress1(translationHelper.getTranslationMain(customerEnglish.getShippingAddress1()));
        customerArabic.setShippingAddress2(translationHelper.getTranslationMain(customerEnglish.getShippingAddress2()));
        customerArabic.setShippingName(translationHelper.getTranslationMain(customerEnglish.getShippingName()));
        customerArabic.setShippingCountry(translationHelper.getTranslationMain(customerEnglish.getShippingCountry()));
        customerArabic.setShippingProvince(translationHelper.getTranslationMain(customerEnglish.getShippingProvince()));
        customerArabic.setShippingCity(translationHelper.getTranslationMain(customerEnglish.getShippingCity()));
        customerArabic.setShippingPostal(translationHelper.getTranslationMain(customerEnglish.getShippingPostal()));
        customerArabic.setDeliveryInstructions(translationHelper.getTranslationMain(customerEnglish.getDeliveryInstructions()));
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
        Customer customerEnglish = null;
        Customer customerArabic = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("id").is(customerID));
            System.out.println("QUERY");
            customerEnglish = mongoOperation.findOne(query, Customer.class);
            if(customerEnglish != null)
                customerArabic = getCustomerArabic(customerEnglish);
        }catch(Exception ex){
            System.out.println("Error in get Customers:"+ ex);
        }
        List<Customer> listCustomers = new ArrayList<>();
        listCustomers.add(customerEnglish);
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

    public String deleteCustomers(String customerID){
        Customer customer = mongoOperation.findOne(new Query(Criteria.where("id").is(customerID)), Customer.class);
        assert customer != null;
        repository.delete(customer);
        return "Customer deleted";
    }

}



   /* Query query = new Query();
            query.addCriteria(Criteria.where("name").is(name).and("password").is(password));
                    //	BasicQuery query1 = new BasicQuery("{ name : '"+name+"'} , { password: '"+password+"'}");
                    System.out.println("ff");
                    User user = mongoOperation.findOne(query, User.class);*/

