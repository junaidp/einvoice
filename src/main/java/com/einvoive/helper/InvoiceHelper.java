package com.einvoive.helper;

import com.einvoive.model.Customer;
import com.einvoive.model.Invoice;
import com.einvoive.repository.CustomerRepository;
import com.einvoive.repository.InvoiceRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InvoiceHelper {

    @Autowired
    InvoiceRepository repository;

    @Autowired
    MongoOperations mongoOperation;


    Gson gson = new Gson();

    public String save(Invoice invoice){
        try {
            repository.save(invoice);
            Invoice savedInvoice = repository.findInvoiceByName(invoice.getInvoiceNumber());
            return savedInvoice.getId();
        }catch(Exception ex){
            return "Invoice Not saved"+ ex;
        }
    }

    public String getAllInvoices(String userId){
        List<Invoice> invoices = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("userId").is(userId));
            invoices = mongoOperation.find(query, Invoice.class);
        }catch(Exception ex){
            System.out.println("Error in get invoices:"+ ex);
        }
        return gson.toJson(invoices);
    }

    public String update(Invoice invoice) {
        try {
            repository.save(invoice);
            return "Invoice Updated";
        }catch(Exception ex){
            return "Invoice Not Updated"+ ex;
        }
    }
}
