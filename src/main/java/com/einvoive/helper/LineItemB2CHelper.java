package com.einvoive.helper;

import com.einvoive.model.*;
import com.einvoive.repository.LineItemB2CRepository;
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
public class LineItemB2CHelper {

    @Autowired
    LineItemB2CRepository repository;

    @Autowired
    MongoOperations mongoOperation;

    Gson gson = new Gson();

    private List<LineItemB2C> lineItems;

    public String save(LineItemB2C lineItem) {
        try {
            repository.save(lineItem);
            return "product saved";
        } catch (Exception ex) {
            return "product Not saved" + ex;
        }
    }

    public String getLineItemsB2C(String invoiceId) {
        lineItems = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("invoiceId").is(invoiceId));
            lineItems = mongoOperation.find(query, LineItemB2C.class);
        } catch (Exception ex) {
            System.out.println("Error in get Products B2C:" + ex);
        }
        return gson.toJson(lineItems);
    }

    public String update(LineItemB2C lineItem) {
        return save(lineItem);
    }

    public String deleteLineItem(String id){
        List<LineItemB2C> lineItemList = mongoOperation.find(new Query(Criteria.where("id").is(id)), LineItemB2C.class);
        repository.deleteAll(lineItemList);
        return "Line Item B2C deleted";
    }

    public List<LineItemB2C> getLineItemsB2C() {
        return lineItems;
    }
}