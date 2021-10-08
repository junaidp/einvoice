package com.einvoive.helper;

import com.einvoive.model.*;
import com.einvoive.repository.InvoiceRepository;
import com.mongodb.client.MongoCollection;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.*;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Component
public class InvoiceHelper {

    @Autowired
    InvoiceRepository repository;

    @Autowired
    LineItemHelper lineItemHelper;

    @Autowired
    MongoOperations mongoOperation;

    @Autowired
     InvoiceRepository invoiceRepository;

    Gson gson = new Gson();

    private  List<Invoice> invoiceListMain = null;

    public String save(Invoice invoice){
        ErrorCustom error = new ErrorCustom();
        String jsonError;
        try {
//            invoice.setId(getAvaiablaeId());
            repository.save(invoice);
            for(LineItem lineItem : invoice.getLineItemList()){
                lineItem.setInvoiceId(invoice.getId());
                lineItemHelper.save(lineItem);
            }
            return "Invoice saved";
        }catch(Exception ex){
            error.setErrorStatus("Error");
            error.setError(ex.getMessage());
            jsonError = gson.toJson(error);
            return jsonError;
        }
    }

    public String getInvoicesByCompany(String companyID){
        List<Invoice> invoices = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("companyID").is(companyID));
            invoices = mongoOperation.find(query, Invoice.class);
            for(Invoice invoice : invoices) {
                lineItemHelper.getItems(invoice.getId());
                invoice.setLineItemList(lineItemHelper.getLineItems());
            }
        }catch(Exception ex){
            System.out.println("Error in get invoices:"+ ex);
        }
        return gson.toJson(invoices);
    }

//    public String getTopCustomerInvoices(Date start, Date end){
//        for()
//    }

    public String getTopCustomerInvoices(String companyID){
       List<TopCustomersInvoices> topCustomersInvoicesList = new ArrayList<TopCustomersInvoices> ();
        try{
            List<Invoice> invoiceList = null;
            invoiceListMain = mongoOperation.find(new Query(Criteria.where("companyID").is(companyID)), Invoice.class);
            while (invoiceListMain.size() != 0){
                invoiceList = null;
                invoiceList = mongoOperation.find(new Query(Criteria.where("customerID").is(invoiceListMain.get(0).getCustomerID())
                        .and("companyID").is(companyID)), Invoice.class);
                TopCustomersInvoices topCustomersInvoices = computeInvoiceSum(invoiceList);
                if(topCustomersInvoices != null)
                    topCustomersInvoicesList.add(topCustomersInvoices);
                checkRemoveExisting(invoiceList);
           }
        }catch(Exception ex){
            System.out.println("Error in get invoices:"+ ex);
        }
        String test = gson.toJson(topCustomersInvoicesList);
        return test;
    }

    private TopCustomersInvoices computeInvoiceSum(List<Invoice> invoiceList) {
        TopCustomersInvoices topCustomersInvoices = new TopCustomersInvoices();
        int sum = 0;
        for(Invoice invoice:invoiceList){
            sum += Integer.parseInt(invoice.getTotalAmountDue());
        }
        Customer customer = mongoOperation.findOne(new Query(Criteria.where("_id").is(invoiceList.get(0).getCustomerID())), Customer.class);
        topCustomersInvoices.setCustomerName(customer.getCustomer());
        topCustomersInvoices.setInvoiceTotal(String.valueOf(sum));
        return topCustomersInvoices;
    }

    private void checkRemoveExisting(List<Invoice> invoiceList){
        for(Invoice invoiceToRemove : invoiceList){
            for(int i=0; i< invoiceListMain.size(); i++){
                Invoice inv = invoiceListMain.get(i);
                if(inv.getId().equals(invoiceToRemove.getId()))
                    invoiceListMain.remove(inv);

            }
        }
    }

    public String deleteInvoice(String invoiceID){
        List<Invoice> invoices = mongoOperation.find(new Query(Criteria.where("id").is(invoiceID)), Invoice.class);
        repository.deleteAll(invoices);
        return "Invoice deleted";
    }

    public String getAvaiablaeId() {
        Long total = repository.count();
        int count = total.intValue();
        return count+1+"";
    }

    public String update(Invoice invoice) {
            return save(invoice);
    }

    public String getInvoiceStatus(String id) {
        Invoice invoice = mongoOperation.findOne(new Query(Criteria.where("id").is(id)), Invoice.class);
        return invoice.getStatus();
    }

    public String setInvoiceStatus(String id, String status) {
        ErrorCustom error = new ErrorCustom();
        String jsonError;
        try {
            Invoice invoice = mongoOperation.findOne(new Query(Criteria.where("id").is(id)), Invoice.class);
            if(invoice != null){
                invoice.setStatus(status);
                repository.save(invoice);
                return "Invoice Status Updated";
            }
            else{
                error.setErrorStatus("Error");
                error.setError("No Invoice against this ID");
                jsonError = gson.toJson(error);
                return jsonError;
            }
        }
        catch (Exception ex){
            error.setErrorStatus("Error");
            error.setError(ex.getMessage());
            jsonError = gson.toJson(error);
            return jsonError;
        }
    }

    public String getInvoicesByID(String id) {
        List<Invoice> invoicesList = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("id").is(id));
            invoicesList = mongoOperation.find(query, Invoice.class);
        }catch(Exception ex){
            System.out.println("Error in get invoices By ID :"+ ex);
        }
        return gson.toJson(invoicesList);
    }
}
