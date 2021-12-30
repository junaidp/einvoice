package com.einvoive.helper;

import com.einvoive.constants.Constants;
import com.einvoive.constants.Locations;
import com.einvoive.model.*;
import com.einvoive.repository.CompanyRepository;
import com.einvoive.repository.DebitInvoiceRepository;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Component
public class DebitInvoiceHelper {
    @Autowired
    MongoOperations mongoOperations;
    @Autowired
    DebitInvoiceRepository repository;
    @Autowired
    LogsHelper logsHelper;
    private Gson gson = new Gson();
    private Logger logger = LoggerFactory.getLogger(DebitInvoiceHelper.class);
    private String INVOICE_SEPARATOR = "-";
    //save method
    private String save(DebitInvoice debitInvoice){
        ErrorCustom error = new ErrorCustom();
        String jsonError;
        try {
            repository.save(debitInvoice);
            logger.info("Invoice "+debitInvoice.getInvoice().getInvoiceName()+" is credited with Credit No "+ debitInvoice.getDebitNo());
            logsHelper.save(new Logs("adding Invoice Credit No "+ debitInvoice.getDebitNo(), "Invoice "+debitInvoice.getInvoice().getInvoiceName()+" is credited with Credit No "+ debitInvoice.getDebitNo()));
            return gson.toJson(debitInvoice);
        }catch (Exception ex){
            error.setErrorStatus("Error");
            error.setError(ex.getMessage());
            jsonError = gson.toJson(error);
            return jsonError;
        }
    }
    //end save method
    //get All Credit Debit w.r.t CompanyID
    public String getDebitInvoicesByCompanyID(String companyID){
        ErrorCustom error = new ErrorCustom();
        String jsonError;
        List<DebitInvoice> debitInvoiceList = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("invoice.companyID").is(companyID));
            debitInvoiceList = mongoOperations.find(query, DebitInvoice.class);
            logger.info("Getting Debit Invoices By CompanyID: "+companyID);
            return gson.toJson(debitInvoiceList);
        }catch(Exception ex){
            logger.info("Error in getDebitInvoicesByCompanyID:"+ ex);
            error.setErrorStatus("Error");
            error.setError(ex.getMessage());
            jsonError = gson.toJson(error);
            return jsonError;
        }
    }
    //end get All Debit Invoices w.r.t CompanyID
    //getting next credit or debit no, fugro(loc code) based n User
    public String getNextDebitNo(String id, String type) {
        User user = mongoOperations.findOne(new Query(Criteria.where("id").is(id)), User.class);
        Optional<Locations> locationEnum = Locations.getLocationsByCode(user.getLocation());
        if(locationEnum.isPresent() && type.equals(Constants.DEBIT_NOTE))
            return getNextDebitNoWithCode(user.getCompanyID(), locationEnum.get().getCode());
        else
            return gson.toJson("Sorry next "+type+" not found");
    }
    //next DebitNo Fugro
    private String getNextDebitNoWithCode(String companyID, String location) {
        List<Invoice> invoices = null;
        String format = location+INVOICE_SEPARATOR+Constants.DEBIT_NOTE+INVOICE_SEPARATOR;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("companyID").is(companyID));
            query.addCriteria(Criteria.where("debitNote").regex(format));
            invoices = mongoOperations.find(query, Invoice.class);
            if (!invoices.isEmpty()) {
                int num, max = 0;
                for (Invoice invoice : invoices) {
                    num = getAttachedNo(invoice.getDebitNote(), format);
                    if (max < num)
                        max = num;
                }
                max++;
                return format + max;
            }
            else
                return format+"1";
        }catch(Exception ex){
            logger.info("Error in getNextDebitNo:"+ ex.getMessage());
            return "Error in getNextDebitNo:"+ ex.getMessage();
        }
    }

    private int getAttachedNo(String invoiceNo, String format) {
        int num = 1;
        String[] inv = StringUtils.split(invoiceNo, format);
        num = Integer.parseInt(inv[inv.length - 1]);
        return num;
    }

}
