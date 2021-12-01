package com.einvoive.helper;

import com.einvoive.constants.Constants;
import com.einvoive.model.*;
import com.einvoive.repository.JournalEntriesRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JournalEntriesHelper {
    @Autowired
    MongoOperations mongoOperation;
    @Autowired
    JournalEntriesRepository journalEntriesRepository;
    private Gson gson = new Gson();

    public void requestHanler(Invoice invoice){
        if(invoice.getInvoiceName().equalsIgnoreCase("Simplified Invoice - فاتورة مبسطة"))
            saveTaxInvoice(invoice);
        if(invoice.getInvoiceName().equalsIgnoreCase("Credit Note - اشعار دائن"))
            saveCreditNotesApproval(invoice);
        if(invoice.getInvoiceName().equalsIgnoreCase("Debit Note - بيان بالخصم"))
            saveDebitNotesApproval(invoice);
    }

    private String save(List<JournalEntries> journalEntriesList){
        ErrorCustom error = new ErrorCustom();
        String jsonError;
        try {
            journalEntriesRepository.saveAll(journalEntriesList);
            return "Journal Entries saved";
        }catch(Exception ex){
            error.setErrorStatus("Error");
            error.setError(ex.getMessage());
            jsonError = gson.toJson(error);
            return jsonError;
        }
    }

    public String getByInvoiceNo(String invoiceNo){
        List<JournalEntries> journalEntriesList = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("invoiceNo").is(invoiceNo));
            journalEntriesList = mongoOperation.find(query, JournalEntries.class);
        }catch(Exception ex){
            System.out.println("Error in getByInvoiceNo :"+ ex);
            return gson.toJson("Error in getByInvoiceNo :"+ ex);
        }
        return gson.toJson(journalEntriesList);
    }

    public String update(List<JournalEntries> journalEntriesList){
        try{
            journalEntriesRepository.saveAll(journalEntriesList);
            return gson.toJson("Journal Entries Updated");
            }catch(Exception ex){
            System.out.println("Error in updateJournalEntries :"+ ex);
            return gson.toJson("Error in updateJournalEntries :"+ ex);
        }
    }

    //1-TAXInvoice on Approval
    private void saveTaxInvoice(Invoice invoice) {
        List<JournalEntries> journalEntriesList = new ArrayList<>();
        int lineItemsSize = invoice.getLineItemList().size();
        //Debit Values
        for(int i=1; i<=3; i++){
            JournalEntries journalEntries = new JournalEntries();
            setToSave(invoice, journalEntries);
            if(i <= 3) {
                journalEntries.setDebit(getDebitValue(invoice, i));
                journalEntries.setChartOfAccount(getChartOfAccountName(i));
            }
            journalEntriesList.add(journalEntries);
        }
        //Credit Values
        //Revenue
        for(int i=1; i<=lineItemsSize; i++){
            JournalEntries journalEntries = new JournalEntries();
            setToSave(invoice, journalEntries);
            setRevenueEntries(invoice.getLineItemList(), journalEntries);
            journalEntriesList.add(journalEntries);
        }
        //false to add Vat values in credit
        journalEntriesList.addAll(setVATEntries(invoice, false));
        //Save List of Journal Entries
        save(journalEntriesList);
    }

    //2-At the time of receipt of TAX INVOICE AMOUNT
    public void saveTaxInvoiceAmount(Invoice invoice) {
        List<JournalEntries> journalEntriesList = new ArrayList<>();
        for(int i=1; i<=2; i++){
            JournalEntries journalEntries = new JournalEntries();
            setToSave(invoice, journalEntries);
            journalEntriesList.add(journalEntries);
        }
        //adding accountChart and Credit/Debit
        journalEntriesList.get(0).setChartOfAccount("Bank Account");
        journalEntriesList.get(1).setChartOfAccount("Account Receivable");
        journalEntriesList.get(0).setDebit(invoice.getTotalNetAmount());
        journalEntriesList.get(1).setCredit(invoice.getTotalNetAmount());
        //Save List of Journal Entries
        save(journalEntriesList);
    }

    private void setRevenueEntries(List<LineItem> lineItemList, JournalEntries journalEntries) {
        for(LineItem lineItem: lineItemList){
            journalEntries.setCredit(String.valueOf(Integer.parseInt(lineItem.getPrice())*Integer.parseInt(lineItem.getQuantity())));
            ProductMain productMain = mongoOperation.findOne(new Query(Criteria.where("id").is(lineItem.getProductId())), ProductMain.class);
            journalEntries.setChartOfAccount(productMain.getAssignedChartofAccounts());
        }
    }
    //setVAT values
    private List<JournalEntries> setVATEntries(Invoice invoice, boolean flag) {
        List<JournalEntries> journalEntriesList = new ArrayList<>();
        for(LineItem lineItem: invoice.getLineItemList()){
            JournalEntries journalEntries = new JournalEntries();
            setToSave(invoice, journalEntries);
            //credit on false, else debit
            if(flag)
                journalEntries.setDebit(lineItem.getTaxAmount());
            else
                journalEntries.setCredit(lineItem.getTaxAmount());
            ProductMain productMain = mongoOperation.findOne(new Query(Criteria.where("id").is(lineItem.getProductId())), ProductMain.class);
            journalEntries.setChartOfAccount(Constants.VAT_PAYABLE);
            journalEntriesList.add(journalEntries);
        }
        return journalEntriesList;
    }

    private JournalEntries setToSave(Invoice invoice, JournalEntries journalEntries){
        journalEntries.setEntryState(Constants.TAX_INVOICE);
        journalEntries.setEntryDate(invoice.getInvoiceDate());
        journalEntries.setCustomerName(invoice.getCustomerName());
        journalEntries.setInvoiceNo(invoice.getInvoiceNumber());
        return journalEntries;
    }

    //4-Debit Notes on Approval
    private void saveDebitNotesApproval(Invoice invoice) {
        List<JournalEntries> journalEntriesList = new ArrayList<>();
        int lineItemsSize = invoice.getLineItemList().size();
        //Debit Values
        for(int i=1; i<=2; i++){
            JournalEntries journalEntries = new JournalEntries();
            setToSave(invoice, journalEntries);
            //needs to be discussed
            journalEntries.setDebit(getDebitValue(invoice, i));
            journalEntries.setChartOfAccount(getChartOfAccountName(i));
            journalEntriesList.add(journalEntries);
        }
        //Credit Values
        //Revenue
        for(int i=1; i<=lineItemsSize; i++){
            JournalEntries journalEntries = new JournalEntries();
            setToSave(invoice, journalEntries);
            setRevenueEntries(invoice.getLineItemList(), journalEntries);
            journalEntriesList.add(journalEntries);
        }
        //VAT, false present credit
        journalEntriesList.addAll(setVATEntries(invoice, false));
        //Save List of Journal Entries
        save(journalEntriesList);
    }

    //6-Credit Notes on Approval
    private void saveCreditNotesApproval(Invoice invoice) {
        List<JournalEntries> journalEntriesList = new ArrayList<>();
        int lineItemsSize = invoice.getLineItemList().size();
        //Debit Values
        for(int i=1; i<=2; i++){
            JournalEntries journalEntries = new JournalEntries();
            setToSave(invoice, journalEntries);
            journalEntries.setCredit(getCreditValue(invoice, i));
            journalEntries.setChartOfAccount(getChartOfAccountName(i));
            journalEntriesList.add(journalEntries);
        }
        //Credit Values
        //Revenue
        for(int i=1; i<=lineItemsSize; i++){
            JournalEntries journalEntries = new JournalEntries();
            setToSave(invoice, journalEntries);
            setRevenueEntries(invoice.getLineItemList(), journalEntries);
            journalEntriesList.add(journalEntries);
        }
        //VAT, false present credit
        journalEntriesList.addAll(setVATEntries(invoice, true));
        //Save List of Journal Entries
        save(journalEntriesList);
    }

    private String getChartOfAccountName(int value){
        switch (value) {
            case 1:
                return "Account Receivable";
            case 2:
                return "Discount Allowed";
            case 3:
                return "Retention Account";
        }
        return null;
    }

    private String getDebitValue(Invoice invoice, int index){
        switch (index) {
            case 1:
                return invoice.getTotalNetAmount();
//            case 2:
//                return invoice.getTotal();
            case 2:
                return invoice.getDiscount();
//            case 4:
//                return invoice.getTotalVat();
            case 3:
                return invoice.getRetention();
        }
        return "0";
    }

    private String getCreditValue(Invoice invoice, int index){
        switch (index) {
            case 1:
                return invoice.getTotalAmountDue();
            case 2:
                return invoice.getDiscount();
        }
        return "0";
    }

}
