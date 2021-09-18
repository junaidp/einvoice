package com.einvoive.helper;

import com.einvoive.model.Customer;
import com.einvoive.model.Invoice;
import com.einvoive.model.LineItem;
import com.einvoive.repository.CustomerRepository;
import com.einvoive.repository.InvoiceRepository;
import com.einvoive.repository.LineItemRepository;
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
    LineItemHelper lineItemHelper;

    @Autowired
    MongoOperations mongoOperation;


    Gson gson = new Gson();

    private List<Invoice> invoicesList;

    public String save(Invoice invoice){
        try {
            repository.save(invoice);
            for(LineItem lineItem : invoice.getLineItemList()){
                lineItemHelper.save(lineItem);
            }
//            Invoice savedInvoice = repository.findInvoiceByName(invoice.getInvoiceNumber());
//            return savedInvoice.getId();
            return "Invoice saved";
        }catch(Exception ex){
            return "Invoice Not saved"+ ex;
        }
    }

    public String getAllInvoices(String companyID){
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

    public String getInvoices(String invoiceID){
        invoicesList = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("id").is(invoiceID));
            invoicesList = mongoOperation.find(query, Invoice.class);
        }catch(Exception ex){
            System.out.println("Error in get invoices:"+ ex);
        }
        return gson.toJson(invoicesList);
    }

    public String getTopInvoices(){
        List<Invoice> invoices = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("totalAmountDue"));
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

    public String deleteInvoice(String invoiceID){
        List<Invoice> invoices = mongoOperation.find(new Query(Criteria.where("id").is(invoiceID)), Invoice.class);
        repository.deleteAll(invoices);
        return "Invoice deleted";
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
