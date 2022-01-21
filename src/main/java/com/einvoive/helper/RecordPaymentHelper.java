package com.einvoive.helper;

import com.einvoive.model.*;
import com.einvoive.repository.RecordPaymentRepository;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class RecordPaymentHelper {
    private Logger logger = LoggerFactory.getLogger(RecordPaymentHelper.class);
    @Autowired
    RecordPaymentRepository repository;
    @Autowired
    InvoiceHelper invoiceHelper;
    @Autowired
    MongoOperations mongoOperation;
    @Autowired
    private LogsHelper logsHelper;
    @Autowired
    UserHelper userHelper;
    Gson gson = new Gson();

    public String save(RecordPayment recordPayment){
        ErrorCustom error = new ErrorCustom();
        String jsonError;
            try {
                repository.save(recordPayment);
                updateInvoiceRecordPayment(recordPayment);
                logsHelper.save(new Logs("Adding Payment Record for InvoiceNo"+recordPayment.getInvoiceNo(),  " Total amount "+ recordPayment.getTotalAmount()+" Paid amount "+ recordPayment.getPaidAmmount()+" Paid Account "+ recordPayment.getPayAccount()+ " Paid date "+ recordPayment.getPaymentDate()+ " Paid nots "+ recordPayment.getNotes()));
                return "RecordPayment saved";
            }catch(Exception ex){
                logger.info("Exception in Record Payment save "+ex.getMessage());
                error.setErrorStatus("Error");
                error.setError(ex.getMessage());
                jsonError = gson.toJson(error);
                return jsonError;
            }
     }

     private void updateInvoiceRecordPayment(RecordPayment recordPayment){
        Invoice invoice = mongoOperation.findOne(new Query(Criteria.where("invoiceNumber").is(recordPayment.getInvoiceNo())), Invoice.class);
         invoice.setRecordPayment(String.valueOf(Double.parseDouble(invoice.getRecordPayment()) - Double.parseDouble(recordPayment.getPaidAmmount())));
        invoiceHelper.save(invoice);
    }

    public String update(RecordPayment recordPayment) {
        RecordPayment recordPayment1 = mongoOperation.findOne(new Query(Criteria.where("id").is(recordPayment.getId())), RecordPayment.class);
        repository.delete(recordPayment1);
        logger.info("Update Record Payment ");
        return save(recordPayment);
    }

    public String getRecordPaymenyByInvoiceNo(String invoiceNo) {
        try {
            RecordPayment recordPayment = mongoOperation.findOne(new Query(Criteria.where("invoiceNo").is(invoiceNo)), RecordPayment.class);
            return gson.toJson(recordPayment);
        }catch(Exception ex){
            logger.info("Error in getLastCompanyId:"+ ex.getMessage());
            return gson.toJson(ex.getMessage());
        }
    }
}
