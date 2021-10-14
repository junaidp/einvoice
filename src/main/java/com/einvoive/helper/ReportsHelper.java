package com.einvoive.helper;

import com.einvoive.model.Customer;
import com.einvoive.model.Invoice;
import com.einvoive.model.LineItem;
import com.einvoive.model.TopCustomersInvoices;
import com.einvoive.repository.LineItemRepository;
import com.google.gson.Gson;
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

@Component
public class ReportsHelper {
    @Autowired
    LineItemRepository repository;
    @Autowired
    InvoiceHelper invoiceHelper;
    @Autowired
    MongoOperations mongoOperation;
    Gson gson = new Gson();
    private List<Invoice> invoiceListMain;
    private int year;

    //Product Sales
    public String getTopSoldProducts(String companyID){
        List<LineItem> lineItemList = null;
        List<LineItem> topLineItemsList = new ArrayList<LineItem>();
        try{
            lineItemList = mongoOperation.findAll(LineItem.class);
            invoiceHelper.getInvoicesByCompany(companyID);
            List<Invoice> invoiceList = invoiceHelper.getInvoices();
            int count = 0;
            while (lineItemList.size() != 0){
               for(Invoice invoice : invoiceList){
                    for(int k=0; k<invoice.getLineItemList().size(); k++){
                        if(lineItemList.get(count).getId().equals(invoice.getLineItemList().get(k).getId()))
                           topLineItemsList.add(lineItemList.get(count));
                        }
                    }
                count++;
                }
           topLineItemsList = mergeAndCompute(topLineItemsList);
        }catch(Exception ex){
            System.out.println("Error in get invoices:"+ ex);
        }
        return gson.toJson(topLineItemsList);
    }

    //Product Sales
    private List<LineItem> mergeAndCompute(List<LineItem> topLineItemsList) {
        for(int i=0; i<topLineItemsList.size(); i++)
            for(int j=1; j<topLineItemsList.size()-1; j++)
                if(topLineItemsList.get(i).getId() == topLineItemsList.get(j).getId()){
                    int quantity = Integer.parseInt(topLineItemsList.get(i).getQuantity()) + Integer.parseInt(topLineItemsList.get(j).getQuantity());
                    topLineItemsList.get(i).setQuantity(String.valueOf(quantity));
                    topLineItemsList.remove(j);
                }
        return topLineItemsList;
    }
                                    //Product Sales end

    //yearWIse Sales

    public String getInvoicesByYear(String companyID){
        List<TopCustomersInvoices> topCustomersInvoicesList = new ArrayList<TopCustomersInvoices> ();
        TopCustomersInvoices topCustomersInvoices = new TopCustomersInvoices();
        try{
            invoiceListMain = mongoOperation.find(new Query(Criteria.where("companyID").is(companyID)), Invoice.class);
            year = Calendar.getInstance().get(Calendar.YEAR);
            while (invoiceListMain.size() != 0){
                TopCustomersInvoices topInvoices = computeInvoicesTotal();
                if(topInvoices != null)
                    topCustomersInvoicesList.add(topInvoices);
            }
        }catch(Exception ex){
            System.out.println("Error in get invoices:"+ ex);
        }
        return gson.toJson(topCustomersInvoicesList);
    }

    private TopCustomersInvoices computeInvoicesTotal() {
        Collections.reverse(invoiceListMain);
        year = getYear(invoiceListMain.get(0).getInvoiceDate());
        int sum = 0,count = 0;
        while (count < invoiceListMain.size()){
            int invoiceYear = getYear(invoiceListMain.get(count).getInvoiceDate());
            if(year == invoiceYear) {
                sum += Integer.parseInt(invoiceListMain.get(count).getTotalAmountDue());
                invoiceListMain.remove(invoiceListMain.get(count));
                count = 0;
            }
            else
                count++;
        }
        TopCustomersInvoices topCustomersInvoices = new TopCustomersInvoices();
        String yearDur = year + " - " + ++year;
        topCustomersInvoices.setCustomerName(yearDur);
        topCustomersInvoices.setInvoiceTotal(String.valueOf(sum));
        return topCustomersInvoices;
    }

            //GetTopCustomerInvoices

    public String getTopCustomerInvoices(String companyID){
        List<TopCustomersInvoices> topCustomersInvoicesList = new ArrayList<TopCustomersInvoices> ();
        try{
            List<Invoice> invoiceList = null;
            invoiceListMain = mongoOperation.find(new Query(Criteria.where("companyID").is(companyID)), Invoice.class);
            while (invoiceListMain.size() != 0){
                invoiceList = null;
                invoiceList = mongoOperation.find(new Query(Criteria.where("customerID").is(invoiceListMain.get(0).getCustomerID())
                        .and("companyID").is(companyID)), Invoice.class);
                TopCustomersInvoices topCustomersInvoices = computeInvoiceSum(invoiceList);
                if(topCustomersInvoices != null)
                    topCustomersInvoicesList.add(topCustomersInvoices);
                checkRemoveExisting(invoiceList);
            }
        }catch(Exception ex){
            System.out.println("Error in get invoices:"+ ex);
        }
        return gson.toJson(topCustomersInvoicesList);
    }

    private TopCustomersInvoices computeInvoiceSum(List<Invoice> invoiceList) {
        TopCustomersInvoices topCustomersInvoices = new TopCustomersInvoices();
        int sum = 0;
        for(Invoice invoice:invoiceList){
            sum += Integer.parseInt(invoice.getTotalAmountDue());
        }
        Customer customer = mongoOperation.findOne(new Query(Criteria.where("_id").is(invoiceList.get(0).getCustomerID())), Customer.class);
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

    public String getTopCustomerInvoicesByDates(String startDate, String endDate, String companyID) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date startDateFinal = df.parse(startDate);
        Date endDateFinal = df.parse(endDate);

        List<TopCustomersInvoices> topCustomersInvoicesList = new ArrayList<TopCustomersInvoices> ();
        try{
            List<Invoice> invoiceList = null;
            invoiceListMain = mongoOperation.find(new Query(Criteria.where("companyID").is(companyID)
                    .and("invoiceDate").gte(startDateFinal).lte(endDateFinal)), Invoice.class);
            while (invoiceListMain.size() != 0){
                invoiceList = null;
                invoiceList = mongoOperation.find(new Query(Criteria.where("customerID").is(invoiceListMain.get(0).getCustomerID())
                        .and("companyID").is(companyID).and("invoiceDate").gte(startDateFinal).lte(endDateFinal)), Invoice.class);
                TopCustomersInvoices topCustomersInvoices = computeInvoiceSum(invoiceList);
                if(topCustomersInvoices != null)
                    topCustomersInvoicesList.add(topCustomersInvoices);
                checkRemoveExisting(invoiceList);
            }
        }catch(Exception ex){
            System.out.println("Error in get invoices:"+ ex);
        }
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

}
