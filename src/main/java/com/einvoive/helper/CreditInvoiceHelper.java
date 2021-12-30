package com.einvoive.helper;

import com.einvoive.constants.Constants;
import com.einvoive.constants.Locations;
import com.einvoive.model.*;
import com.einvoive.repository.CeditInvoiceRepository;
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
public class CreditInvoiceHelper {

    @Autowired
    MongoOperations mongoOperations;
    @Autowired
    CeditInvoiceRepository repository;
    @Autowired
    LogsHelper logsHelper;
    private Gson gson = new Gson();
    private Logger logger = LoggerFactory.getLogger(CreditInvoiceHelper.class);
    private String INVOICE_SEPARATOR = "-";
    //save method
    private String save(CreditInvoice creditInvoice){
        ErrorCustom error = new ErrorCustom();
        String jsonError;
        try {
            repository.save(creditInvoice);
            logger.info("Invoice "+creditInvoice.getInvoice().getInvoiceName()+" is credited with Credit No "+ creditInvoice.getCreditNo());
            logsHelper.save(new Logs("adding Invoice Credit No "+ creditInvoice.getCreditNo(), "Invoice "+creditInvoice.getInvoice().getInvoiceName()+" is credited with Credit No "+ creditInvoice.getCreditNo()));
            return gson.toJson(creditInvoice);
        }catch (Exception ex){
            error.setErrorStatus("Error");
            error.setError(ex.getMessage());
            jsonError = gson.toJson(error);
            return jsonError;
        }
    }
    //end save method

    //get All Credit Invoices w.r.t CompanyID
    public String getCreditInvoicesByCompanyID(String companyID){
        ErrorCustom error = new ErrorCustom();
        String jsonError;
        List<CreditInvoice> creditInvoiceList = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("invoice.companyID").is(companyID));
            creditInvoiceList = mongoOperations.find(query, CreditInvoice.class);
            logger.info("Getting Credit Invoices By CompanyID: "+companyID);
            return gson.toJson(creditInvoiceList);
        }catch(Exception ex){
            logger.info("Error in getCreditInvoicesByCompanyID:"+ ex);
            error.setErrorStatus("Error");
            error.setError(ex.getMessage());
            jsonError = gson.toJson(error);
            return jsonError;
        }
    }
    //end get All Credit Invoices w.r.t CompanyID

    //getting next credit or debit no, fugro(loc code) based n User
    public String getNextCreditNo(String id, String type) {
        User user = mongoOperations.findOne(new Query(Criteria.where("id").is(id)), User.class);
        Optional<Locations> locationEnum = Locations.getLocationsByCode(user.getLocation());
        if(locationEnum.isPresent() && type.equals(Constants.CREDIT_NOTE))
            return getNextCreditNoWithCode(user.getCompanyID(), locationEnum.get().getCode());
        else
            return gson.toJson("Sorry next "+type+" not found");
    }

       // location based creditNo Fugro
    private String getNextCreditNoWithCode(String companyID, String location) {
        List<Invoice> invoices = null;
        String format = location+INVOICE_SEPARATOR+Constants.CREDIT_NOTE+INVOICE_SEPARATOR;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("companyID").is(companyID));
            query.addCriteria(Criteria.where("creditNote").regex(format));
            invoices = mongoOperations.find(query, Invoice.class);
            if (!invoices.isEmpty()) {
                int num, max = 0;
                for (Invoice invoice : invoices) {
                    num = getAttachedNo(invoice.getCreditNote(), format);
                    if (max < num)
                        max = num;
                }
                max++;
                return format + max;
            }
            else
                return format+"1";
        }catch(Exception ex){
            logger.info("Error in getNextCreditNo:"+ ex.getMessage());
            return "Error in getNextCreditNo:"+ ex.getMessage();
        }
    }

    private int getAttachedNo(String invoiceNo, String format) {
        int num = 1;
        String[] inv = StringUtils.split(invoiceNo, format);
        num = Integer.parseInt(inv[inv.length - 1]);
        return num;
    }


}
