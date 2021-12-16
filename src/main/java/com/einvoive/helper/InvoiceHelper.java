package com.einvoive.helper;

import com.einvoive.constants.Constants;
import com.einvoive.constants.Locations;
import com.einvoive.model.*;
import com.einvoive.util.EmailSender;
import com.einvoive.util.Utility;
import com.einvoive.repository.InvoiceRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

@Component
public class InvoiceHelper {

    @Autowired
    InvoiceRepository repository;

    @Autowired
    LineItemHelper lineItemHelper;

    @Autowired
    MongoOperations mongoOperation;

    @Autowired
     InvoiceRepository invoiceRepository;

    @Autowired
    JournalEntriesHelper journalEntriesHelper;

    @Autowired
    private GridFsTemplate template;

    private Gson gson = new Gson();

    private String INVOICE_SEPARATOR = "-";

    private  List<Invoice> invoiceListMain = null;

    @Autowired
    EmailSender emailSender;

    @Autowired
    CompanyHelper companyHelper;

    @Autowired
    LogsHelper logsHelper;

    public String save(Invoice invoice){
        ErrorCustom error = new ErrorCustom();
        String jsonError;
        try {
            Company company = companyHelper.getCompanyObject(invoice.getCompanyID());
            if(company.getLimitInvoices() == null || Integer.parseInt(company.getLimitInvoices()) > getCompanyTotalInvoices(invoice.getCompanyID())) {
                setInvoice(invoice);
                repository.save(invoice);
                for (LineItem lineItem : invoice.getLineItemList()) {
                    lineItem.setInvoiceId(invoice.getId());
                    lineItemHelper.save(lineItem);
                }
                return "Invoice saved";
            }else{
                error.setErrorStatus("Error");
                error.setError("Sorry Company has a limit of generationg "+company.getLimitInvoices()+" Invoices");
                jsonError = gson.toJson(error);
                return jsonError;
            }
        }catch(Exception ex){
            error.setErrorStatus("Error");
            error.setError(ex.getMessage());
            jsonError = gson.toJson(error);
            return jsonError;
        }
    }

    private void setInvoice(Invoice invoice) throws NoSuchAlgorithmException {
        invoice.setSerialNo(getAvaiablaeId(invoice.getCompanyID()));
//      UUID uuid = UUID.fromString("00809e66-36d5-436f-93c4-e4e2c76cce0d");
        invoice.setId(invoice.setId(String.valueOf(UUID.randomUUID())));
        invoice.setHash(Utility.encrypt(invoice.getId()));
        invoice.setPreviousHash(getPreviousHash(invoice));
//        invoice.setInvoiceNumber(getNextInvoiceNumber(invoice.getCompanyID()));
    }

//    Company having multiple user w.r.t their locations
    public String getNextInvoiceNoByUserID(String id) {
        User user = mongoOperation.findOne(new Query(Criteria.where("id").is(id)), User.class);
        Optional<Locations> locationEnum = Locations.getLocationsByValue(user.getLocation());
        //check for Fugro Company
        if(locationEnum.isPresent())
            return getNextInvoiceNoWithLocationCode(user, locationEnum.get().getCode());
        else{
            String lastCompanyInvoiceNumber = getLastInvoiceLocation(user.getCompanyID(), user.getLocation());
            String invoiceNumber = "";
            if (lastCompanyInvoiceNumber.isEmpty()) {
                invoiceNumber = user.getCompanyID().substring(0, 2) + INVOICE_SEPARATOR + user.getLocation().substring(0, 2) + INVOICE_SEPARATOR + "1";
            } else {
                String[] inv = StringUtils.split(lastCompanyInvoiceNumber, INVOICE_SEPARATOR);
                int invoiceNum = 0;
                inv = StringUtils.split(String.valueOf(inv[1]), INVOICE_SEPARATOR);
                if (inv != null && !(inv.length <= 0)) {
                    invoiceNum = Integer.parseInt(inv[1]) + 1;
                } else {
                    invoiceNum = Integer.parseInt(lastCompanyInvoiceNumber) + 1;
                }
                invoiceNumber = user.getCompanyID().substring(0, 2) + INVOICE_SEPARATOR + user.getLocation().substring(0, 2) + INVOICE_SEPARATOR + invoiceNum;
            }
            return invoiceNumber;
        }
    }

    //get Fugro InvoiceNo
    private String getNextInvoiceNoWithLocationCode(User user, String locationCode) {
        String invoiceNumber = null;
        String lastCompanyInvoiceNumber = getNextInvoiceNoLocationCode(user.getCompanyID(), locationCode);
        if (lastCompanyInvoiceNumber.isEmpty()) {
            invoiceNumber = locationCode + INVOICE_SEPARATOR + "1";
        } else {
            String[] inv = StringUtils.split(lastCompanyInvoiceNumber, INVOICE_SEPARATOR);
            int invoiceNum = 0;
            if (inv != null && !(inv.length <= 0)) {
                invoiceNum = Integer.parseInt(inv[1]) + 1;
            }
            invoiceNumber = locationCode + INVOICE_SEPARATOR + invoiceNum;
        }
        return invoiceNumber;
    }

    // location based invoiceNo
    public String getNextInvoiceNoLocationCode(String companyID, String location) {
        List<InvoiceB2C> invoices = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("companyID").is(companyID));
//            query.with(Sort.by(Sort.Direction.DESC, "invoiceNumber"));
            invoices = mongoOperation.find(query, InvoiceB2C.class);
            Collections.reverse(invoices);
            for(InvoiceB2C invoice : invoices) {
                String[] inv = StringUtils.split(invoice.getInvoiceNumber(), INVOICE_SEPARATOR);
                if(inv[0].equals(location))
                    return invoice.getInvoiceNumber();
            }
        }catch(Exception ex){
            System.out.println("Error in getLastInvoiceLocationFugro:"+ ex.getMessage());
            return "Error in getLastInvoiceLocationFugro:"+ ex.getMessage();
        }
        return "";
    }

    //    When single user Company request
    public String getNextInvoiceNoIndividual(String id){
        Company company = mongoOperation.findOne(new Query(Criteria.where("id").is(id)), Company.class);
        String lastCompanyInvoiceNumber = getLastInvoiceNo(company.getCompanyID());
        String invoiceNumber = "";
        if (lastCompanyInvoiceNumber.isEmpty()) {
            invoiceNumber = company.getCompanyID().substring(0, 2) + INVOICE_SEPARATOR + "1";
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

    private String getPreviousHash(Invoice invoice) {
        List<Invoice> invoiceList = mongoOperation.findAll(Invoice.class);
        String previousHash = "";
        if(invoiceList.size() > 0)
            previousHash = invoiceList.get(invoiceList.size()-1).getHash();
        else
            previousHash = "";

        return previousHash;
    }

    public String getInvoicesByCustomer(String customerName){
        List<Invoice> invoices = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("customerName").is(customerName));
            invoices = mongoOperation.find(query, Invoice.class);
            for(Invoice invoice : invoices) {
                lineItemHelper.getLineItems(invoice.getId());
                invoice.setLineItemList(lineItemHelper.getLineItems());
            }
        }catch(Exception ex){
            System.out.println("Error in get invoices:"+ ex);
        }
        return gson.toJson(invoices);
    }

    //Location based
    public String getInvoicesByCustomerLocation(String customerName, String location){
        List<Invoice> invoices = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("customerName").is(customerName).and("location").is(location));
            invoices = mongoOperation.find(query, Invoice.class);
            for(Invoice invoice : invoices) {
                lineItemHelper.getLineItems(invoice.getId());
                invoice.setLineItemList(lineItemHelper.getLineItems());
            }
        }catch(Exception ex){
            System.out.println("Error in get invoices:"+ ex);
        }
        return gson.toJson(invoices);
    }

    public String getInvoicesByInvoiceNo(String invoiceNo){
        List<Invoice> invoices = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("invoiceNumber").is(invoiceNo));
            invoices = mongoOperation.find(query, Invoice.class);
            for(Invoice invoice : invoices) {
                lineItemHelper.getLineItems(invoice.getId());
                invoice.setLineItemList(lineItemHelper.getLineItems());
            }
        }catch(Exception ex){
            System.out.println("Error in get invoices:"+ ex);
        }
        return gson.toJson(invoices);
    }

    private int getCompanyTotalInvoices(String companyID){
        Query query = new Query();
        query.addCriteria(Criteria.where("companyID").is(companyID));
        List<Invoice>invoices = mongoOperation.find(query, Invoice.class);
        return invoices.size();
    }

    public Invoice getInvoiceByInvoiceNoQR(String invoiceNumber){
        Invoice invoices = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("invoiceNumber").is(invoiceNumber));
            invoices = mongoOperation.findOne(query, Invoice.class);
//            for(Invoice invoice : invoices) {
//                lineItemHelper.getLineItems(invoice.getId());
//                invoice.setLineItemList(lineItemHelper.getLineItems());
//            }
        }catch(Exception ex){
            System.out.println("Error in get invoices:"+ ex);
        }
        return invoices;
    }

    public String getInvoicesByStatus(String status){
        List<Invoice> invoices = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("status").is(status));
            invoices = mongoOperation.find(query, Invoice.class);
            for(Invoice invoice : invoices) {
                lineItemHelper.getLineItems(invoice.getId());
                invoice.setLineItemList(lineItemHelper.getLineItems());
            }
        }catch(Exception ex){
            System.out.println("Error in get invoices:"+ ex);
        }
        return gson.toJson(invoices);
    }

    public String getAllInvoicesByLocation(String companyID, String location){
        List<Invoice> invoices = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("companyID").is(companyID).and("location").is(location));
            invoices = mongoOperation.find(query, Invoice.class);
            for(Invoice invoice : invoices) {
                lineItemHelper.getLineItems(invoice.getId());
                invoice.setLineItemList(lineItemHelper.getLineItems());
            }
        }catch(Exception ex){
            System.out.println("Error in get invoices:"+ ex);
        }
        return gson.toJson(invoices);
    }

    //direct api method
    public String getInvoicesByCompany(String companyID){
        List<Invoice> invoices = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("companyID").is(companyID));
            invoices = mongoOperation.find(query, Invoice.class);
            for(Invoice invoice : invoices) {
                lineItemHelper.getLineItems(invoice.getId());
                invoice.setLineItemList(lineItemHelper.getLineItems());
            }
        }catch(Exception ex){
            System.out.println("Error in get invoices:"+ ex);
        }
        return gson.toJson(invoices);
    }

    public String getLastInvoiceLocation(String companyID, String location) {
        List<Invoice> invoices = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("companyID").is(companyID));
//            query.with(Sort.by(Sort.Direction.DESC, "invoiceNumber"));     //Sorting is done through collection as its not working
            invoices = mongoOperation.find(query, Invoice.class);
            Collections.reverse(invoices);
            for(Invoice invoice : invoices) {
                if(invoice.getInvoiceNumber().contains(location.substring(0,2)))
                    return invoice.getInvoiceNumber();
            }
        }catch(Exception ex){
            System.out.println("Error in getLastInvoiceByCompany:"+ ex);
            return "";
        }
        return "";
    }

    public String getLastInvoiceNo(String companyID) {
        List<Invoice> invoices = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("companyID").is(companyID));
//            query.with(Sort.by(Sort.Direction.DESC, "invoiceNumber"));
            invoices = mongoOperation.find(query, Invoice.class);
            Collections.reverse(invoices);
            char someChar = '-';
            for(Invoice invoice : invoices){
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
            System.out.println("Error in getLastInvoiceByCompany:"+ ex);
            return "";
        }
        return "";
    }

    public String deleteInvoice(String invoiceID){
        List<Invoice> invoices = mongoOperation.find(new Query(Criteria.where("id").is(invoiceID)), Invoice.class);
        repository.deleteAll(invoices);
        return "Invoice deleted";
    }

    public String getAvaiablaeId(String companyID) {
        List<Invoice> invoiceList = mongoOperation.find(new Query(Criteria.where("companyID").is(companyID)), Invoice.class);
        return String.valueOf(invoiceList.size()+1);
    }

    public String update(Invoice invoice) {
        deleteInvoice(invoice.getId());
        return save(invoice);
    }

    public String getInvoiceStatus(String id) {
        Invoice invoice = mongoOperation.findOne(new Query(Criteria.where("id").is(id)), Invoice.class);
        return invoice.getStatus();
    }

    public Resource load(String invoiceNo) throws MalformedURLException {
        try {
            Path file = Paths.get(Constants.INVOICES_PATH+invoiceNo);
            String[] filePath = file.toFile().list();
            Resource resource = new UrlResource(file.toUri()+filePath[0]);
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public String uploadFile(MultipartFile file, String invoiceNo)  {
        try{
             byte[] bytes = file.getBytes();
             Path path = Paths.get(Constants.INVOICES_PATH+invoiceNo);
            if(!path.toFile().exists())
                Files.createDirectories(path);
            Files.copy(file.getInputStream(), path.resolve(file.getOriginalFilename()));
            return gson.toJson("Uploaded the file successfully: " + file.getOriginalFilename());
          }catch (Exception exception){
            return gson.toJson(exception.getMessage());
        }
    }

    public String setInvoiceStatus(String id, String status) {
        ErrorCustom error = new ErrorCustom();
        String jsonError;
        try {
            Invoice invoice = mongoOperation.findOne(new Query(Criteria.where("id").is(id)), Invoice.class);
            if(invoice != null){
                invoice.setStatus(status);
//                journalEntriesHelper.setToSave(invoice);
                if(status.equals(Constants.STATUS_APPROVED)) {
                    User user = mongoOperation.findOne(new Query(Criteria.where("id").is(invoice.getUserId())), User.class);
                    if(user != null)
                        emailSender.sendEmail(user.getEmail(), "Invoice Approved", "Your Invoice has benn approved. Please have a look on Invoice: "+invoice.getInvoiceNumber());
                    journalEntriesHelper.requestHanler(invoice);
                }
                if(status.equals(Constants.STATUS_FORAPPROVAL)){
                    User user = mongoOperation.findOne(new Query(Criteria.where("location").is(invoice.getLocation())), User.class);
                    if(user != null)
                        emailSender.sendEmail(user.getEmail(), "Invoice Approval", "Please Approve this Invoice: "+invoice.getInvoiceNumber());
                }
                repository.save(invoice);
//                saveLogs(invoice);
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
        List<Invoice> invoicesList = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("id").is(id));
            invoicesList = mongoOperation.find(query, Invoice.class);
        }catch(Exception ex){
            System.out.println("Error in get invoices By ID :"+ ex);
        }
        return gson.toJson(invoicesList);
    }

    public String setInvoiceStatusByInvoiceID(String invoiceID, String status) {
        ErrorCustom error = new ErrorCustom();
        String jsonError;
        try {
            Invoice invoice = mongoOperation.findOne(new Query(Criteria.where("invoiceNumber").is(invoiceID)), Invoice.class);
            if(invoice != null){
                invoice.setStatus(status);
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

    public String getInvoicesByDurationLocation(String startDate, String endDate, String companyID, String location) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date startDateFinal = null;
        Date endDateFinal = null;
        try {
            startDateFinal = df.parse(startDate);
            endDateFinal = df.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<Invoice> invoiceList = mongoOperation.find(new Query(Criteria.where("companyID").is(companyID)
                .and("invoiceDate").gte(startDateFinal).lte(endDateFinal).and("location").is(location)), Invoice.class);
        if(invoiceList.isEmpty())
            return gson.toJson("No Record found");
        else
            return gson.toJson(invoiceListMain);
    }

}
