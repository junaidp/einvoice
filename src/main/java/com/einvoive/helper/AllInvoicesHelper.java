package com.einvoive.helper;

import com.einvoive.constants.Constants;
import com.einvoive.model.*;
import com.einvoive.repository.CeditInvoiceRepository;
import com.einvoive.repository.DebitInvoiceRepository;
import com.einvoive.repository.InvoiceRepository;
import com.einvoive.util.EmailSender;
import com.einvoive.util.Utility;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class AllInvoicesHelper {
    @Autowired
    InvoiceRepository invoiceRepository;
    @Autowired
    InvoiceHelper invoiceHelper;
    @Autowired
    EmailSender emailSender;
    @Autowired
    LogsHelper logsHelper;
    @Autowired
    CeditInvoiceRepository ceditInvoiceRepository;
    @Autowired
    MongoOperations mongoOperation;
    @Autowired
    CreditInvoiceHelper creditInvoiceHelper;
    @Autowired
    DebitInvoiceRepository debitInvoiceRepository;
    @Autowired
    DebitInvoiceHelper debitInvoiceHelper;
    private Logger logger = LoggerFactory.getLogger(AllInvoicesHelper.class);
    private Gson gson = new Gson();


    //get All Credit Debit Invoices by ID

    public String getAllTypesInvoiceByID(String id) {
        Object invoice = null;
        try {
            invoice = mongoOperation.findById(id, Invoice.class);
            if(invoice == null)
                invoice = mongoOperation.findById(id, CreditInvoice.class);
            if(invoice == null)
                invoice = mongoOperation.findById(id, DebitInvoice.class);
            else
                gson.toJson("No Invoice found");
        }catch(Exception ex){
            System.out.println("Error in get invoices By ID :"+ ex);
        }
        return gson.toJson(invoice);
    }
    //get All Credit Debit Invoices by cutomerName

    public String getAllTypesInvoiceByCustomer(String cutomerName, String location, String companyID) {
        List<Object> list = new ArrayList<>();
        List<?> invoiceList = new ArrayList<>();
        List<?> creditInvoiceList = new ArrayList<>();
        List<?> debitInvoices = new ArrayList<>();
        try {
            invoiceList = mongoOperation.find(new Query(Criteria.where("customerName").is(cutomerName).and("location").is(location).and("companyID").is(companyID)), Invoice.class);
            creditInvoiceList = mongoOperation.find(new Query(Criteria.where("customerName").is(cutomerName).and("location").is(location).and("companyID").is(companyID)), CreditInvoice.class);
            debitInvoices = mongoOperation.find(new Query(Criteria.where("customerName").is(cutomerName).and("location").is(location).and("companyID").is(companyID)), DebitInvoice.class);
            if(invoiceList != null && !invoiceList.isEmpty())
                addInvoicesinList(list, invoiceList);
            if(creditInvoiceList != null && !creditInvoiceList.isEmpty())
                addInvoicesinList(list, creditInvoiceList);
            if(debitInvoices != null && !debitInvoices.isEmpty())
                addInvoicesinList(list, debitInvoices);
            if (list != null && !list.isEmpty())
                return gson.toJson(list);
            else
                return gson.toJson("No Invoice found Customer :"+cutomerName);
        }catch(Exception ex){
            logger.info("Error in get invoices By Customer :"+cutomerName+"Errors is: "+ ex.getMessage());
            return "Error in get invoices By Customer :"+cutomerName+"Errors is: "+ ex.getMessage();
        }
//        return gson.toJson(list);
    }

    //for get invoice by customer for company
    public String getAllTypesInvoiceByCustomerforCompany(String cutomerName, String companyID) {
        List<Object> list = new ArrayList<>();
        List<?> invoiceList = new ArrayList<>();
        List<?> creditInvoiceList = new ArrayList<>();
        List<?> debitInvoices = new ArrayList<>();
        try {
            invoiceList = mongoOperation.find(new Query(Criteria.where("customerName").is(cutomerName).and("companyID").is(companyID)), Invoice.class);
            creditInvoiceList = mongoOperation.find(new Query(Criteria.where("customerName").is(cutomerName).and("companyID").is(companyID)), CreditInvoice.class);
            debitInvoices = mongoOperation.find(new Query(Criteria.where("customerName").is(cutomerName).and("companyID").is(companyID)), DebitInvoice.class);
            if(invoiceList != null && !invoiceList.isEmpty())
                addInvoicesinList(list, invoiceList);
            if(creditInvoiceList != null && !creditInvoiceList.isEmpty())
                addInvoicesinList(list, creditInvoiceList);
            if(debitInvoices != null && !debitInvoices.isEmpty())
                addInvoicesinList(list, debitInvoices);
            if (list != null && !list.isEmpty())
                return gson.toJson(list);
            else
                return gson.toJson("No Invoice found Customer :"+cutomerName);
        }catch(Exception ex){
            logger.info("Error in get invoices By Customer :"+cutomerName+"Errors is: "+ ex.getMessage());
            return "Error in get invoices By Customer :"+cutomerName+"Errors is: "+ ex.getMessage();
        }
//        return gson.toJson(list);
    }
    private void addInvoicesinList(List<Object> list, List<?> invoiceList) {
        for(Object obj:invoiceList)
            list.add(obj);
    }

    //get All Credit Debit Invoices by invoiceNumber

    public String getAllTypesInvoiceByInvoiceNumber(String invoiceNumber, String location, String companyID) {
        List<Object> list = new ArrayList<>();
        List<?> invoiceList = new ArrayList<>();
        List<?> creditInvoiceList = new ArrayList<>();
        List<?> debitInvoices = new ArrayList<>();
        try {
            invoiceList = mongoOperation.find(new Query(Criteria.where("invoiceNumber").is(invoiceNumber).and("location").is(location).and("companyID").is(companyID)), Invoice.class);
            creditInvoiceList = mongoOperation.find(new Query(Criteria.where("invoiceNumber").is(invoiceNumber).and("location").is(location).and("companyID").is(companyID)), CreditInvoice.class);
            debitInvoices = mongoOperation.find(new Query(Criteria.where("invoiceNumber").is(invoiceNumber).and("location").is(location).and("companyID").is(companyID)), DebitInvoice.class);
            if(invoiceList != null && !invoiceList.isEmpty())
                addInvoicesinList(list, invoiceList);
            if(creditInvoiceList != null && !creditInvoiceList.isEmpty())
                addInvoicesinList(list, creditInvoiceList);
            if(debitInvoices != null && !debitInvoices.isEmpty())
                addInvoicesinList(list, debitInvoices);
            if (list != null && !list.isEmpty())
                return gson.toJson(list);
            else
                return gson.toJson("No Invoice found"+invoiceNumber);
        }catch(Exception ex){
            logger.info("Error in get invoices By InvoiceNumber :"+invoiceNumber+"Errors is: "+ ex.getMessage());
            return "No Invoice found"+invoiceNumber;
        }
//        return gson.toJson(list);
    }
    //get All Credit Debit Invoices by Duration

    //for invoicenumber by company
    public String getAllTypesInvoiceByInvoiceNumberforCompany(String invoiceNumber, String companyID) {
        List<Object> list = new ArrayList<>();
        List<?> invoiceList = new ArrayList<>();
        List<?> creditInvoiceList = new ArrayList<>();
        List<?> debitInvoices = new ArrayList<>();
        try {
            invoiceList = mongoOperation.find(new Query(Criteria.where("invoiceNumber").is(invoiceNumber).and("companyID").is(companyID)), Invoice.class);
            creditInvoiceList = mongoOperation.find(new Query(Criteria.where("invoiceNumber").is(invoiceNumber).and("companyID").is(companyID)), CreditInvoice.class);
            debitInvoices = mongoOperation.find(new Query(Criteria.where("invoiceNumber").is(invoiceNumber).and("companyID").is(companyID)), DebitInvoice.class);
            if(invoiceList != null && !invoiceList.isEmpty())
                addInvoicesinList(list, invoiceList);
            if(creditInvoiceList != null && !invoiceList.isEmpty())
                addInvoicesinList(list, creditInvoiceList);
            if(debitInvoices != null && !debitInvoices.isEmpty())
                addInvoicesinList(list, debitInvoices);
            if (list != null && !list.isEmpty())
                return gson.toJson(list);
            else
                return gson.toJson("No Invoice found"+invoiceNumber);
        }catch(Exception ex){
            logger.info("Error in get invoices By InvoiceNumber :"+invoiceNumber+"Errors is: "+ ex.getMessage());
            return "No Invoice found"+invoiceNumber;
        }
//        return gson.toJson(list);
    }
    //
    public String getAllTypesInvoiceByDuration(String startDate, String endDate, String companyID, String location) {
        List<Object> list = new ArrayList<>();
        List<?> invoiceList = new ArrayList<>();
        List<?> creditInvoiceList = new ArrayList<>();
        List<?> debitInvoices = new ArrayList<>();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date startDateFinal = null;
        Date endDateFinal = null;
        try {
            startDateFinal = df.parse(startDate);
            endDateFinal = df.parse(endDate);
        } catch (
                ParseException e) {
            e.printStackTrace();
        }
        try {
            invoiceList = mongoOperation.find(new Query(Criteria.where("companyID").is(companyID)
                    .and("invoiceDate").gte(startDateFinal).lte(endDateFinal).and("location").is(location)), Invoice.class);
            creditInvoiceList = mongoOperation.find(new Query(Criteria.where("companyID").is(companyID)
                        .and("invoiceDate").gte(startDateFinal).lte(endDateFinal).and("location").is(location)), CreditInvoice.class);
            debitInvoices = mongoOperation.find(new Query(Criteria.where("companyID").is(companyID)
                        .and("invoiceDate").gte(startDateFinal).lte(endDateFinal).and("location").is(location)), DebitInvoice.class);
            if(invoiceList != null && !invoiceList.isEmpty())
                addInvoicesinList(list, invoiceList);
            if(creditInvoiceList != null && !creditInvoiceList.isEmpty())
                addInvoicesinList(list, creditInvoiceList);
            if(debitInvoices != null && !debitInvoices.isEmpty())
                addInvoicesinList(list, debitInvoices);
            if (list != null && !list.isEmpty())
                return gson.toJson(list);
            else
                return gson.toJson("No Invoice found"+startDateFinal.toString()+":"+endDateFinal);
        }catch(Exception ex){
            logger.info("Error in get invoices By Duration :"+startDateFinal.toString()+":"+endDateFinal+"Errors is: "+ex.getMessage());
            return "Error in get invoices By Duration :"+startDateFinal.toString()+":"+endDateFinal;
        }
//        return gson.toJson(list);
    }

    public String getAllTypesInvoiceByDurationforCompany(String startDate, String endDate, String companyID) {
        List<Object> list = new ArrayList<>();
        List<?> invoiceList = new ArrayList<>();
        List<?> creditInvoiceList = new ArrayList<>();
        List<?> debitInvoices = new ArrayList<>();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date startDateFinal = null;
        Date endDateFinal = null;
        try {
            startDateFinal = df.parse(startDate);
            endDateFinal = df.parse(endDate);
        } catch (
                ParseException e) {
            e.printStackTrace();
        }
        try {
            invoiceList = mongoOperation.find(new Query(Criteria.where("companyID").is(companyID)
                    .and("invoiceDate").gte(startDateFinal).lte(endDateFinal)), Invoice.class);
            creditInvoiceList = mongoOperation.find(new Query(Criteria.where("companyID").is(companyID)
                    .and("invoiceDate").gte(startDateFinal).lte(endDateFinal)), CreditInvoice.class);
            debitInvoices = mongoOperation.find(new Query(Criteria.where("companyID").is(companyID)
                    .and("invoiceDate").gte(startDateFinal).lte(endDateFinal)), DebitInvoice.class);
            if(invoiceList != null && !invoiceList.isEmpty())
                addInvoicesinList(list, invoiceList);
            if(creditInvoiceList != null && !creditInvoiceList.isEmpty())
                addInvoicesinList(list, creditInvoiceList);
            if(debitInvoices != null && !debitInvoices.isEmpty())
                addInvoicesinList(list, debitInvoices);
            if (list != null && !list.isEmpty())
                return gson.toJson(list);
            else
                return gson.toJson("No Invoice found"+startDateFinal.toString()+":"+endDateFinal);
        }catch(Exception ex){
            logger.info("Error in get invoices By Duration :"+startDateFinal.toString()+":"+endDateFinal+"Errors is: "+ex.getMessage());
            return "Error in get invoices By Duration :"+startDateFinal.toString()+":"+endDateFinal;
        }
//        return gson.toJson(list);
    }

    public String setInvoiceStatus(String id, String status) {
        ErrorCustom error = new ErrorCustom();
        String jsonError;
        try {
            Invoice invoice = mongoOperation.findOne(new Query(Criteria.where("id").is(id)), Invoice.class);
            CreditInvoice creditInvoice = mongoOperation.findOne(new Query(Criteria.where("id").is(id)), CreditInvoice.class);
            DebitInvoice debitInvoice = mongoOperation.findOne(new Query(Criteria.where("id").is(id)), DebitInvoice.class);
            if(invoice != null){
                return settingInvoiceStatus(status, invoice);
            }
            if(creditInvoice != null){
                return settingCreditInvoiceStatus(status, creditInvoice);
            }
            if(debitInvoice != null){
                return settingDebitInvoiceStatus(status, debitInvoice);
            }
            else{
                error.setErrorStatus("Error");
                error.setError("No Invoice against this ID");
                jsonError = gson.toJson(error);
                return jsonError;
            }
        }
        catch (Exception ex){
            error.setErrorStatus("Error");
            error.setError(ex.getMessage());
            jsonError = gson.toJson(error);
            return jsonError;
        }
    }

    public String getAllInvoicesByLocation(String companyID, String location){
        List<Object> list = new ArrayList<>();
        List<?> invoiceList = new ArrayList<>();
        List<?> creditInvoiceList = new ArrayList<>();
        List<?> debitInvoices = new ArrayList<>();
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("companyID").is(companyID).and("location").is(location));
            invoiceList = mongoOperation.find(query, Invoice.class);
            creditInvoiceList = mongoOperation.find(new Query(Criteria.where("companyID").is(companyID).and("location").is(location)), CreditInvoice.class);
            debitInvoices = mongoOperation.find(new Query(Criteria.where("companyID").is(companyID).and("location").is(location)), DebitInvoice.class);
            if(invoiceList != null && !invoiceList.isEmpty())
                addInvoicesinList(list, invoiceList);
            if(creditInvoiceList != null && !creditInvoiceList.isEmpty())
                addInvoicesinList(list, creditInvoiceList);
            if(debitInvoices != null && !debitInvoices.isEmpty())
                addInvoicesinList(list, debitInvoices);
            if (list != null && !list.isEmpty())
                 return gson.toJson(list);
            else
                return gson.toJson("No Invoice found");
//            for(Invoice invoice : invoices) {
//                lineItemHelper.getLineItems(invoice.getId());
//                invoice.setLineItemList(lineItemHelper.getLineItems());
//            }
        }catch(Exception ex){
            logger.info("Error in get invoices:"+ ex.getMessage());
            return "Error in get invoices:"+ ex.getMessage();
        }
    }

    private String settingInvoiceStatus(String status, Invoice invoice) {
        invoice.setStatus(status);
        if(status.equals(Constants.STATUS_APPROVED)) {
            User user = mongoOperation.findOne(new Query(Criteria.where("id").is(invoice.getUserId())), User.class);
            if(user != null)
                emailSender.sendEmail(user.getEmail(), "Invoice Approved", "Your Invoice has benn approved. Please have a look on Invoice: "+ invoice.getInvoiceNumber());
        }
        if(status.equals(Constants.STATUS_FORAPPROVAL)){
            User user = mongoOperation.findOne(new Query(Criteria.where("location").is(invoice.getLocation())), User.class);
            if(user != null)
                emailSender.sendEmail(user.getEmail(), "Invoice Approval", "Please Approve this Invoice: "+ invoice.getInvoiceNumber());
        }
        logsHelper.save(new Logs("Changing Invoice Status for "+ invoice.getInvoiceName(), "User "+ Utility.getUserName(invoice.getUserId(), mongoOperation)+" has changed invoice "+ invoice.getInvoiceName()+" status to "+ status));
        invoiceRepository.save(invoice);
//                saveLogs(invoice);
        return "Invoice Status Updated";
    }

    //Credit InvoiceStatus
    private String settingCreditInvoiceStatus(String status, CreditInvoice invoice) {
        invoice.setStatus(status);
        if(status.equals(Constants.STATUS_APPROVED)) {
            User user = mongoOperation.findOne(new Query(Criteria.where("id").is(invoice.getUserId())), User.class);
            if(user != null)
                emailSender.sendEmail(user.getEmail(), "Invoice Approved", "Your Invoice has benn approved. Please have a look on Invoice: "+ invoice.getInvoiceNumber());
        }
        if(status.equals(Constants.STATUS_FORAPPROVAL)){
            User user = mongoOperation.findOne(new Query(Criteria.where("location").is(invoice.getLocation())), User.class);
            if(user != null)
                emailSender.sendEmail(user.getEmail(), "Invoice Approval", "Please Approve this Invoice: "+ invoice.getInvoiceNumber());
        }
        logsHelper.save(new Logs("Changing Credit Invoice Status for "+ invoice.getInvoiceName(), "User "+ Utility.getUserName(invoice.getUserId(), mongoOperation)+" has changed invoice "+ invoice.getInvoiceName()+" status to "+ status));
        ceditInvoiceRepository.save(invoice);
//                saveLogs(invoice);
        return "Credit Invoice Status Updated";
    }

    //Debit Invoice Status
    private String settingDebitInvoiceStatus(String status, DebitInvoice invoice) {
        invoice.setStatus(status);
        if(status.equals(Constants.STATUS_APPROVED)) {
            User user = mongoOperation.findOne(new Query(Criteria.where("id").is(invoice.getUserId())), User.class);
            if(user != null)
                emailSender.sendEmail(user.getEmail(), "Invoice Approved", "Your Invoice has benn approved. Please have a look on Invoice: "+ invoice.getInvoiceNumber());
        }
        if(status.equals(Constants.STATUS_FORAPPROVAL)){
            User user = mongoOperation.findOne(new Query(Criteria.where("location").is(invoice.getLocation())), User.class);
            if(user != null)
                emailSender.sendEmail(user.getEmail(), "Invoice Approval", "Please Approve this Invoice: "+ invoice.getInvoiceNumber());
        }
        logsHelper.save(new Logs("Changing Debit Invoice Status for "+ invoice.getInvoiceName(), "User "+ Utility.getUserName(invoice.getUserId(), mongoOperation)+" has changed invoice "+ invoice.getInvoiceName()+" status to "+ status));
        debitInvoiceRepository.save(invoice);
//                saveLogs(invoice);
        return "Debit Invoice Status Updated";
    }


}
