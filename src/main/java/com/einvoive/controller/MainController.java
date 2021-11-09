package com.einvoive.controller;

import com.einvoive.helper.*;
import com.einvoive.model.*;
import com.einvoive.util.Translator;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    PaymentCardHelper paymentCardHelper;

    @Autowired
    CompanyHelper companyHelper;

    @Autowired
    CustomerHelper customerHelper;

    @Autowired
    LineItemHelper lineItemHelper;

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
    VatHelper vatHelper;

    @Autowired
    LocationHelper locationHelper;

    @PostMapping("/saveUser")
    public String saveUser(@RequestBody User userEntity) {
        return userHelper.saveUser(userEntity);
    }

    @PostMapping("/saveCompany")
    public String saveCompany(@RequestBody Company companyEnglish) { return companyHelper.saveCompany(companyEnglish);
    }

    @PostMapping("/saveCustomer")
    public String saveCustomer(@RequestBody List<Customer> customer) {
        return customerHelper.save(customer.get(0), customer.get(1));
    }

    @PostMapping("/saveInvoice")
    public String saveInvoice(@RequestBody Invoice invoice) {
        return invoiceHelper.save(invoice);
    }

    @PostMapping("/saveLineItem")
    public String saveLineItem(@RequestBody LineItem lineItem) {
        return lineItemHelper.save(lineItem);
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
    @PostMapping("/updateTranslation")
    public String updateTranslation(@RequestBody Translation translation) {
        return translationHelper.updateTranslation(translation);
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
    public String updateInvoice(@RequestBody Invoice invoice) {
        return invoiceHelper.update(invoice);
    }

    @PostMapping("/updateProduct")
    public String updateProduct(@RequestBody LineItem lineItem) {
        return lineItemHelper.update(lineItem);
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

    @GetMapping("/deleteUser")
    public String deleteUser(@RequestParam String id){
        return userHelper.deleteUser(id);
    }

    @GetMapping("/deleteProduct")
    public String deleteProduct(@RequestParam String productID) {
        return productMainHelper.deleteProduct(productID);
    }

    @GetMapping("/deleteLineItem")
    public String deleteLineItem(@RequestParam String id) {
        return lineItemHelper.deleteLineItem(id);
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

    @GetMapping("/getRecordPaymeny")
    public String getRecordPaymeny(@RequestParam String invoiceNo) {
        return recordPaymentHelper.getRecordPaymenyByInvoiceNo(invoiceNo);
    }

    @GetMapping("/getAvailableInvoiceNo")
    public String getAvailableInvoiceNo(@RequestParam String companyID) {
        String invpiceNo = new Gson().toJson(invoiceHelper.getNextInvoiceNumber(companyID));
        return invpiceNo;
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
        return customerHelper.getCustomer(customerID);}

    @GetMapping("/getInvoicesByID")
    public String getInvoicesByID(@RequestParam String id) { return invoiceHelper.getInvoicesByID(id); }

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

    @GetMapping("/getInvoicesByDuration")
    public String getInvoicesByDuration(@RequestParam String startDate, String endDate, String companyID) throws ParseException {
        return reportsHelper.getInvoicesByDuration(startDate, endDate, companyID);}

    @GetMapping("/getInvoicesByStatus")
    public String getInvoicesByStatus(@RequestParam String status) { return invoiceHelper.getInvoicesByStatus(status); }

    @GetMapping("/getInvoicesByCompany")
    public String getInvoicesByCompany(@RequestParam String companyID) { return invoiceHelper.getInvoicesByCompany(companyID); }

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
    public String signIn(@RequestBody Login login){ return loginHelper.signIn(login); }

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
        String arabic = new Gson().toJson(Translator.getTranslation(english));
        return arabic;
    }

}
