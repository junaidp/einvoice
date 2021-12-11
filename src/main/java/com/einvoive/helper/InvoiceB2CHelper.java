package com.einvoive.helper;

import com.einvoive.model.*;
import com.einvoive.repository.InvoiceB2CRepository;
import com.einvoive.util.Utility;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.NoSuchAlgorithmException;
import java.util.*;

@Component
public class InvoiceB2CHelper {

    @Autowired
    InvoiceB2CRepository repository;

    @Autowired
    LineItemB2CHelper lineItemHelper;

    @Autowired
    MongoOperations mongoOperation;

    Gson gson = new Gson();

    private String INVOICE_SEPARATOR = "-";

    private  List<InvoiceB2C>invoiceListMain = null;

    public String save(InvoiceB2C invoice){
        ErrorCustom error = new ErrorCustom();
        String jsonError;
        try {
            setInvoice(invoice);
            repository.save(invoice);
            for(LineItemB2C lineItem : invoice.getLineItemList()){
                lineItem.setInvoiceId(invoice.getId());
                lineItemHelper.save(lineItem);
            }
            return "Invoice saved";
        }catch(Exception ex){
            error.setErrorStatus("Error");
            error.setError(ex.getMessage());
            jsonError = gson.toJson(error);
            return jsonError;
        }
    }

    private void setInvoice(InvoiceB2C invoice) throws NoSuchAlgorithmException {
        invoice.setSerialNo(getAvaiablaeId(invoice.getCompanyID()));
//      UUID uuid = UUID.fromString("00809e66-36d5-436f-93c4-e4e2c76cce0d");
        invoice.setId(invoice.setId(String.valueOf(UUID.randomUUID())));
        invoice.setHash(Utility.encrypt(invoice.getId()));
        invoice.setPreviousHash(getPreviousHash(invoice));
//        invoice.setInvoiceNumber(getNextInvoiceNumber(invoice.getCompanyID()));
    }

    public String getNextInvoiceNumber(String companyID) {

        String lastCompanyInvoiceNumber = getLastInvoiceByCompany(companyID);
        String invoiceNumber = "";
        if (lastCompanyInvoiceNumber.isEmpty()) {
            invoiceNumber = companyID.substring(0,2) + INVOICE_SEPARATOR + "1";
        }
        else{
            String[] inv = StringUtils.split(lastCompanyInvoiceNumber, INVOICE_SEPARATOR);
            int invoiceNum = 0 ;
            if(inv!=null && !(inv.length <=0)) {
                invoiceNum = Integer.parseInt(inv[1]) + 1;
            }
            else{
                invoiceNum = Integer.parseInt(lastCompanyInvoiceNumber) + 1;
            }
            invoiceNumber = companyID.substring(0,2) + INVOICE_SEPARATOR + invoiceNum;
        }
        return invoiceNumber;
    }

    private String getPreviousHash(InvoiceB2C invoice) {
        List<Invoice> invoiceList = mongoOperation.findAll(Invoice.class);
        String previousHash = "";
        if(invoiceList.size() > 0)
            previousHash = invoiceList.get(invoiceList.size()-1).getHash();
        else
            previousHash = "";

        return previousHash;
    }

    public String getInvoicesByCustomer(String customerName){
        List<InvoiceB2C> invoices = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("customerName").is(customerName));
            invoices = mongoOperation.find(query, InvoiceB2C.class);
            for(InvoiceB2C invoice : invoices) {
                lineItemHelper.getLineItemsB2C(invoice.getId());
                invoice.setLineItemList(lineItemHelper.getLineItemsB2C());
            }
        }catch(Exception ex){
            System.out.println("Error in get invoices:"+ ex);
        }
        return gson.toJson(invoices);
    }

    public String getInvoicesByInvoiceNo(String invoiceNo){
        List<InvoiceB2C> invoices = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("invoiceNumber").is(invoiceNo));
            invoices = mongoOperation.find(query, InvoiceB2C.class);
            for(InvoiceB2C invoice : invoices) {
                lineItemHelper.getLineItemsB2C(invoice.getId());
                invoice.setLineItemList(lineItemHelper.getLineItemsB2C());
            }
        }catch(Exception ex){
            System.out.println("Error in get invoices:"+ ex);
        }
        return gson.toJson(invoices);
    }

    public String getInvoicesByStatus(String status){
        List<InvoiceB2C> invoices = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("status").is(status));
            invoices = mongoOperation.find(query, InvoiceB2C.class);
            for(InvoiceB2C invoice : invoices) {
                lineItemHelper.getLineItemsB2C(invoice.getId());
                invoice.setLineItemList(lineItemHelper.getLineItemsB2C());
            }
        }catch(Exception ex){
            System.out.println("Error in get invoices:"+ ex);
        }
        return gson.toJson(invoices);
    }

    public String getInvoicesByCompany(String companyID){
        List<InvoiceB2C> invoices = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("companyID").is(companyID));
            invoices = mongoOperation.find(query, InvoiceB2C.class);
            for(InvoiceB2C invoice : invoices) {
                lineItemHelper.getLineItemsB2C(invoice.getId());
                invoice.setLineItemList(lineItemHelper.getLineItemsB2C());
            }
        }catch(Exception ex){
            System.out.println("Error in get invoices:"+ ex);
        }
        return gson.toJson(invoices);
    }

    public String getLastInvoiceByCompany(String companyID) {
        List<Invoice> invoices = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("companyID").is(companyID));
            query.with(Sort.by(Sort.Direction.DESC, "invoiceNumber")).limit(1);
            // invoiceRepository.findAll(Sort.by(Sort.Direction.DESC, "invoiceNumber"));
            invoices = mongoOperation.find(query, Invoice.class);
            return invoices.get(0).getInvoiceNumber();
        }catch(Exception ex){
            System.out.println("Error in getLastInvoiceByCompany:"+ ex);
            return "";
        }

    }

    public String deleteInvoice(String invoiceID){
        List<InvoiceB2C> invoices = mongoOperation.find(new Query(Criteria.where("id").is(invoiceID)), InvoiceB2C.class);
        repository.deleteAll(invoices);
        return "Invoice deleted";
    }

    public String getAvaiablaeId(String companyID) {
        List<Invoice> invoiceList = mongoOperation.find(new Query(Criteria.where("companyID").is(companyID)), Invoice.class);
        return String.valueOf(invoiceList.size()+1);
    }

    public String update(InvoiceB2C invoice) {
        deleteInvoice(invoice.getId());
        return save(invoice);
    }

    public String getInvoiceStatus(String id) {
        Invoice invoice = mongoOperation.findOne(new Query(Criteria.where("id").is(id)), Invoice.class);
        return invoice.getStatus();
    }

    public String setInvoiceStatus(String id, String status) {
        ErrorCustom error = new ErrorCustom();
        String jsonError;
        try {
            InvoiceB2C invoice = mongoOperation.findOne(new Query(Criteria.where("id").is(id)), InvoiceB2C.class);
            if(invoice != null){
//                invoice.setStatus(status);
                repository.save(invoice);
                return "Invoice Status Updated";
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

    public String getInvoicesByID(String id) {
        List<InvoiceB2C> invoicesList = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("id").is(id));
            invoicesList = mongoOperation.find(query, InvoiceB2C.class);
        }catch(Exception ex){
            System.out.println("Error in get invoices By ID :"+ ex);
        }
        return gson.toJson(invoicesList);
    }

    public String setInvoiceStatusByInvoiceID(String invoiceID, String status) {
        ErrorCustom error = new ErrorCustom();
        String jsonError;
        try {
            InvoiceB2C invoice = mongoOperation.findOne(new Query(Criteria.where("invoiceNumber").is(invoiceID)), InvoiceB2C.class);
            if(invoice != null){
//                invoice.setStatus(status);
                repository.save(invoice);
                return "Invoice Status Updated";
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


    //    Company having multiple user w.r.t their locations
    public String getNextInvoiceNoByUserID(String id) {
        User user = mongoOperation.findOne(new Query(Criteria.where("id").is(id)), User.class);
        String lastCompanyInvoiceNumber = getLastInvoiceLocation(user.getCompanyID(), user.getLocation());
        String invoiceNumber = "";
        if (lastCompanyInvoiceNumber.isEmpty()) {
            invoiceNumber = user.getCompanyID().substring(0,2) + INVOICE_SEPARATOR + user.getLocation().substring(0,2) + INVOICE_SEPARATOR + "1";
        }
        else{
            String[] inv = StringUtils.split(lastCompanyInvoiceNumber, INVOICE_SEPARATOR);
            int invoiceNum = 0 ;
            inv = StringUtils.split(String.valueOf(inv[1]), INVOICE_SEPARATOR);
            if(inv!=null && !(inv.length <=0)) {
                invoiceNum = Integer.parseInt(inv[1]) + 1;
            }
            else{
                invoiceNum = Integer.parseInt(lastCompanyInvoiceNumber) + 1;
            }
            invoiceNumber = user.getCompanyID().substring(0,2) + INVOICE_SEPARATOR + user.getLocation().substring(0,2) + INVOICE_SEPARATOR + invoiceNum;
        }
        return invoiceNumber;
    }

    //    When single user Company request
    public String getNextInvoiceNoIndividual(String id){
        Company company = mongoOperation.findOne(new Query(Criteria.where("id").is(id)), Company.class);
        String lastCompanyInvoiceNumber = getLastInvoiceNo(company.getCompanyID());
        String invoiceNumber = "";
        if (lastCompanyInvoiceNumber.isEmpty()) {
            invoiceNumber = company.getCompanyID().substring(0,2) + INVOICE_SEPARATOR + "1";
        }
        else{
            String[] inv = StringUtils.split(lastCompanyInvoiceNumber, INVOICE_SEPARATOR);
            int invoiceNum = 0 ;
//            inv = StringUtils.split(inv[1], INVOICE_SEPARATOR);
            if(inv!=null && !(inv.length <=0)) {
                invoiceNum = Integer.parseInt(inv[1]) + 1;
            }
            else{
                invoiceNum = Integer.parseInt(lastCompanyInvoiceNumber) + 1;
            }
            invoiceNumber = company.getCompanyID().substring(0,2) + INVOICE_SEPARATOR + invoiceNum;
        }
        return invoiceNumber;
    }

    //for Multiple User under a company But CompanyID is generating
    public String getNextInvoiceNoByCompanyID(String id) {
        Company company = mongoOperation.findOne(new Query(Criteria.where("id").is(id)), Company.class);
        String lastCompanyInvoiceNumber = getLastInvoiceNo(company.getCompanyID());
        String invoiceNumber = "";
        if (lastCompanyInvoiceNumber.isEmpty()) {
            invoiceNumber = company.getCompanyID().substring(0,2) + INVOICE_SEPARATOR + "1";
        }
        else{
            String[] inv = StringUtils.split(lastCompanyInvoiceNumber, INVOICE_SEPARATOR);
            int invoiceNum = 0 ;
//            inv = StringUtils.split(inv[1], INVOICE_SEPARATOR);
            if(inv!=null && !(inv.length <=0)) {
                invoiceNum = Integer.parseInt(inv[1]) + 1;
            }
            else{
                invoiceNum = Integer.parseInt(lastCompanyInvoiceNumber) + 1;
            }
            invoiceNumber = company.getCompanyID().substring(0,2) + INVOICE_SEPARATOR + invoiceNum;
        }
        return invoiceNumber;
    }


    public String getLastInvoiceLocation(String companyID, String location) {
        List<InvoiceB2C> invoices = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("companyID").is(companyID));
//            query.with(Sort.by(Sort.Direction.DESC, "invoiceNumber"));
            invoices = mongoOperation.find(query, InvoiceB2C.class);
            Collections.reverse(invoices);
            for(InvoiceB2C invoice : invoices) {
                if(invoice.getInvoiceNumber().contains(location.substring(0,2)))
                    return invoice.getInvoiceNumber();
            }
        }catch(Exception ex){
            System.out.println("Error in getLastInvoiceLocation:"+ ex);
            return "";
        }
        return "";
    }

    public String getLastInvoiceNo(String companyID) {
        List<InvoiceB2C> invoices = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("companyID").is(companyID));
//            query.with(Sort.by(Sort.Direction.DESC, "invoiceNumber"));
            invoices = mongoOperation.find(query, InvoiceB2C.class);
            Collections.reverse(invoices);
            char someChar = '-';
            for(InvoiceB2C invoice : invoices){
                int count = 0;
                for (int i = 0; i < invoice.getInvoiceNumber().length(); i++) {
                    if (invoice.getInvoiceNumber().charAt(i) == someChar) {
                        count++;
                    }
                }
                if(count == 1)
                    return invoice.getInvoiceNumber();
            }
        }catch(Exception ex){
            System.out.println("Error in getLastInvoiceNoB2C:"+ ex);
            return "";
        }
        return "";
    }

    public String getAllInvoicesB2CByLocation(String companyID, String location){
        List<InvoiceB2C> invoices = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("companyID").is(companyID).and("location").is(location));
            invoices = mongoOperation.find(query, InvoiceB2C.class);
            for(InvoiceB2C invoice : invoices) {
                lineItemHelper.getLineItemsB2C(invoice.getId());
                invoice.setLineItemList(lineItemHelper.getLineItemsB2C());
            }
        }catch(Exception ex){
            System.out.println("Error in get invoices:"+ ex);
        }
        return gson.toJson(invoices);
    }

}
