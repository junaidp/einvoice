package com.einvoive.helper;

import com.einvoive.model.Invoice;
import com.einvoive.model.LineItem;
import com.einvoive.model.ProductMain;
import com.einvoive.model.TopCustomersInvoices;
import com.einvoive.repository.LineItemRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class LineItemHelper {

    @Autowired
    LineItemRepository repository;

    @Autowired
    MongoOperations mongoOperation;

    Gson gson = new Gson();

    private List<LineItem> lineItems;

    public String save(LineItem lineItem) {
        try {
            repository.save(lineItem);
            return "product saved";
        } catch (Exception ex) {
            return "product Not saved" + ex;
        }
    }

    public String getItems(String invoiceId) {
        lineItems = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("invoiceId").is(invoiceId));
            lineItems = mongoOperation.find(query, LineItem.class);
        } catch (Exception ex) {
            System.out.println("Error in get Products:" + ex);
        }
        return gson.toJson(lineItems);
    }

    public String update(LineItem lineItem) {
        return save(lineItem);
    }

    public String deleteLineItem(String id){
        List<LineItem> lineItemList = mongoOperation.find(new Query(Criteria.where("id").is(id)), LineItem.class);
        repository.deleteAll(lineItemList);
        return "Line Item deleted";
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }
}