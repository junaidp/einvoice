package com.einvoive.controller;

import com.einvoive.constants.*;
import com.einvoive.helper.*;
import com.einvoive.model.*;
import com.einvoive.util.Translator;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RequestMapping("control")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class MainController {

    @Autowired
    UserHelper userHelper;

    @Autowired
    LoginHelper loginHelper;

    @Autowired
    UpdatePasswordHelper updatePasswordHelper;

    @Autowired
    PaymentCardHelper paymentCardHelper;

    @Autowired
    CompanyHelper companyHelper;

    @Autowired
    CustomerHelper customerHelper;

    @Autowired
    LineItemHelper lineItemHelper;

    @Autowired
    LineItemB2CHelper lineItemB2CHelper;

    @Autowired
    AccountsHelper accountsHelper;

    @Autowired
    RecordPaymentHelper recordPaymentHelper;

    @Autowired
    ReportsHelper reportsHelper;

    @Autowired
    TranslationHelper translationHelper;

    @Autowired
    ProductMainHelper productMainHelper;

    @Autowired
    InvoiceHelper invoiceHelper;

    @Autowired
    InvoiceB2CHelper invoiceB2CHelper;

    @Autowired
    VatHelper vatHelper;

    @Autowired
    LocationHelper locationHelper;

    @Autowired
    SettingsHelper settingsHelper;

    @Autowired
    BankAccountHelper bankAccountHelper;

//    @Autowired
//    JournalEntriesHelper journalEntriesHelper;

    private Gson gson = new Gson();

//    @PostMapping("/saveJournalEntries")
//    public String saveJournalEntries(@RequestBody List<JournalEntries> journalEntriesList) {
//        return journalEntriesHelper.save(journalEntriesList);
//    }

    @PostMapping("/saveSettings")
    public String saveSettings(@RequestBody Settings settings) {
        return settingsHelper.save(settings);
    }

    @PostMapping("/saveUser")
    public String saveUser(@RequestBody User userEntity) {
        return userHelper.saveUser(userEntity);
    }
    @PostMapping("/saveBank")
    public String saveBank(@RequestBody BankAccount bankAccountSave) {
        return bankAccountHelper.saveBank(bankAccountSave);
    }


    @PostMapping("/saveCompany")
    public String saveCompany(@RequestBody Company companyEnglish) { return companyHelper.saveCompany(companyEnglish);
    }

    @PostMapping("/saveCustomer")
    public String saveCustomer(@RequestBody List<Customer> customer) {
        return customerHelper.save(customer.get(0), customer.get(1));
    }

//    @PostMapping(value = "/saveInvoice", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
//    public String saveInvoice(@RequestParam("file") MultipartFile file, @RequestPart("invoice") String invoiceJson) {
//        Invoice invoice = gson.fromJson(invoiceJson, Invoice.class);
//        return invoiceHelper.save(invoice, file);
//    }

    @PostMapping(value = "/saveInvoice")
    public String saveInvoice(@RequestBody Invoice invoice)  {
        return invoiceHelper.save(invoice);
    }

    @PostMapping("/saveInvoiceB2C")
    public String saveInvoiceB2C(@RequestBody InvoiceB2C invoice) {
        return invoiceB2CHelper.save(invoice);
    }

    @PostMapping("/saveLineItem")
    public String saveLineItem(@RequestBody LineItem lineItem) {
        return lineItemHelper.save(lineItem);
    }

    @PostMapping("/saveLineItemB2C")
    public String saveLineItemB2C(@RequestBody LineItemB2C lineItem) {
        return lineItemB2CHelper.save(lineItem);
    }

    @PostMapping("/saveAccounts")
    public String saveAccounts(@RequestBody Accounts accounts) {
        return accountsHelper.save(accounts);
    }

    @PostMapping("/saveRecordPayment")
    public String saveRecordPayment(@RequestBody RecordPayment recordPayment) {
        return recordPaymentHelper.save(recordPayment);
    }

    @PostMapping("/savePaymentCard")
    public String savePaymentCard(@RequestBody PaymentCard paymentCard) {
        return paymentCardHelper.savePaymentCard(paymentCard);
    }

    @PostMapping("/saveProductMain")
    public String saveProductMain(@RequestBody List<ProductMain> products) {
        return productMainHelper.save(products.get(0), products.get(1));
    }

    @PostMapping("/saveVat")
    public String saveVat(@RequestBody Vat vat) {
        return vatHelper.save(vat);
    }

    @PostMapping("/saveLocation")
    public String saveLocation(@RequestBody Location location) {
        return locationHelper.save(location);
    }

    @PostMapping("/saveTranslation")
    public String saveTranslation(@RequestBody Translation translation) {
        return translationHelper.saveTranslation(translation);
    }

    //update

//    @PostMapping("/updateJournalEntries")
//    public String updateJournalEntries(@RequestBody List<JournalEntries> journalEntriesList) {
//        return journalEntriesHelper.update(journalEntriesList);
//    }

    @PostMapping("/updateTranslation")
    public String updateTranslation(@RequestBody Translation translation) {
        return translationHelper.updateTranslation(translation);
    }

    @PostMapping("/updateBankAccount")
    public String updateBankAccount(@RequestBody BankAccount bankAccount) {
        return bankAccountHelper.updateBankAccount(bankAccount);
    }

    @PostMapping("/updateUser")
    public String updateUser(@RequestBody User user) {
        return userHelper.updateUser(user);
    }

    @PostMapping("/updatePaymentCard")
    public String updatePaymentCard(@RequestBody PaymentCard paymentCard) {
        return paymentCardHelper.updatePaymentCard(paymentCard);
    }

    @PostMapping("/updateCompany")
    public String updateCompany(@RequestBody List<Company> companyList) {
        return companyHelper.updateCompany(companyList.get(0), companyList.get(1));
    }

    @PostMapping("/updateCustomer")
    public String updateCustomer(@RequestBody List<Customer> customerList) {
        return customerHelper.update(customerList.get(0), customerList.get(1));
    }

    @PostMapping("/updateInvoice")
    public String updateInvoice(@RequestBody InvoiceWithFile invoiceWithFile) {
        return null; // TODO: invoiceHelper.update(invoiceWithFile);
    }

    @PostMapping("/updateInvoiceB2C")
    public String updateInvoiceB2C(@RequestBody InvoiceB2C invoice) {
        return invoiceB2CHelper.update(invoice);
    }

    @PostMapping("/updateProduct")
    public String updateProduct(@RequestBody LineItem lineItem) {
        return lineItemHelper.update(lineItem);
    }

    @PostMapping("/updateLineItemB2C")
    public String updateLineItemB2C(@RequestBody LineItemB2C lineItem) {
        return lineItemB2CHelper.update(lineItem);
    }

    @PostMapping("/updateAccounts")
    public String updateAccounts(@RequestBody Accounts accounts) {
        return accountsHelper.update(accounts);
    }

    @PostMapping("/updateRecordPayment")
    public String updateRecordPayment(@RequestBody RecordPayment recordPayment) {
        return recordPaymentHelper.update(recordPayment);
    }

    @PostMapping("/updateProductMain")
    public String updateProductMain(@RequestBody List<ProductMain> products) {
        return productMainHelper.update(products.get(0), products.get(1));
    }

    @PostMapping("/updateVat")
    public String updateVat(@RequestBody Vat vat) {
        return vatHelper.update(vat);
    }

    @PostMapping("/updateLocation")
    public String updateLocation(@RequestBody Location location) {
        return locationHelper.update(location);
    }

    //delete
    @GetMapping("/deleteCustomers")
    public String deleteCustomers(@RequestParam String customerID) {
        return customerHelper.deleteCustomers(customerID);
    }

    @GetMapping("/deleteInvoices")
    public String deleteInvoices(@RequestParam String inoviceID) {
        return invoiceHelper.deleteInvoice(inoviceID);
    }

    @GetMapping("/deleteInvoicesB2C")
    public String deleteInvoicesB2C(@RequestParam String inoviceID) {
        return invoiceB2CHelper.deleteInvoice(inoviceID);
    }

    @GetMapping("/deleteUser")
    public String deleteUser(@RequestParam String id){
        return userHelper.deleteUser(id);
    }
    @GetMapping("/deleteBankAccounts")
    public String deleteBankAccounts(@RequestParam String id){
        return bankAccountHelper.deleteBankAccounts(id);
    }

    @GetMapping("/deleteProduct")
    public String deleteProduct(@RequestParam String productID) {
        return productMainHelper.deleteProduct(productID);
    }

    @GetMapping("/deleteLineItem")
    public String deleteLineItem(@RequestParam String id) {
        return lineItemHelper.deleteLineItem(id);
    }

    @GetMapping("/deleteLineItemB2C")
    public String deleteLineItemB2C(@RequestParam String id) {
        return lineItemB2CHelper.deleteLineItem(id);
    }

    @GetMapping("/deleteAccount")
    public String deleteAccount(@RequestParam String id) {
        return accountsHelper.deleteAccount(id);
    }

    @GetMapping("/deleteVats")
    public String deleteVats(@RequestParam String id) {
        return vatHelper.deleteVAT(id);
    }

    @GetMapping("/deleteLocations")
    public String gdeleteLocations(@RequestParam String id) {
        return locationHelper.deleteLocation(id);
    }

    //get

    @GetMapping("/getAllUsers")
    public String getAllUsers(@RequestParam String companyID) {
        return userHelper.getAllUsers(companyID);
    }

    @GetMapping("/getBankaccounts")
    public String getBankaccounts(@RequestParam String companyID) {
        return bankAccountHelper.getBankaccounts(companyID);
    }

    @GetMapping("/getBankaccountsByID")
    public String getBankaccountsByID(@RequestParam String id) {
        return bankAccountHelper.getBankaccountsByID(id);
    }

    @GetMapping("/getRecordPaymeny")
    public String getRecordPaymeny(@RequestParam String invoiceNo) {
        return recordPaymentHelper.getRecordPaymenyByInvoiceNo(invoiceNo);
    }

    @GetMapping("/getAvailableInvoiceNo")
    public String getAvailableInvoiceNo(@RequestParam String companyID) {
        return gson.toJson(invoiceHelper.getNextInvoiceNumber(companyID));
    }

    @GetMapping("/getAvailableInvoiceB2CNo")
    public String getAvailableInvoiceB2CNo(@RequestParam String companyID) {
        return gson.toJson(invoiceB2CHelper.getNextInvoiceNumber(companyID));
    }

    @GetMapping("/getTopSaledProducts")
    public String getTopSaledProducts(@RequestParam String companyID) {
        return productMainHelper.getTopSaledProducts(companyID);
    }

    @GetMapping("/getPaymentCard")
    public String getPaymentCard(@RequestParam String userId) {
        return paymentCardHelper.getPaymentCards(userId);
    }

    @GetMapping("/getCompany")
    public String getCompany(@RequestParam String companyID) {
        return companyHelper.getCompany(companyID);
    }

    @GetMapping("/getAllCustomers")
    public String getAllCustomers(@RequestParam String companyID) {
        return customerHelper.getAllCustomers(companyID);
    }

    @GetMapping("/getCustomer")
    public String getCustomer(@RequestParam String customerID) {
        return customerHelper.getCustomer
                (customerID);}

    @GetMapping("/getInvoicesByID")
    public String getInvoicesByID(@RequestParam String id) { return invoiceHelper.getInvoicesByID(id); }

    @GetMapping("/getInvoicesB2CByID")
    public String getInvoicesB2CByID(@RequestParam String id) { return invoiceB2CHelper.getInvoicesByID(id); }

    @GetMapping("/getTopCustomerInvoices")
    public String getTopCustomerInvoices(@RequestParam String companyID)  {
        return reportsHelper.getTopCustomerInvoices(companyID);
    }

    @GetMapping("/getTopSoldProducts")
    public String getTopSoldProducts(@RequestParam String companyID)  {
        return reportsHelper.getTopSoldProducts(companyID);
    }

    @GetMapping("/getTopCustomerInvoicesByDates")
    public String getTopCustomerInvoicesByDates(@RequestParam String startDate, String endDate, String companyID) throws ParseException {
        return reportsHelper.getTopCustomerInvoicesByDates(startDate, endDate, companyID);
    }

    @GetMapping("/getTopSoldProductsByDate")
    public String getTopSoldProductsByDate(@RequestParam String startDate, String endDate, String companyID) throws ParseException {
        return reportsHelper.getTopSoldProductsByDate(startDate, endDate, companyID);
    }

    @GetMapping("/getTotalSalesByDate")
    public String getTotalSalesByDate(@RequestParam String startDate, String endDate, String companyID) throws ParseException {
        return reportsHelper.getTotalSalesByDate(startDate, endDate, companyID);
    }

    @GetMapping("/getInvoicesByYear")
    public String getInvoicesByYear(@RequestParam String companyID) {
        return reportsHelper.getInvoicesByYear(companyID);
    }

    @GetMapping("/getInvoicesByCustomer")
    public String getInvoicesByCustomer(@RequestParam String customerName) { return invoiceHelper.getInvoicesByCustomer(customerName); }

    @GetMapping("/getInvoicesByInvoiceNo")
    public String getInvoicesByInvoiceNo(@RequestParam String invoiceNo) { return invoiceHelper.getInvoicesByInvoiceNo(invoiceNo); }

    @GetMapping("/getInvoicesB2CByInvoiceNo")
    public String getInvoicesB2CByInvoiceNo(@RequestParam String invoiceNo) { return invoiceB2CHelper.getInvoicesByInvoiceNo(invoiceNo); }

    @GetMapping("/getInvoicesByDuration")
    public String getInvoicesByDuration(@RequestParam String startDate, String endDate, String companyID) throws ParseException {
        return reportsHelper.getInvoicesByDuration(startDate, endDate, companyID);}

    @GetMapping("/getInvoicesB2CByDuration")
    public String getInvoicesB2CByDuration(@RequestParam String startDate, String endDate, String companyID) throws ParseException {
        return reportsHelper.getInvoicesB2CByDuration(startDate, endDate, companyID);}

    @GetMapping("/getInvoicesByStatus")
    public String getInvoicesByStatus(@RequestParam String status) { return invoiceHelper.getInvoicesByStatus(status); }

    @GetMapping("/getInvoicesByCompany")
    public String getInvoicesByCompany(@RequestParam String companyID) { return invoiceHelper.getInvoicesByCompany(companyID); }

    @GetMapping("/getInvoicesB2CByCompany")
    public String getInvoicesB2CByCompany(@RequestParam String companyID) { return invoiceB2CHelper.getInvoicesByCompany(companyID); }

    @GetMapping("/getInvoiceStatus")
    public String getInvoiceStatus(@RequestParam String id) {
        return invoiceHelper.getInvoiceStatus(id);
    }

    @PostMapping("/setInvoiceStatus")
    public String setInvoiceStatus(@RequestParam String id, String status) {
        return invoiceHelper.setInvoiceStatus(id, status);
    }

    @PostMapping("/setInvoiceStatusByInvoiceID")
    public String setInvoiceStatusByInvoiceID(@RequestBody Invoice invoice) {
        return invoiceHelper.setInvoiceStatusByInvoiceID(invoice.getInvoiceNumber(), invoice.getStatus());
    }

    @PostMapping("/signIn")
    public String signIn(@RequestBody Login login) { return loginHelper.signIn(login); }

    @PostMapping("/changePassword")
    public String testuser(@RequestBody UpdatePassword updatePassword) { return updatePasswordHelper.changePassword(updatePassword); }

//    @PostMapping("/signInUser")
//    public String signInUser(@RequestBody User user){
//        return userHelper.signInUser(user);
//    }
//
//    @PostMapping("/signInCompany")
//    public String signInCompany(@RequestBody Company company){
//        return companyHelper.singIn(company);
//    }

    @GetMapping("/getLineItems")
    public String getLineItems(@RequestParam String invoiceId) {
        return lineItemHelper.getLineItems(invoiceId);
    }

    @GetMapping("/getLineItemsB2C")
    public String getLineItemsB2C(@RequestParam String invoiceId) {
        return lineItemB2CHelper.getLineItemsB2C(invoiceId);
    }

    @GetMapping("/getProducts")
    public String getProducts(@RequestParam String companyId) {
        return productMainHelper.getProducts(companyId);
    }

    @GetMapping("/getAccounts")
    public String getAccounts(@RequestParam String companyID) {
        return accountsHelper.getAccounts(companyID);
    }

    @GetMapping("/getAllVats")
    public String getAllVats(@RequestParam String companyID) {
        return vatHelper.getAllVats(companyID);
    }

    @GetMapping("/getAllLocations")
    public String getAllLocations(@RequestParam String companyID) {
        return locationHelper.getAllLocations(companyID);
    }

    @GetMapping("/getTranslation")
    public String getTranslation(@RequestParam String english) {
        return translationHelper.getTranslation(english);
    }

    @GetMapping("/getTranslationFromApi")
    public String getTranslationFromApi(@RequestParam String english) {
        String arabic = gson.toJson(Translator.getTranslation(english));
        return arabic;
    }

    @GetMapping("/getAccountReceivableConstant")
    public String getAccountReceivableConstant(){
       return gson.toJson(new AccountReceivableConstant());
    }

    @GetMapping("/getBankChargesConstant")
    public String getBankChargesConstant(){
        return gson.toJson(new BankChargesConstant());
    }

    @GetMapping("/getDiscountConstant")
    public String getDiscountConstant(){
        return gson.toJson(new DiscountConstant());
    }

    @GetMapping("/getRetentionReceivableConstant")
    public String getRetentionReceivableConstant(){
        return gson.toJson(new RetentionReceivableConstant());
    }

    @GetMapping("/getTaxDeductionConstant")
    public String getTaxDeductionConstant(){
        return gson.toJson(new TaxDeductionConstant());
    }

    @GetMapping("/getVATPayableConstant")
    public String getVATPayableConstant(){
        return gson.toJson(new VATPayableConstant());
    }



//    @GetMapping("/getJournalEntries")
//    public String getJournalEntries(String invoiceNo){
//        return journalEntriesHelper.getByInvoiceNo(invoiceNo);
//    }

}
