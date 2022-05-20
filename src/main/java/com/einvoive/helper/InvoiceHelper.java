package com.einvoive.helper;

import com.einvoive.constants.Constants;
import com.einvoive.constants.Locations;
import com.einvoive.model.*;
import com.einvoive.util.EmailSender;
import com.einvoive.util.Utility;
import com.einvoive.repository.InvoiceRepository;
import com.einvoive.zatcaxml.*;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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

    private  String INVOICE_SEPARATOR = "-";

    private List<Invoice> invoiceListMain = null;

    private Logger logger = LoggerFactory.getLogger(InvoiceHelper.class);

    @Autowired
    EmailSender emailSender;

    @Autowired
    CompanyHelper companyHelper;

    @Autowired
    LogsHelper logsHelper;

    @Autowired
    UserHelper userHelper;

    public String save(Invoice invoice){
        ErrorCustom error = new ErrorCustom();
        String jsonError;
        try {
            Company company = companyHelper.getCompanyObject(invoice.getCompanyID());
            if(company.getLimitInvoices() == null || Integer.parseInt(company.getLimitInvoices()) > getCompanyTotalInvoices(invoice.getCompanyID())) {
                setInvoice(invoice);
                invoiceRepository.save(invoice);
                String items = "";
                for (LineItem lineItem : invoice.getLineItemList()) {
                    items = items + ", " + lineItem.getProductName();
                    lineItem.setInvoiceId(invoice.getId());
                    lineItemHelper.deleteLineItem(invoice.getId());
                    lineItemHelper.save(lineItem);
                    logsHelper.save(new Logs(invoice.getInvoiceName()+" Item added against Invoice Name", "Item added by "+Utility.getUserName(invoice.getUserId(), mongoOperation)+ ", Item Name  "+ lineItem.getProductName()+", Price "+lineItem.getPrice()+", Discount "+lineItem.getDiscount()+", Sub Total "+ lineItem.getItemSubTotal()+", Quantity "+lineItem.getQuantity()+", Tax "+lineItem.getTaxableAmount()));
                }
                logsHelper.save(new Logs("InvoiceBEB "+invoice.getInvoiceName()+" added by User "+Utility.getUserName(invoice.getUserId(), mongoOperation), Utility.getUserName(invoice.getUserId(), mongoOperation)+ " has added a new Invoice "+ invoice.getInvoiceName()+", Invoice No: "+invoice.getInvoiceNumber()+", Bill to: "+invoice.getBillTo()+", Total amount due "+ invoice.getTotalAmountDue()+", saved items "+items));
                return "Invoice saved";
            }else{
                logger.info("Sorry Company has a limit of generating "+company.getLimitInvoices()+" Invoices");
                error.setErrorStatus("Error");
                error.setError("Sorry Company has a limit of generating "+company.getLimitInvoices()+" Invoices");
                jsonError = gson.toJson(error);
                return jsonError;
            }
        }catch(Exception ex){
            logger.info("Exception in saving invoice "+ex.getMessage());
            error.setErrorStatus("Error");
            error.setError(ex.getMessage());
            jsonError = gson.toJson(error);
            return jsonError;
        }
    }

//    private boolean isAlreadySaved(String invoiceNo){
//        Invoice invoice = mongoOperation.findOne(new Query(Criteria.where("invoiceNumber").is(invoiceNo)), Invoice.class);
//        if(invoice != null)
//            return true;
//        else
//            return false;
//    }

    private void setInvoice(Invoice invoice) throws NoSuchAlgorithmException {
        invoice.setSerialNo(getAvaiablaeId(invoice.getCompanyID()));
//      UUID uuid = UUID.fromString("00809e66-36d5-436f-93c4-e4e2c76cce0d");
        invoice.setId(invoice.setId(String.valueOf(UUID.randomUUID())));
        invoice.setHash(Utility.encrypt(invoice.getId()));
        invoice.setSupplyDate(invoice.getSupplyDate());
        invoice.setPreviousHash(getPreviousHash(invoice));
//        invoice.setInvoiceNumber(getNextInvoiceNumber(invoice.getCompanyID()));
    }

//    Company having multiple user w.r.t their locations
    public String getNextInvoiceNoByUserID(String id) {
       User user = mongoOperation.findById(id, User.class);
        Optional<Locations> locationEnum = Locations.getLocationsByCode(user.getLocation());
        //check for Fugro Company
        if(locationEnum.isPresent())
            return getNextInvoiceNoLocationCode(user.getCompanyID(), locationEnum.get().getCode());
        else
            return getLastInvoiceLocation(user.getCompanyID(), user.getLocation());
    }

//    //getting next credit or debit no
//    public String getCompanyNextCreditDebitNo(String id, String type) {
//        CompanyXML company = mongoOperation.findById(id, CompanyXML.class);
//         if(type.equals(Constants.CREDIT_NOTE))
//            return getNextCreditNo(company);
//        else if(type.equals(Constants.DEBIT_NOTE))
//            return getNextDebitNo(company);
//        else
//             return gson.toJson("Sorry next "+type+" not found");
//    }

////getting next credit or debit no, fugro(loc code) based n User
//    public String getNextCreditDebitNo(String id, String type) {
//        User user = mongoOperation.findOne(new Query(Criteria.where("id").is(id)), User.class);
//        Optional<Locations> locationEnum = Locations.getLocationsByCode(user.getLocation());
//        //check for Fugro CompanyXML
//        if(locationEnum.isPresent() && type.equals(Constants.CREDIT_NOTE))
//            return getNextCreditNoWithCode(user.getCompanyID(), locationEnum.get().getCode());
//        else if(locationEnum.isPresent() && type.equals(Constants.DEBIT_NOTE))
//            return getNextDebitNoWithCode(user.getCompanyID(), locationEnum.get().getCode());
//        //fugro end
//        else
//            return gson.toJson("Sorry next "+type+" not found");
//    }

    //get Fugro InvoiceNo
//    private String getNextInvoiceNoWithLocationCode(User user, String locationCode) {
//        String invoiceNumber = null;
//        String lastCompanyInvoiceNumber = getNextInvoiceNoLocationCode(user.getCompanyID(), locationCode);
//        if (lastCompanyInvoiceNumber.isEmpty()) {
//            invoiceNumber = locationCode + INVOICE_SEPARATOR + "1";
//        } else {
//            String[] inv = StringUtils.split(lastCompanyInvoiceNumber, INVOICE_SEPARATOR);
//            int invoiceNum = 0;
//            if (inv != null && !(inv.length <= 0)) {
//                invoiceNum = Integer.parseInt(inv[1]) + 1;
//            }
//            invoiceNumber = locationCode + INVOICE_SEPARATOR + invoiceNum;
//        }
//        return invoiceNumber;
//    }

    // location based invoiceNo
    public String getNextInvoiceNoLocationCode(String companyID, String location) {
        List<Invoice> invoices = null;
        String format = location+INVOICE_SEPARATOR;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("companyID").is(companyID));
            query.addCriteria(Criteria.where("invoiceNumber").regex(format));
            invoices = mongoOperation.find(query, Invoice.class);
            if (!invoices.isEmpty()) {
                int num, max = 0;
                for (Invoice invoice : invoices) {
                    num = Utility.getAttachedNo(invoice.getInvoiceNumber(), format);
                    if (max < num)
                        max = num;
                }
                max++;
                return format + max;
            }
            else
                return format+"1";
        }catch(Exception ex){
            logger.info("Error in getNextInvoiceNoLocationCode:"+ ex.getMessage());
            return "Error in getNextInvoiceNoLocationCode:"+ ex.getMessage();
        }
    }
//    // CompanyXML based creditNo
//    private String getNextCreditNo(CompanyXML company) {
//        List<Invoice> invoices = null;
//        String format = company.getCompanyName().substring(0,2).toUpperCase(Locale.ROOT)+INVOICE_SEPARATOR+Constants.CREDIT_NOTE+INVOICE_SEPARATOR;
//        try {
//            Query query = new Query();
//            query.addCriteria(Criteria.where("companyID").is(company.getCompanyID()));
//            query.addCriteria(Criteria.where("creditNote").regex(format));
//            invoices = mongoOperation.find(query, Invoice.class);
//            if (!invoices.isEmpty()) {
//                int num, max = 0;
//                for (Invoice invoice : invoices) {
//                    num = getAttachedNo(invoice.getCreditNote(), format);
//                    if (max < num)
//                        max = num;
//                }
//                max++;
//                return format + max;
//            }
//            else
//                return format+"1";
//        }catch(Exception ex){
//            logger.info("Error in getNextCreditNo:"+ ex.getMessage());
//            return "Error in getNextCreditNo:"+ ex.getMessage();
//        }
//    }

//    //next DenitNo
//    private String getNextDebitNo(CompanyXML company) {
//        List<Invoice> invoices = null;
//        String format = company.getCompanyName().substring(0,2).toUpperCase()+INVOICE_SEPARATOR+Constants.DEBIT_NOTE+INVOICE_SEPARATOR;
//        try {
//            Query query = new Query();
//            query.addCriteria(Criteria.where("companyID").is(company.getCompanyID()));
//            query.addCriteria(Criteria.where("debitNote").regex(format));
//            invoices = mongoOperation.find(query, Invoice.class);
//            if (!invoices.isEmpty()) {
//                int num, max = 0;
//                for (Invoice invoice : invoices) {
//                    num = getAttachedNo(invoice.getDebitNote(), format);
//                    if (max < num)
//                        max = num;
//                }
//                max++;
//                return format + max;
//            }
//            else
//                return format+"1";
//        }catch(Exception ex){
//            logger.info("Error in getNextDebitNo:"+ ex.getMessage());
//            return "Error in getNextDebitNo:"+ ex.getMessage();
//        }
//    }

//    // location based creditNo Fugro
//    private String getNextCreditNoWithCode(String companyID, String location) {
//        List<Invoice> invoices = null;
//        String format = location+INVOICE_SEPARATOR+Constants.CREDIT_NOTE+INVOICE_SEPARATOR;
//        try {
//            Query query = new Query();
//            query.addCriteria(Criteria.where("companyID").is(companyID));
//            query.addCriteria(Criteria.where("creditNote").regex(format));
//            invoices = mongoOperation.find(query, Invoice.class);
//            if (!invoices.isEmpty()) {
//                int num, max = 0;
//                for (Invoice invoice : invoices) {
//                    num = getAttachedNo(invoice.getCreditNote(), format);
//                    if (max < num)
//                        max = num;
//                }
//                max++;
//                return format + max;
//            }
//            else
//                return format+"1";
//        }catch(Exception ex){
//            logger.info("Error in getNextCreditNo:"+ ex.getMessage());
//            return "Error in getNextCreditNo:"+ ex.getMessage();
//        }
//    }
//
//    private int getAttachedNo(String invoiceNo, String format) {
//        int num = 1;
//        String[] inv = StringUtils.split(invoiceNo, format);
//        num = Integer.parseInt(inv[inv.length - 1]);
//        return num;
//    }

//    //next DebitNo Fugro
//    private String getNextDebitNoWithCode(String companyID, String location) {
//        List<Invoice> invoices = null;
//        String format = location+INVOICE_SEPARATOR+Constants.DEBIT_NOTE+INVOICE_SEPARATOR;
//        try {
//            Query query = new Query();
//            query.addCriteria(Criteria.where("companyID").is(companyID));
//            query.addCriteria(Criteria.where("debitNote").regex(format));
//            invoices = mongoOperation.find(query, Invoice.class);
//            if (!invoices.isEmpty()) {
//                int num, max = 0;
//                for (Invoice invoice : invoices) {
//                    num = getAttachedNo(invoice.getDebitNote(), format);
//                    if (max < num)
//                        max = num;
//                }
//                max++;
//                return format + max;
//            }
//            else
//                return format+"1";
//        }catch(Exception ex){
//            logger.info("Error in getNextDebitNo:"+ ex.getMessage());
//            return "Error in getNextDebitNo:"+ ex.getMessage();
//        }
//    }

    //    When single user CompanyXML request
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
            logger.info("Error in get invoices:"+ ex.getMessage());
            System.out.println("Error in get invoices:"+ ex.getMessage());
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
            logger.info("Error in get invoices:"+ ex.getMessage());
            System.out.println("Error in get invoices:"+ ex.getMessage());
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
            logger.info("Error in get invoices:"+ ex.getMessage());
            System.out.println("Error in get invoices:"+ ex.getMessage());
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
            logger.info("Error in get invoices:"+ ex.getMessage());
            System.out.println("Error in get invoices:"+ ex.getMessage());
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
            logger.info("Error in get invoices:"+ ex.getMessage());
            System.out.println("Error in get invoices:"+ ex.getMessage());
        }
        return gson.toJson(invoices);
    }

//    public String getAllInvoicesByLocation(String companyID, String location){
//        List<Invoice> invoices = null;
//        try {
//            Query query = new Query();
//            query.addCriteria(Criteria.where("companyID").is(companyID).and("location").is(location));
//            invoices = mongoOperation.find(query, Invoice.class);
//            for(Invoice invoice : invoices) {
//                lineItemHelper.getLineItems(invoice.getId());
//                invoice.setLineItemList(lineItemHelper.getLineItems());
//            }
//        }catch(Exception ex){
//            System.out.println("Error in get invoices:"+ ex);
//        }
//        return gson.toJson(invoices);
//    }

    //get Invoices by User ID, test case use
    public String getInvoicesByUser(String userId){
        List<String> numList = new ArrayList<>();
        List<Invoice> invoices = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("userId").is(userId));
            invoices = mongoOperation.find(query, Invoice.class);
            for(Invoice invoice : invoices) {
                lineItemHelper.getLineItems(invoice.getId());
                invoice.setLineItemList(lineItemHelper.getLineItems());
            }
        }catch(Exception ex){
            logger.info("Error in get invoices:"+ ex.getMessage());
            System.out.println("Error in get invoices:"+ ex.getMessage());
        }
        for(Invoice invoice:invoices)
            numList.add(invoice.getInvoiceNumber());
        return gson.toJson(numList);
    }

    public void addAllInvoices(String companyID, List<Invoice> list){
            Query query = new Query();
            query.addCriteria(Criteria.where("companyID").is(companyID));
            List<Invoice> invoices = mongoOperation.find(query, Invoice.class);
            for(Invoice invoice : invoices) {
                lineItemHelper.getLineItems(invoice.getId());
                invoice.setLineItemList(lineItemHelper.getLineItems());
            }
            list.addAll(invoices);
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
            logger.info("Error in get invoices:"+ ex.getMessage());
            System.out.println("Error in get invoices:"+ ex.getMessage());
        }
        return gson.toJson(invoices);
    }

    public String getLastInvoiceLocation(String companyID, String location) {
        List<Invoice> invoices = null;
        String format = companyID.substring(0, 2) + INVOICE_SEPARATOR + location.substring(0, 2) + INVOICE_SEPARATOR;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("invoiceNumber").regex(format));
           invoices = mongoOperation.find(query, Invoice.class);
            if (!invoices.isEmpty()) {
                int num, max = 0;
                for (Invoice invoice : invoices) {
                    num = Utility.getAttachedNo(invoice.getInvoiceNumber(), format);
                    if (max < num)
                        max = num;
                }
                max++;
                return format + max;
            }else
                return format + "1";
        }catch(Exception ex){
            System.out.println("Error in getLastInvoiceByCompany:"+ ex.getMessage());
            logger.info("Error in getLastInvoiceByCompany:"+ ex.getMessage());
            return "Error in getLastInvoiceByCompany:"+ ex.getMessage();
        }
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
            System.out.println("Error in getLastInvoiceByCompany:"+ ex.getMessage());
            logger.info("Error in getLastInvoiceByCompany:"+ ex.getMessage());
            return "Error in getLastInvoiceByCompany:"+ ex.getMessage();
        }
        return "";
    }

    public String deleteInvoice(String invoiceID){
        Invoice invoice = mongoOperation.findById(invoiceID, Invoice.class);
        invoiceRepository.delete(invoice);
        try {
            lineItemHelper.deleteAllLineItemByInvoiceId(invoiceID);
        }catch (Exception exception){
           logger.info("Exception in delete All Line Items by Invoice ID in deleteInvoice "+exception.getMessage());
        }
        logsHelper.save(new Logs("Delete Invoice "+invoice.getInvoiceName(), "Invoice delection: "+invoice.getInvoiceName()+" will be deleted"));
        logger.info("Invoice deleted "+invoice.getInvoiceName());
        return "Invoice deleted";
    }

    public String getAvaiablaeId(String companyID) {
        List<Invoice> invoiceList = mongoOperation.find(new Query(Criteria.where("companyID").is(companyID)), Invoice.class);
        return String.valueOf(invoiceList.size()+1);
    }

    public String update(Invoice invoice) {
        deleteInvoice(invoice.getId());
        logger.info("Invoice updated "+invoice.getInvoiceName());
        logsHelper.save(new Logs("Invoice updated "+invoice.getInvoiceName(), "Invoice updated "+invoice.getInvoiceName()+" will be deleted and saved for updation"));
        return save(invoice);
    }

    public String getInvoiceStatus(String id) {
        Invoice invoice = mongoOperation.findOne(new Query(Criteria.where("id").is(id)), Invoice.class);
        return invoice.getStatus();
    }

    public Resource load(Path file) throws MalformedURLException {
        try {
//            Path file = Paths.get(Constants.INVOICES_PATH+invoiceNo);
            String[] filePath = file.toFile().list();
            Resource resource = new UrlResource(file.toUri()+filePath[0]);
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                logger.info("File not found");
                throw new RuntimeException("File not found");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public String updateInvoiceAttachment(MultipartFile file, String invoiceNo){
        try {
            deleteInvoiceAttachment(invoiceNo);
            uploadFile(file, invoiceNo);
            return gson.toJson("File updated");
        }catch (Exception ex){
            logger.info(ex.getMessage());
            return gson.toJson(ex.getMessage());
        }
    }

    public String deleteInvoiceAttachment(String invoiceNo){
        try {
            File dir = new File(Constants.INVOICES_PATH+invoiceNo);
            File[] listFiles = dir.listFiles();
            for(File file : listFiles){
                logger.info("Deleting "+file.getName());
                file.delete();
            }
            logger.info("Deleting Directory Success = "+dir.delete());
            return gson.toJson("Deleting file on path "+invoiceNo);
        }catch (Exception exception){
            return gson.toJson(exception.getMessage());
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
            logger.info(exception.getMessage());
            return gson.toJson(exception.getMessage());
        }
    }

//    public String setInvoiceStatus(String id, String status) {
//        ErrorCustom error = new ErrorCustom();
//        String jsonError;
//        try {
//            Invoice invoice = mongoOperation.findOne(new Query(Criteria.where("id").is(id)), Invoice.class);
//            if(invoice != null){
//                invoice.setStatus(status);
////                journalEntriesHelper.setToSave(invoice);
//                if(status.equals(Constants.STATUS_APPROVED)) {
//                    User user = mongoOperation.findOne(new Query(Criteria.where("id").is(invoice.getUserId())), User.class);
//                    if(user != null)
//                        emailSender.sendEmail(user.getEmail(), "Invoice Approved", "Your Invoice has benn approved. Please have a look on Invoice: "+invoice.getInvoiceNumber());
//                    journalEntriesHelper.requestHanler(invoice);
//                }
//                if(status.equals(Constants.STATUS_FORAPPROVAL)){
//                    User user = mongoOperation.findOne(new Query(Criteria.where("location").is(invoice.getLocation())), User.class);
//                    if(user != null)
//                        emailSender.sendEmail(user.getEmail(), "Invoice Approval", "Please Approve this Invoice: "+invoice.getInvoiceNumber());
//                }
//                logsHelper.save(new Logs("Changing Invoice Status for "+invoice.getInvoiceName(), "User "+Utility.getUserName(invoice.getUserId(), mongoOperation)+" has changed invoice "+invoice.getInvoiceName()+" status to "+status));
//                repository.save(invoice);
////                saveLogs(invoice);
//                return "Invoice Status Updated";
//            }
//            else{
//                error.setErrorStatus("Error");
//                error.setError("No Invoice against this ID");
//                jsonError = gson.toJson(error);
//                return jsonError;
//            }
//        }
//        catch (Exception ex){
//            error.setErrorStatus("Error");
//            error.setError(ex.getMessage());
//            jsonError = gson.toJson(error);
//            return jsonError;
//        }
//    }

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
            logger.info("Error in get invoices By ID :"+ ex.getMessage());
            System.out.println("Error in get invoices By ID :"+ ex.getMessage());
        }
        return gson.toJson(invoice);
    }

    public String getInvoicesByID(String id) {
        List<Invoice> invoicesList = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("id").is(id));
            invoicesList = mongoOperation.find(query, Invoice.class);
        }catch(Exception ex){
            logger.info("Error in get invoices By ID :"+ ex.getMessage());
            System.out.println("Error in get invoices By ID :"+ ex.getMessage());
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
                invoiceRepository.save(invoice);
                return "Invoice Status Updated";
            }
            else{
                logger.info("No Invoice against this ID "+invoiceID);
                error.setErrorStatus("Error");
                error.setError("No Invoice against this ID");
                jsonError = gson.toJson(error);
                return jsonError;
            }
        }
        catch (Exception ex){
            logger.info("Invoice Status has Exception "+ex.getMessage());
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
            logger.info("Date parse exception in getInvoicesByDurationLocation "+e.getMessage());
        }
        List<Invoice> invoiceList = mongoOperation.find(new Query(Criteria.where("companyID").is(companyID)
                .and("invoiceDate").gte(startDateFinal).lte(endDateFinal).and("location").is(location)), Invoice.class);
        if(invoiceList.isEmpty())
            return gson.toJson("No Record found");
        else
            return gson.toJson(invoiceListMain);
    }

    public void updateCustomerName(String customerNameNew, String customerNameOld) {
        Update update = new Update();
        try {
            update.set("customerName", customerNameNew);
            mongoOperation.updateMulti(new Query(Criteria.where("customerName").is(customerNameOld)), update, Invoice.class);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    public File getInvoiceXML(String invoiceID) throws Exception {
        ConvertToXML convertToXML = new ConvertToXML();
        Invoice invoice = mongoOperation.findById(invoiceID, Invoice.class);
        Company company = mongoOperation.findOne(new Query(Criteria.where("companyID").is(invoice.getCompanyID())), Company.class);
        User user = mongoOperation.findById(invoice.getUserId(), User.class);
        Customer customer = mongoOperation.findById(invoice.getBillTo(), Customer.class);
        InvoiceXML invoiceXML = new InvoiceXML(2.1, invoice.getInvoiceNumber(), Utility.getDateHH(invoice.getDateTime()),new InvoiceTypeCode("UN/ECE 1001 Subset", "6", "380"), new Note("en", "Ordered in our booth at the convention."), "2009-12-15", new InvoiceXML.DocumentCurrencyCode("ISO 4217 Alpha", "6", "SAR"), "Project cost code 123");
        invoiceXML.setInvoicePeriod(new InvoicePeriod(Utility.getDateHH(invoice.getSupplyDate()), Utility.getDateHH(invoice.getPaymentDue())));
        invoiceXML.setOrderReference(new com.einvoive.zatcaxml.OrderReference(invoice.getReferenceField()));
        invoiceXML.setContractDocumentReference(new ContractDocumentReference("Contract Dummy", "Framework agreement dummy"));
        invoiceXML.setAdditionalDocumentReference(new AdditionalDocumentReference("Doc1", "Timesheet", new Attachment(new Attachment.ExternalReference("http://www.suppliersite.eu/sheet001.html"))));
        //Supplier Party
        EndpointID endpointID = new EndpointID("GLN", "9", "1234567890123");
        Country country = new Country(new IdentificationCode("ISO3166-1", "6", company.getCountry()));
        PostalAddress postalAddress = new PostalAddress(new PostalAddress.ID("GLN", "9", company.getCompanyID()), company.getPostalCode(), company.getAddress2(), "nill", company.getAddress1(), "nill", company.getCity(), company.getPostalCode(), company.getCountry(), country);
        PartyTaxScheme partyTaxScheme = new PartyTaxScheme(new CompanyXML("DKVAT", "ZZZ", invoice.getCompanyID()), new TaxScheme(new Scheme("UN/ECE 5153", "6", company.getVatNumber_Company())));
        PartyLegalEntity partyLegalEntity = new PartyLegalEntity(company.getCompanyName(), new CompanyXML("CVR", "ZZZ", company.getCompanyID()), new RegistrationAddress(company.getCity(), company.getCountry(), new RegistrationAddress.Country1(company.getState())));
        Contact contact = new Contact(company.getCellNo(), "nill", company.getEmail());
        Person person = new Person(Utility.getSplitString(user.getName())[0], "M", Utility.getSplitString(user.getName())[1], "User");
        Party partySupplier = new Party(endpointID, new PartyIdentification(new ID("ZZZ", "Supp123")), new Party.PartyName(company.getCompanyName()), postalAddress, partyTaxScheme, partyLegalEntity, contact, person);
        invoiceXML.setAccountingSupplierParty(new AccountingSupplierParty(partySupplier));
        //customer party
        EndpointID endpointID1 = new EndpointID("GLN", "9", "1234567890123");
        Country country1 = new Country(new IdentificationCode("ISO3166-1", "6", customer.getShippingCountry()));
        PostalAddress postalAddress1 = new PostalAddress(new PostalAddress.ID("GLN", "9", customer.getCompanyID()), customer.getShippingPostal(), customer.getShippingAddress2(), "nill", customer.getShippingAddress2(), "nill", customer.getShippingCity(), customer.getShippingPostal(), customer.getShippingCountry(), country1);
        PartyTaxScheme partyTaxScheme1 = new PartyTaxScheme(new CompanyXML("DKVAT", "ZZZ", customer.getId()), new TaxScheme(new Scheme("UN/ECE 5153", "6", customer.getVatNumber_Customer())));
        PartyLegalEntity partyLegalEntity1 = new PartyLegalEntity(customer.getCustomer(), new CompanyXML("CVR", "ZZZ", customer.getCompanyID()), new RegistrationAddress(customer.getShippingCity(), customer.getShippingCountry(), new RegistrationAddress.Country1(customer.getShippingProvince())));
        Contact contact1 = new Contact(customer.getPhone(), "nill", customer.getEmail());
        Person person1 = new Person(customer.getFirstName(), "M", customer.getLastName(), "Customer");
        Party partyCustomer = new Party(endpointID1, new PartyIdentification(new ID("ZZZ", customer.getId())), new Party.PartyName(customer.getCustomer()), postalAddress1, partyTaxScheme1, partyLegalEntity1, contact1, person1);
        invoiceXML.setAccountingCustomerParty(new AccountingCustomerParty(partyCustomer));
        PartyLegalEntity partyLegalEntity2 = new PartyLegalEntity();
        partyLegalEntity2.setCompany(new CompanyXML("UK:CH", "ZZZ", "6411982340"));
        invoiceXML.setPayeeParty(new PayeeParty(new PartyIdentification(new ID("GLN", "9", "098740918237")), new Party.PartyName("Ebeneser Scrooge Inc."), partyLegalEntity2));
        invoiceXML.setDelivery(new Delivery("2009-12-15", new Delivery.DeliveryLocation(new Scheme("GLN", "9", "6754238987648"), new Address("Deliverystreet", "Side door", "12", "DeliveryCity", "523427", "RegionC", new Country(new IdentificationCode("BE"))))));
        PayeeFinancialAccount payeeFinancialAccount = new PayeeFinancialAccount("DK1212341234123412", new PayeeFinancialAccount.FinancialInstitutionBranch(new PayeeFinancialAccount.FinancialInstitutionBranch.FinancialInstitution("DKDKABCD")) ,"Address");
        invoiceXML.setPaymentMeans(new PaymentMeans(new PaymentMeans.PaymentMeansCode("UN/ECE 4461", "31"), "2009-12-31", "IBAN", "Payref1", payeeFinancialAccount));
        invoiceXML.setPaymentTerms(new PaymentTerms("Penalty percentage 10% from due date"));
        invoiceXML.setAllowanceCharge(new AllowanceCharge(false, "Promotion Discount", new Amount("SAR", "100")) );
        TaxScheme taxScheme = new TaxScheme(new Scheme("UN/ECE 5153", "6", "VAT"));
        invoiceXML.setTaxTotal(new TaxTotal(new Amount("SAR", "293.20"), new TaxSubtotal(new Amount("SAR", "140.20"), new Amount("SAR", "292.1"), new TaxCategory(new ID("UN/ECE 5305", "6", "S"), "20", new EndpointID("UN/ECE 5153", "6", "VAT"), "Exempt New Means of Transport", taxScheme))));
        Amount amount = new Amount("SAR", "293.20");
        invoiceXML.setLegalMonetaryTotal(new LegalMonetaryTotal(amount, amount, amount, amount, amount, amount, amount, amount));
        AllowanceCharge allowanceCharge = new AllowanceCharge(false, "Damage", new Amount("SAR", "100"));
        Item item = new Item(new Item.Description("EN", "Processor: Intel Core 2 Duo SU9400 LV (1.4GHz)"), "Laptop", new ItemIdentification(new EndpointID("1234567890124")), new ItemIdentification(new EndpointID("GTIN", "9", "3356")), new CommodityClassification(new IdentificationCode("113", "UNSPSC", "41246")), new TaxCategory(new ID("UN/ECE 5153", "3"), "20", new EndpointID("UN/ECE 5153", "6", "VAT"), "Exempt New Means of Transport", taxScheme) );
        InvoiceLine invoiceLine = new InvoiceLine("1", "Scratch on box", new InvoicedQuantity("C62", "1"), new Amount("SAR", "1273"), "BookingCode001", new InvoiceLine.OrderLineReference("1"), new AllowanceCharge(false,"Damage", new Amount("SAR", "1253")), new InvoiceLine.TaxTotal1(new Amount("SAR", "1253")), item, new Price(new Amount("SAR", "1253"), new InvoicedQuantity("C62", "1"), allowanceCharge));
//        List<InvoiceLine> invoiceLines = new ArrayList<>();
//        invoiceLines.add(invoiceLine);
        invoiceXML.setInvoiceLine(invoiceLine);
        return convertToXML.ConvertInvoiceToXML(invoiceXML);
    }

}
