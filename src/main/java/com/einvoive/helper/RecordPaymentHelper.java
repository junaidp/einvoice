package com.einvoive.helper;

import com.einvoive.model.*;
import com.einvoive.repository.RecordPaymentRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class RecordPaymentHelper {

    @Autowired
    RecordPaymentRepository repository;
    @Autowired
    InvoiceHelper invoiceHelper;
    @Autowired
    MongoOperations mongoOperation;

    Gson gson = new Gson();

    public String save(RecordPayment recordPayment){
        ErrorCustom error = new ErrorCustom();
        String jsonError;
            try {
                repository.save(recordPayment);
                updateInvoiceRecordPayment(recordPayment);
                return "RecordPayment saved";
            }catch(Exception ex){
                error.setErrorStatus("Error");
                error.setError(ex.getMessage());
                jsonError = gson.toJson(error);
                return jsonError;
            }
     }

     private void updateInvoiceRecordPayment(RecordPayment recordPayment){
        Invoice invoice = mongoOperation.findOne(new Query(Criteria.where("invoiceNumber").is(recordPayment.getInvoiceNo())), Invoice.class);
         invoice.setRecordPayment(String.valueOf(Double.parseDouble(invoice.getRecordPayment()) - Double.parseDouble(recordPayment.getPaidAmmount())));
       InvoiceWithFile invoiceWithFile = new InvoiceWithFile();
       invoiceWithFile.setInvoice(invoice);
        //TODO: invoiceHelper.update(invoiceWithFile);
    }

    public String update(RecordPayment recordPayment) {
        RecordPayment recordPayment1 = mongoOperation.findOne(new Query(Criteria.where("id").is(recordPayment.getId())), RecordPayment.class);
        repository.delete(recordPayment1);
        return save(recordPayment);
    }

    public String getRecordPaymenyByInvoiceNo(String invoiceNo) {
        try {
            RecordPayment recordPayment = mongoOperation.findOne(new Query(Criteria.where("invoiceNo").is(invoiceNo)), RecordPayment.class);
            return gson.toJson(recordPayment);
        }catch(Exception ex){
            System.out.println("Error in getLastCompanyId:"+ ex);
            return gson.toJson(ex.getMessage());
        }
    }
}
