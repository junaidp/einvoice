package com.einvoive.helper;

import com.einvoive.constants.Constants;
import com.einvoive.constants.Locations;
import com.einvoive.model.*;
import com.einvoive.repository.DebitInvoiceRepository;
import com.einvoive.util.Utility;
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
import java.util.Locale;
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
    public String save(DebitInvoice debitInvoice){
        ErrorCustom error = new ErrorCustom();
        String jsonError;
        try {
            repository.save(debitInvoice);
            logger.info("Invoice "+debitInvoice.getInvoiceName()+" has debited with Credit No "+ debitInvoice.getInvoiceNumber());
            logsHelper.save(new Logs("adding Invoice Debit No "+ debitInvoice.getInvoiceNumber(), "Invoice "+debitInvoice.getInvoiceName()+" is Debited with Debit No "+ debitInvoice.getInvoiceNumber()));
            return "Debit Invoice saved";
        }catch (Exception ex){
            error.setErrorStatus("Error");
            error.setError(ex.getMessage());
            jsonError = gson.toJson(error);
            return jsonError;
        }
    }
    //end save method

    public void addAllInvoices(String companyID, List<DebitInvoice> list){
        Query query = new Query();
        query.addCriteria(Criteria.where("companyID").is(companyID));
        List<DebitInvoice> invoices = mongoOperations.find(query, DebitInvoice.class);
//        for(CreditInvoice invoice : invoices) {
//            lineItemHelper.getLineItems(invoice.getId());
//            invoice.setLineItemList(lineItemHelper.getLineItems());
//        }
        list.addAll(invoices);
    }
    //get All Debit w.r.t CompanyID
    public String getDebitInvoiceNoByCompanyID(String id){
        Company company = mongoOperations.findOne(new Query(Criteria.where("id").is(id)), Company.class);
        if(company != null)
            return getNextDebitNoWithCode(company.getCompanyID(), company.getCompanyName().substring(0,2).toUpperCase(Locale.ROOT));
        else
            return gson.toJson("Sorry next Credit No not found for this Company/Individual");
    }

    public String getAllDebitInvoicesByCompanyID(String companyID){
        List<DebitInvoice> debitInvoiceList = mongoOperations.find(new Query(Criteria.where("companyID").is(companyID)), DebitInvoice.class);
        if(!debitInvoiceList.isEmpty() && debitInvoiceList !=null)
            return gson.toJson(debitInvoiceList);
        else
            return gson.toJson("Sorry no record found");
    }

    public String deleteDebitInvoice(String id){
        DebitInvoice debitInvoice = mongoOperations.findById(id, DebitInvoice.class);
        if(debitInvoice !=null) {
            repository.delete(debitInvoice);
            logger.info(debitInvoice.getInvoiceName()+":"+debitInvoice.getInvoiceNumber()+" has deleted");
            return gson.toJson("Deleted successfully");
        }
        else
            return gson.toJson("Sorry no record found");
    }

    public String updateDebitInvoice(DebitInvoice debitInvoice){
        deleteDebitInvoice(debitInvoice.getId());
        return save(debitInvoice);
    }

    //end get All Debit Invoices w.r.t CompanyID
    //getting next credit or debit no, fugro(loc code) based n User
    public String getNextDebitNo(String id) {
        User user = mongoOperations.findOne(new Query(Criteria.where("id").is(id)), User.class);
        if(user != null)
        {
            Optional<Locations> locationEnum = Locations.getLocationsByCode(user.getLocation());
            if (locationEnum.isPresent())
                return getNextDebitNoWithCode(user.getCompanyID(), locationEnum.get().getCode());
            else
                return getNextDebitNoWithCode(user.getCompanyID(), user.getLocation().substring(0, 2).toUpperCase(Locale.ROOT));
        }
        else
            return gson.toJson("Sorry next Debit No not found for this user");
    }
    //next DebitNo Fugro
    private String getNextDebitNoWithCode(String companyID, String location) {
        List<DebitInvoice> debitInvoiceList = null;
        String format = location+INVOICE_SEPARATOR+Constants.DEBIT_NOTE+INVOICE_SEPARATOR;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("companyID").is(companyID));
            query.addCriteria(Criteria.where("invoiceNumber").regex(format));
            debitInvoiceList = mongoOperations.find(query, DebitInvoice.class);
            if (!debitInvoiceList.isEmpty()) {
                int num, max = 0;
                for (DebitInvoice debitInvoice : debitInvoiceList) {
                    num = Utility.getAttachedNo(debitInvoice.getInvoiceNumber(), format);
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

//    private int getAttachedNo(String invoiceNo, String format) {
//        int num = 1;
//        String[] inv = StringUtils.split(invoiceNo, format);
//        num = Integer.parseInt(inv[inv.length - 1]);
//        return num;
//    }

}
