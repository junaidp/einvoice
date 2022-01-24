package com.einvoive.helper;

import com.einvoive.model.*;
import com.einvoive.repository.LineItemRepository;
import com.einvoive.constants.Constants;
import com.einvoive.repository.UserRepository;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Component
public class ReportsHelper {
    @Autowired
    LineItemRepository repository;
    @Autowired
    InvoiceHelper invoiceHelper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MongoOperations mongoOperation;
    Gson gson = new Gson();
    private List<Invoice> invoiceListMain;
    private int year;
    private Logger logger = LoggerFactory.getLogger(ReportsHelper.class);
    //product sales year wise

    public String getTopSoldProductsByDate(String startDate, String endDate, String companyID) throws ParseException {
//        DateFormat df = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
//        startDate = startDate+" 00:00:00";
//        endDate = endDate+" 23:59:59";
//        Date startDateFinal = df.parse(startDate);
//        Date endDateFinal = df.parse(endDate);
        List<LineItem> lineItemList = null;
        List<LineItem> topLineItemsList = new ArrayList<LineItem>();
        try{
            lineItemList = mongoOperation.findAll(LineItem.class);
//            invoiceHelper.getInvoicesByCompany(companyID);
//                        Aggregation agg = new Aggregation(
//                    project("dateTime")
//                            .and(ComparisonOperators.Gte.valueOf(DateOperators.DateFromString.fromStringOf("dateTime").withFormat("%Y/%m/%d"))
//                                    .greaterThanEqualToValue(startDate))
//                            .as("matches1")
//                            .and(ComparisonOperators.Lte.valueOf(DateOperators.DateFromString.fromStringOf("dateTime").withFormat("%Y/%m/%d"))
//                                    .lessThanEqualToValue(endDate))
//                            .as("matches2"),
//                    Aggregation.match(where("matches1").is(true)),
//                    Aggregation.match(where("matches2").is(true))
//            );
//            AggregationResults<Invoice> results = mongoOperation.aggregate(agg, "persons", Invoice.class);
            List<Invoice> invoiceList =  mongoOperation.find(new Query(where("companyID").is(companyID)
//                    .and("dateTime").gt(startDateFinal).lt(endDateFinal)
                    .and("status").is(Constants.STATUS_APPROVED)), Invoice.class);
            int count = 0;
            if(invoiceList != null && !invoiceList.isEmpty())
                filterInvoiceDuration(invoiceList, startDate, endDate);
            while (lineItemList.size() != 0){
                for(Invoice invoice : invoiceList){
                    for(int k=0; k<invoice.getLineItemList().size(); k++){
                        if(lineItemList.get(count).getProductName().equals(invoice.getLineItemList().get(k).getProductName()
                        ))
                            topLineItemsList.add(lineItemList.get(count));
                    }
                }
                lineItemList.remove(count);
                count = 0;
            }
            topLineItemsList = mergeAndCompute(topLineItemsList);
        }catch(Exception ex){
            logger.info("Error in get invoices:"+ ex.getMessage());
            System.out.println("Error in get invoices:"+ ex.getMessage());
            return gson.toJson(ex.getMessage());
        }
        return gson.toJson(topLineItemsList);
    }

    //Product Sales
    public String getTopSoldProducts(String companyID){
        List<LineItem> lineItemList = null;
        List<LineItem> topLineItemsList = new ArrayList<LineItem>();
        try{
            lineItemList = mongoOperation.findAll(LineItem.class);
            List<Invoice> invoiceList = mongoOperation.find(new Query(where("companyID").is(companyID)
                    .and("status").is(Constants.STATUS_APPROVED)), Invoice.class);
            int count = 0;
            while (lineItemList.size() != 0){
                for(Invoice invoice : invoiceList){
                    for(int k=0; k<invoice.getLineItemList().size(); k++){
                        if(lineItemList.get(count).getProductId().equals(invoice.getLineItemList().get(k).getProductId()))
                            topLineItemsList.add(lineItemList.get(count));
                    }
                }
                lineItemList.remove(count);
                count = 0;
            }
           topLineItemsList = mergeAndCompute(topLineItemsList);
        }catch(Exception ex){
            logger.info("Error in get invoices:"+ ex.getMessage());
            System.out.println("Error in get invoices:"+ ex.getMessage());
            return gson.toJson(ex.getMessage());
        }

        return gson.toJson(topLineItemsList);
    }

    //Product Sales
    private List<LineItem> mergeAndCompute(List<LineItem> topLineItemsList) {
        for(int i=0; i<topLineItemsList.size(); i++) {
            for (int j = 1; j < topLineItemsList.size() - 1; j++)
                if (topLineItemsList.get(i).getId().equals(topLineItemsList.get(j).getId())) {
                    int quantity = Integer.parseInt(topLineItemsList.get(i).getQuantity()) + Integer.parseInt(topLineItemsList.get(j).getQuantity());
                    topLineItemsList.get(i).setQuantity(String.valueOf(quantity));
                    topLineItemsList.remove(j);
                }
        }
        return topLineItemsList;
    }
                                    //Product Sales end
    //yearWIse Sales

//    ToDo
//    public String getInvoicesByYear(String companyID){
//        List<TopCustomersInvoices> topCustomersInvoicesList = new ArrayList<TopCustomersInvoices> ();
//        TopCustomersInvoices topCustomersInvoices = new TopCustomersInvoices();
//        try{
//            invoiceListMain = mongoOperation.find(new Query(Criteria.where("companyID").is(companyID)
//                    .and("status").is(Constants.STATUS_APPROVED)), Invoice.class);
//            year = Calendar.getInstance().get(Calendar.YEAR);
//            while (invoiceListMain.size() != 0){
//                TopCustomersInvoices topInvoices = computeInvoicesTotal();
//                if(topInvoices != null)
//                    topCustomersInvoicesList.add(topInvoices);
//            }
//        }catch(Exception ex){
//            System.out.println("Error in get invoices:"+ ex);
//        }
//        return gson.toJson(topCustomersInvoicesList);
//    }

    //ToDo
//    private TopCustomersInvoices computeInvoicesTotal() {
//        Collections.reverse(invoiceListMain);
//        year = getYear(invoiceListMain.get(0).getInvoiceDate());
//        int sum = 0,count = 0;
//        while (count < invoiceListMain.size()){
//            int invoiceYear = getYear(invoiceListMain.get(count).getInvoiceDate());
//            if(year == invoiceYear) {
//                sum += Integer.parseInt(invoiceListMain.get(count).getTotalAmountDue());
//                invoiceListMain.remove(invoiceListMain.get(count));
//                count = 0;
//            }
//            else
//                count++;
//        }
//        TopCustomersInvoices topCustomersInvoices = new TopCustomersInvoices();
//        String yearDur = year + " - " + ++year;
//        topCustomersInvoices.setCustomerName(yearDur);
//        topCustomersInvoices.setInvoiceTotal(String.valueOf(sum));
//        return topCustomersInvoices;
//    }

            //GetTopCustomerInvoices

    public String getTopCustomerInvoices(String companyID){
        List<TopCustomersInvoices> topCustomersInvoicesList = new ArrayList<TopCustomersInvoices> ();
        try{
            List<Invoice> invoiceList = null;
            invoiceListMain = mongoOperation.find(new Query(where("companyID").is(companyID)
                    .and("status").is(Constants.STATUS_APPROVED)), Invoice.class);
            while (invoiceListMain.size() != 0){
                invoiceList = null;
                invoiceList = mongoOperation.find(new Query(where("customerName").is(invoiceListMain.get(0).getCustomerName())
                        .and("companyID").is(companyID).and("status").is(Constants.STATUS_APPROVED)), Invoice.class);
                TopCustomersInvoices topCustomersInvoices = computeInvoiceSum(invoiceList);
                if(topCustomersInvoices != null)
                    topCustomersInvoicesList.add(topCustomersInvoices);
                checkRemoveExisting(invoiceList);
            }
        }catch(Exception ex){
            System.out.println("Error in get invoices:"+ ex.getMessage());
            logger.info("Error in get invoices:"+ ex.getMessage());
            return gson.toJson(ex.getMessage());
        }
        if(topCustomersInvoicesList.size() > 1)
            overAllSum(topCustomersInvoicesList);
        return gson.toJson(topCustomersInvoicesList);
    }

    private void overAllSum(List<TopCustomersInvoices> topCustomersInvoicesList){
        double sum = 0;
        for(TopCustomersInvoices topCustomersInvoices : topCustomersInvoicesList)
            sum += Double.parseDouble(topCustomersInvoices.getInvoiceTotal());
        TopCustomersInvoices invoicesSum = new TopCustomersInvoices();
        invoicesSum.setInvoiceTotal(String.valueOf(sum));
        invoicesSum.setCustomerName("Total");
        topCustomersInvoicesList.add(invoicesSum);
    }

    private TopCustomersInvoices computeInvoiceSum(List<Invoice> invoiceList) {
        TopCustomersInvoices topCustomersInvoices = new TopCustomersInvoices();
        double sum = 0;
        for(Invoice invoice:invoiceList){
            sum += Double.parseDouble(invoice.getTotalAmountDue());
        }
        Customer customer = mongoOperation.findOne(new Query(where("customer").is(invoiceList.get(0).getCustomerName())), Customer.class);
        topCustomersInvoices.setCustomerName(customer.getCustomer());
        topCustomersInvoices.setInvoiceTotal(String.valueOf(sum));
        return topCustomersInvoices;
    }

    private void checkRemoveExisting(List<Invoice> invoiceList){
        for(Invoice invoiceToRemove : invoiceList){
            for(int i=0; i< invoiceListMain.size(); i++){
                Invoice inv = invoiceListMain.get(i);
                if(inv.getId().equals(invoiceToRemove.getId()))
                    invoiceListMain.remove(inv);

            }
        }
    }

    public String getInvoicesB2CByDuration(String startDate, String endDate, String companyID) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date startDateFinal = df.parse(startDate);
        Date endDateFinal = df.parse(endDate);
        List<InvoiceB2C> invoicesList;
        try{
            invoicesList = mongoOperation.find(new Query(where("companyID").is(companyID)
                    .and("invoiceDate").gte(startDateFinal).lte(endDateFinal)
                    .and("status").is(Constants.STATUS_APPROVED)), InvoiceB2C.class);
        } catch(Exception ex){
            logger.info("Error in get invoicesB2C:"+ ex.getMessage());
            System.out.println("Error in get invoicesB2C:"+ ex.getMessage());
            return gson.toJson(ex.getMessage());
        }
        return gson.toJson(invoicesList);
    }

     public String getInvoicesByDuration(String startDate, String endDate, String companyID) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date startDateFinal = df.parse(startDate);
        Date endDateFinal = df.parse(endDate);
        List<Invoice> invoicesList;
        try{
            invoicesList = mongoOperation.find(new Query(where("companyID").is(companyID)
                    .and("invoiceDate").gte(startDateFinal).lte(endDateFinal)
                    .and("status").is(Constants.STATUS_APPROVED)), Invoice.class);
        } catch(Exception ex){
            logger.info("Error in get invoices:"+ ex.getMessage());
            System.out.println("Error in get invoices:"+ ex.getMessage());
            return gson.toJson(ex.getMessage());
        }
        return gson.toJson(invoicesList);
    }

    public String getTopCustomerInvoicesByDates(String startDate, String endDate, String companyID) throws ParseException {
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        Date startDateFinal = df.parse(startDate);
//        Date endDateFinal = df.parse(endDate);
        List<TopCustomersInvoices> topCustomersInvoicesList = new ArrayList<TopCustomersInvoices> ();
        try{
            List<Invoice> invoiceList = null;
            invoiceListMain = mongoOperation.find(new Query(where("companyID").is(companyID)
//                    .and("invoiceDate").gte(startDateFinal).lte(endDateFinal)
                    .and("status").is(Constants.STATUS_APPROVED)), Invoice.class);
            while (invoiceListMain.size() != 0){
                invoiceList = null;
                invoiceList = mongoOperation.find(new Query(where("customerName").is(invoiceListMain.get(0).getCustomerName())
//                        .and("companyID").is(companyID).and("invoiceDate").gte(startDateFinal).lte(endDateFinal)
                        .and("status").is(Constants.STATUS_APPROVED)), Invoice.class);
                if(invoiceList != null && !invoiceList.isEmpty())
                    filterInvoiceDuration(invoiceList, startDate, endDate);
                TopCustomersInvoices topCustomersInvoices = computeInvoiceSum(invoiceList);
                if(topCustomersInvoices != null)
                    topCustomersInvoicesList.add(topCustomersInvoices);
                checkRemoveExisting(invoiceList);
            }
        }catch(Exception ex){
            logger.info("Error in get invoices:"+ ex.getMessage());
            System.out.println("Error in get invoices:"+ ex.getMessage());
            return gson.toJson(ex.getMessage());
        }
        if(topCustomersInvoicesList.size() > 1)
            overAllSum(topCustomersInvoicesList);
        return gson.toJson(topCustomersInvoicesList);
    }

    private Date addHoursToDate(Date startDateFinal, int hoursToAdd) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDateFinal);
        cal.add(Calendar.HOUR_OF_DAY, hoursToAdd);      // adds one hour
        startDateFinal = cal.getTime();
        return startDateFinal;
    }
    //GetTopCustomerInvoicesByYear End---

//    private void checkRemoveExisting(List<Invoice> invoiceList){
//        for(Invoice invoiceToRemove : invoiceList){
//            for(int i=0; i< invoiceListMain.size(); i++){
//                Invoice inv = invoiceListMain.get(i);
//                if(inv.getId().equals(invoiceToRemove.getId()))
//                    invoiceListMain.remove(inv);
//
//            }
//        }
//    }

    //yearWIse Sales End

    private int getYear(Date date){
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int year  = localDate.getYear();
        return year;
    }

    public String getTotalSalesByDate(String startDate, String endDate, String companyID) throws ParseException {
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        Date startDateFinal = df.parse(startDate);
//        Date endDateFinal = df.parse(endDate);
        TopCustomersInvoices topCustomersInvoice = new TopCustomersInvoices ();
        try{
            invoiceListMain = mongoOperation.find(new Query(where("companyID").is(companyID)
//                    .and("invoiceDate").gte(startDateFinal).lte(endDateFinal)
                    .and("status").is(Constants.STATUS_APPROVED)), Invoice.class);
                int sum = 0;
                if(invoiceListMain != null && !invoiceListMain.isEmpty())
                    filterInvoiceDuration(invoiceListMain, startDate, endDate);
                for(Invoice invoice:invoiceListMain){
                    sum += Integer.parseInt(invoice.getTotalAmountDue());
                }
                topCustomersInvoice.setInvoiceTotal(String.valueOf(sum));
                topCustomersInvoice.setCustomerName(startDate + " - " + endDate);
        }catch(Exception ex){
            logger.info("Error in get invoices:"+ ex.getMessage());
            System.out.println("Error in get invoices:"+ ex.getMessage());
        }
        return gson.toJson(topCustomersInvoice);
    }

    //get Reports by All Filters
    public String getReportFilters(String companyID, String id, String customer, String location, String startDate, String endDate){
        List<Report> reports = new ArrayList<>();
        try{
            Query query = filterCriterias(companyID, id, customer, location, startDate, endDate);
            setInvoiceReports(startDate, endDate, reports, query);
            setCreditInvoiceReports(startDate, endDate, reports, query);
            setDebitInvoiceReports(startDate, endDate, reports, query);
        }catch (Exception ex){
            logger.info("Exception in get Report Filters: "+ex.getMessage());
            return gson.toJson(ex.getMessage());
        }
        return gson.toJson(reports);
    }

    private void setInvoiceReports(String startDate, String endDate, List<Report> reports, Query query) throws ParseException {
        List<Invoice> invoiceList = mongoOperation.find(query, Invoice.class);
        if(!startDate.isEmpty() || !endDate.isEmpty())
            filterInvoiceDuration(invoiceList, startDate, endDate);
        for(Invoice invoice:invoiceList)
            reports.add(setInvoiceToReport(invoice));
    }

    private void setCreditInvoiceReports(String startDate, String endDate, List<Report> reports, Query query) throws ParseException {
        List<CreditInvoice> invoiceList = mongoOperation.find(query, CreditInvoice.class);
        if(!startDate.isEmpty() || !endDate.isEmpty())
            filterCreditInvoiceDuration(invoiceList, startDate, endDate);
        for(CreditInvoice creditInvoice:invoiceList)
            reports.add(setCreditInvoiceToReport(creditInvoice));
    }

    private void setDebitInvoiceReports(String startDate, String endDate, List<Report> reports, Query query) throws ParseException {
        List<DebitInvoice> invoiceList = mongoOperation.find(query, DebitInvoice.class);
        if(!startDate.isEmpty() || !endDate.isEmpty())
            filterDebitInvoiceDuration(invoiceList, startDate, endDate);
        for(DebitInvoice debitInvoice:invoiceList)
            reports.add(setDebitInvoiceToReport(debitInvoice));
    }

    private void filterCreditInvoiceDuration(List<CreditInvoice> creditInvoiceList, String startDate, String endDate) throws ParseException {
        if(creditInvoiceList == null && creditInvoiceList.isEmpty())
            creditInvoiceList = mongoOperation.findAll(CreditInvoice.class);
        DateFormat df = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        startDate = startDate+" 00:00:00";
        endDate = endDate+" 23:59:59";
        Date startDateFinal = df.parse(startDate);
        Date endDateFinal = df.parse(endDate);
        int count = 0;
        if(!startDate.isEmpty()){
            while(count < creditInvoiceList.size()){
                if(!df.parse(creditInvoiceList.get(count).getDateTime()).after(startDateFinal))
                    creditInvoiceList.remove(creditInvoiceList.get(count));
                else
                    count++;
            }
        }

        if(!endDate.isEmpty()){
            count = 0;
            while(count < creditInvoiceList.size()){
                if(!df.parse(creditInvoiceList.get(count).getDateTime()).before(endDateFinal))
                    creditInvoiceList.remove(creditInvoiceList.get(count));
                else
                    count++;
            }
        }

    }

    private void filterDebitInvoiceDuration(List<DebitInvoice> debitInvoiceList, String startDate, String endDate) throws ParseException {
        if(debitInvoiceList == null && debitInvoiceList.isEmpty())
            debitInvoiceList = mongoOperation.findAll(DebitInvoice.class);
        DateFormat df = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        startDate = startDate+" 00:00:00";
        endDate = endDate+" 23:59:59";
        Date startDateFinal = df.parse(startDate);
        Date endDateFinal = df.parse(endDate);
        int count = 0;
        if(!startDate.isEmpty()){
            while(count < debitInvoiceList.size()){
                if(!df.parse(debitInvoiceList.get(count).getDateTime()).after(startDateFinal))
                    debitInvoiceList.remove(debitInvoiceList.get(count));
                else
                    count++;
            }
        }

        if(!endDate.isEmpty()){
            count = 0;
            while(count < debitInvoiceList.size()){
                if(!df.parse(debitInvoiceList.get(count).getDateTime()).before(endDateFinal))
                    debitInvoiceList.remove(debitInvoiceList.get(count));
                else
                     count++;
            }
        }

    }

    private void filterInvoiceDuration(List<Invoice> invoiceList, String startDate, String endDate) throws ParseException {
        if(invoiceList == null && invoiceList.isEmpty())
            invoiceList = mongoOperation.findAll(Invoice.class);
        DateFormat df = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        startDate = startDate+" 00:00:01";
        endDate = endDate+" 23:59:59";
        Date startDateFinal = df.parse(startDate);
        Date endDateFinal = df.parse(endDate);
        int count = 0;
        if(!startDate.isEmpty()){
            while(count < invoiceList.size()){
                if(!df.parse(invoiceList.get(count).getDateTime()).after(startDateFinal))
                    invoiceList.remove(invoiceList.get(count));
                else
                count++;
            }
        }

        if(!endDate.isEmpty()){
            count = 0;
            while(count < invoiceList.size()){
                if(!df.parse(invoiceList.get(count).getDateTime()).before(endDateFinal))
                    invoiceList.remove(invoiceList.get(count));
                else
                count++;
            }
        }

    }

    private Query filterCriterias(String companyID, String id, String customer, String location,
                                  String startDate, String endDate){
        final List<Criteria> criteria = new ArrayList<>();
        if(companyID != null && !companyID.isEmpty())
            criteria.add(where("companyID").is(companyID));
        if(id != null && !id.isEmpty())
            criteria.add(where("userId").is(id));
        if(customer != null && !customer.isEmpty())
            criteria.add(where("customerName").is(customer));
        if(location != null && !location.isEmpty())
            criteria.add(where("location").is(location));
//        if(startDate != null && !startDate.isEmpty())
//            criteria.add(where("dateTime").gt(startDate));
//        if(!endDate.isEmpty() && endDate != null )
//            criteria.add(where("dateTime").lt(endDate));
        Query query = new Query(new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()])));
        return query;
    }

    //get Reports by CompanyID
    public String getReportByCompany(String companyID){
        List<Report> reports = new ArrayList<>();
        try{
            List<Invoice> invoiceList = mongoOperation.find(new Query(where("companyID").is(companyID)), Invoice.class);
            for(Invoice invoice:invoiceList)
                reports.add(setInvoiceToReport(invoice));
        }catch (Exception ex){
            logger.info(ex.getMessage());
            return gson.toJson("Exception in getReportByCompany "+ex.getMessage());
        }
        return gson.toJson(reports);
    }

    //get Reports by user's ID and location
    public String getReportByUser(String id, String location){
        List<Report> reports = new ArrayList<>();
        try{
            List<Invoice> invoiceList = mongoOperation.find(new Query(where("userId").is(id).and("location").is(location)), Invoice.class);
            for(Invoice invoice:invoiceList)
                reports.add(setInvoiceToReport(invoice));
        }catch (Exception ex){
            logger.info("Exception in getReportByUser "+ex.getMessage());
            return gson.toJson(ex.getMessage());
        }
        return gson.toJson(reports);
    }

    private Report setInvoiceToReport(Invoice invoice) {
        Report report = new Report();
        Customer customer = mongoOperation.findOne(new Query(where("customer").is(invoice.getCustomerName())), Customer.class);
        report.setCustomerName(customer.getCustomer());
        report.setCustomerVatNo(customer.getVatNumber_Customer());
        report.setInvoiceDate(invoice.getDateTime());
        report.setInvoiceNumber(invoice.getInvoiceNumber());
        report.setTotalExcludingVAT(invoice.getTotalExcludingVAT());
        //report.setTotalTaxableAmount(invoice.getTotalTaxableAmount());
        report.setTotalVat(invoice.getTotalVat());
        //report.setTotalAmountDue(invoice.getTotalAmountDue());
        report.setTotalNetAmount(invoice.getTotalNetAmount());
        report.setLocation(invoice.getLocation());
        return report;
    }

    private Report setCreditInvoiceToReport(CreditInvoice creditInvoice) {
        Report report = new Report();
        Customer customer = mongoOperation.findOne(new Query(where("customer").is(creditInvoice.getCustomerName())), Customer.class);
        report.setCustomerName(customer.getCustomer());
        report.setCustomerVatNo(customer.getVatNumber_Customer());
        report.setInvoiceDate(creditInvoice.getDateTime());
        report.setInvoiceNumber(creditInvoice.getInvoiceNumber());
        report.setTotalExcludingVAT(creditInvoice.getTotalExcludingVAT());
        //report.setTotalTaxableAmount(creditInvoice.getTotalTaxableAmount());
        report.setTotalVat(creditInvoice.getTotalVat());
        //report.setTotalAmountDue(creditInvoice.getTotalAmountDue());
        report.setTotalNetAmount(creditInvoice.getTotalNetAmount());
        report.setLocation(creditInvoice.getLocation());
        return report;
    }

    private Report setDebitInvoiceToReport(DebitInvoice debitInvoice) {
        Report report = new Report();
        Customer customer = mongoOperation.findOne(new Query(where("customer").is(debitInvoice.getCustomerName())), Customer.class);
        report.setCustomerName(customer.getCustomer());
        report.setCustomerVatNo(customer.getVatNumber_Customer());
        report.setInvoiceDate(debitInvoice.getDateTime());
        report.setInvoiceNumber(debitInvoice.getInvoiceNumber());
        report.setTotalExcludingVAT(debitInvoice.getTotalExcludingVAT());
        //report.setTotalTaxableAmount(debitInvoice.getTotalTaxableAmount());
        report.setTotalVat(debitInvoice.getTotalVat());
        //report.setTotalAmountDue(debitInvoice.getTotalAmountDue());
        report.setTotalNetAmount(debitInvoice.getTotalNetAmount());
        report.setLocation(debitInvoice.getLocation());
        return report;
    }


}
