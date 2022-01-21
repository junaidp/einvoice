package com.einvoive.helper;

import com.einvoive.constants.Constants;
import com.einvoive.constants.Locations;
import com.einvoive.model.*;
import com.einvoive.repository.CeditInvoiceRepository;
import com.einvoive.util.Utility;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Locale;
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
    public String save(CreditInvoice creditInvoice){
        ErrorCustom error = new ErrorCustom();
        String jsonError;
        try {
            repository.save(creditInvoice);
            logger.info("Invoice "+creditInvoice.getInvoiceName()+" has credited with Credit No "+ creditInvoice.getInvoiceNumber());
            logsHelper.save(new Logs("adding Invoice Credit No "+ creditInvoice.getInvoiceNumber(), "Invoice "+creditInvoice.getInvoiceName()+" has credited with Credit No "+ creditInvoice.getInvoiceNumber()));
            return "Credit Invoice saved";
        }catch (Exception ex){
            logger.info("Adding Credit Invoice "+ex.getMessage());
            error.setErrorStatus("Error");
            error.setError(ex.getMessage());
            jsonError = gson.toJson(error);
            return jsonError;
        }
    }
    //end save method

    public void addAllInvoices(String companyID, List<CreditInvoice> list){
        Query query = new Query();
        query.addCriteria(Criteria.where("companyID").is(companyID));
        List<CreditInvoice> invoices = mongoOperations.find(query, CreditInvoice.class);
//        for(CreditInvoice invoice : invoices) {
//            lineItemHelper.getLineItems(invoice.getId());
//            invoice.setLineItemList(lineItemHelper.getLineItems());
//        }
        list.addAll(invoices);
    }

    public String getAllCreditInvoicesByCompanyID(String companyID){
        List<CreditInvoice> creditInvoiceList = mongoOperations.find(new Query(Criteria.where("companyID").is(companyID)), CreditInvoice.class);
        if(!creditInvoiceList.isEmpty() && creditInvoiceList !=null)
            return gson.toJson(creditInvoiceList);
        else
            return gson.toJson("Sorry no record found");
    }

    public String deleteCreditInvoice(String id){
        CreditInvoice creditInvoice = mongoOperations.findById(id, CreditInvoice.class);
        if(creditInvoice !=null) {
            repository.delete(creditInvoice);
            logger.info(creditInvoice.getInvoiceName()+":"+creditInvoice.getInvoiceNumber()+" has deleted");
            //return gson.toJson("Deleted successfully");
            return ("Deleted successfully");
        }
        else
            //return gson.toJson("Sorry no record found");
            return ("Sorry no record found");
    }

    public String updateCreditInvoice(CreditInvoice creditInvoice){
        deleteCreditInvoice(creditInvoice.getId());
        logger.info("Update Credit Invoice "+creditInvoice.getInvoiceName());
        return save(creditInvoice);
    }

    //get All Credit Invoices w.r.t CompanyID
    public String getCreditInvoiceNoByCompanyID(String id){
        Company company = mongoOperations.findOne(new Query(Criteria.where("id").is(id)), Company.class);
        if(company != null)
            return getNextCreditNoWithCode(company.getCompanyID(), company.getCompanyName().substring(0,2).toUpperCase(Locale.ROOT));
        else
            return gson.toJson("Sorry next Credit No not found for this Company/Individual");
    }
    //end get All Credit Invoices w.r.t CompanyID

    //getting next credit or debit no, fugro(loc code) based n User
    public String getUserNextCreditNo(String id) {
        User user = mongoOperations.findOne(new Query(Criteria.where("id").is(id)), User.class);
        if(user != null)
        {
            Optional<Locations> locationEnum = Locations.getLocationsByCode(user.getLocation());
            if (locationEnum.isPresent())
                return getNextCreditNoWithCode(user.getCompanyID(), locationEnum.get().getCode());
            else
                return getNextCreditNoWithCode(user.getCompanyID(), user.getLocation().substring(0, 2).toUpperCase(Locale.ROOT));
        }
        else
            return gson.toJson("Sorry next Credit No not found for this user");
    }

       // location based creditNo Fugro
    private String getNextCreditNoWithCode(String companyID, String location) {
        List<CreditInvoice> creditInvoiceList = null;
        String format = location+INVOICE_SEPARATOR+Constants.CREDIT_NOTE+INVOICE_SEPARATOR;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("companyID").is(companyID));
            query.addCriteria(Criteria.where("invoiceNumber").regex(format));
            creditInvoiceList = mongoOperations.find(query, CreditInvoice.class);
            if (creditInvoiceList != null) {
                int num, max = 0;
                for (CreditInvoice creditInvoice : creditInvoiceList) {
                    num = Utility.getAttachedNo(creditInvoice.getInvoiceNumber(), format);
                    if (max < num)
                        max = num;
                }
                max++;
                return gson.toJson(format + max);
            }
            else
                return gson.toJson(format+"1");
        }catch(Exception ex){
            logger.info("Error in getNextCreditNo:"+ ex.getMessage());
            return gson.toJson("Error in getNextCreditNo:"+ ex.getMessage());
        }
    }

}
