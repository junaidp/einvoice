package com.einvoive.controller;

import com.einvoive.authenticator.ValidateCodeDto;
import com.einvoive.authenticator.Validation;
import com.einvoive.constants.*;
import com.einvoive.helper.*;
import com.einvoive.model.*;
import com.einvoive.util.Translator;
import com.einvoive.util.Utility;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("control")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class MainController {

    @Autowired
    UserHelper userHelper;

    @Autowired
    LoginHelper loginHelper;

    @Autowired
    ContractHelper contractHelper;

    @Autowired
    ContractItemHelper contractItemHelper;

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
    CreditInvoiceHelper creditInvoiceHelper;

    @Autowired
    DebitInvoiceHelper debitInvoiceHelper;

    @Autowired
    InvoiceB2CHelper invoiceB2CHelper;

    @Autowired
    VatHelper vatHelper;

    @Autowired
    LocationHelper locationHelper;

    @Autowired
    SettingsHelper settingsHelper;

    @Autowired
    AllInvoicesHelper allInvoicesHelper;

    @Autowired
    BankAccountHelper bankAccountHelper;

//    @Autowired
//    JournalEntriesHelper journalEntriesHelper;

    private final Gson gson = new Gson();

    private final GoogleAuthenticator gAuth;
    //private final CredentialRepository credentialRepository ;


    @SneakyThrows
    @GetMapping("/generate/{username}")
    //For generating
    public void generate(@PathVariable String username, HttpServletResponse response) {
        final GoogleAuthenticatorKey key = gAuth.createCredentials(username);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        String otpAuthURL = GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL("goFatoorah", username, key);

        BitMatrix bitMatrix = qrCodeWriter.encode(otpAuthURL, BarcodeFormat.QR_CODE, 200, 200);

        ServletOutputStream outputStream = response.getOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        outputStream.close();
        userHelper.add2FactorAuthentication(username);
    }

    @PostMapping("/validate/key")
    public Validation validateKey(@RequestBody ValidateCodeDto body) {
        return new Validation(gAuth.authorizeUser(body.getUsername(), body.getCode()));
    }

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
        return userHelper.saveUser(userEntity, true);
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

    @PostMapping(value = "/saveCreditInvoice")
    public String saveCreditInvoice(@RequestBody CreditInvoice invoice)  {
        return creditInvoiceHelper.save(invoice);
    }

    @PostMapping(value = "/saveDebitInvoice")
    public String saveDebitInvoice(@RequestBody DebitInvoice invoice)  {
        return debitInvoiceHelper.save(invoice);
    }

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
    public String saveProductMain(@RequestBody ProductMain productMain) {
        return productMainHelper.save(productMain);
    }
//test save product added by moqeet
    @PostMapping("/updateProductNameArabic")
    public String updateProductNameArabic(@RequestBody ProductMain products) {
        return productMainHelper.updateProductNameArabic(products);
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

    @PostMapping("/updateCreditInvoice")
    public String updateCreditInvoice(@RequestBody CreditInvoice creditInvoice) {
        return creditInvoiceHelper.updateCreditInvoice(creditInvoice);
    }

    @PostMapping("/updateDebitInvoice")
    public String updateDebitInvoice(@RequestBody DebitInvoice debitInvoice) {
        return debitInvoiceHelper.updateDebitInvoice(debitInvoice);
    }

    @PostMapping("/updateInvoice")
    public String updateInvoice(@RequestBody Invoice invoice) {
        return invoiceHelper.update(invoice);
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
    public String updateProductMain(@RequestBody ProductMain productMain) {
        return productMainHelper.update(productMain);
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

    @GetMapping("/deleteCreditInvoices")
    public String deleteCreditInvoices(@RequestParam String id) {
        return creditInvoiceHelper.deleteCreditInvoice(id);
    }

    @GetMapping("/deleteDebitInvoices")
    public String deleteDebitInvoices(@RequestParam String id) {
        return debitInvoiceHelper.deleteDebitInvoice(id);
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
    public String getAvailableInvoiceNo(@RequestParam String id, @RequestParam String userType) {
        //id could be of company or user
        if(userType.equals(Constants.TYPE_COMPANY))
            return gson.toJson(invoiceHelper.getNextInvoiceNoByCompanyID(id));
        if(userType.equals(Constants.TYPE_INDIVIDUAL))
            return gson.toJson(invoiceHelper.getNextInvoiceNoIndividual(id));
        else
            return gson.toJson(invoiceHelper.getNextInvoiceNoByUserID(id));
    }

    @GetMapping("/getNextCreditNo")
    public String getNextCreditNo(@RequestParam String id, @RequestParam String userType) {
        if(userType.equals(Constants.TYPE_COMPANY) || userType.equals(Constants.TYPE_INDIVIDUAL))
            return creditInvoiceHelper.getCreditInvoiceNoByCompanyID(id);
        else
            return creditInvoiceHelper.getUserNextCreditNo(id);
    }

    @GetMapping("/getNextDebitNo")
    public String getNextDebitNo(@RequestParam String id, @RequestParam String userType) {
        if(userType.equals(Constants.TYPE_COMPANY) || userType.equals(Constants.TYPE_INDIVIDUAL))
            return gson.toJson(debitInvoiceHelper.getDebitInvoiceNoByCompanyID(id));
        else
            return gson.toJson(debitInvoiceHelper.getNextDebitNo(id));
    }

    @GetMapping("/getAvailableInvoiceB2CNo")
    public String getAvailableInvoiceB2CNo(@RequestParam String id, @RequestParam String userType) {
        //id could be of company or user
        if(userType.equals(Constants.TYPE_COMPANY))
            return gson.toJson(invoiceB2CHelper.getNextInvoiceNoByCompanyID(id));
        if(userType.equals(Constants.TYPE_INDIVIDUAL))
            return gson.toJson(invoiceB2CHelper.getNextInvoiceNoIndividual(id));
        else
            return gson.toJson(invoiceB2CHelper.getNextInvoiceNoByUserID(id));
    }

    @GetMapping("/searchProducts")
    public String searchProducts(@RequestParam String productName) {
        return productMainHelper.searchProducts(productName);
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

    @GetMapping("/getAllCustomersEnglish")
    public String getAllCustomersEnglish(@RequestParam String companyID) {
        return customerHelper.getAllCustomersEnglish(companyID);
    }

    @GetMapping("/getCustomer")
    public String getCustomer(@RequestParam String customerID) { return customerHelper.getCustomer (customerID);}

    @GetMapping("/getAllTypesInvoiceByCustomer")
    public String getAllTypesInvoiceByCustomer(@RequestParam String customerName, @RequestParam String location, @RequestParam String companyID) { return allInvoicesHelper.getAllTypesInvoiceByCustomer(customerName,location,companyID); }

    @GetMapping("/getAllTypesInvoiceByCustomerforCompany")
    public String getAllTypesInvoiceByCustomerforCompany(@RequestParam String customerName, @RequestParam String companyID) { return allInvoicesHelper.getAllTypesInvoiceByCustomerforCompany(customerName,companyID); }

    @GetMapping("/getAllTypesInvoiceByInvoiceNumber")
    public String getAllTypesInvoiceByInvoiceNumber(@RequestParam String invoiceNumber, @RequestParam String location, @RequestParam String companyID) { return allInvoicesHelper.getAllTypesInvoiceByInvoiceNumber(invoiceNumber, location, companyID); }

    @GetMapping("/getAllTypesInvoiceByInvoiceNumberforCompany")
    public String getAllTypesInvoiceByInvoiceNumberforCompany(@RequestParam String invoiceNumber, @RequestParam String companyID) { return allInvoicesHelper.getAllTypesInvoiceByInvoiceNumberforCompany(invoiceNumber, companyID); }

    @GetMapping("/getAllTypesInvoiceByDurationforCompany")
    public String getAllTypesInvoiceByDurationforCompany(@RequestParam String startDate, String endDate, String companyID) { return allInvoicesHelper.getAllTypesInvoiceByDurationforCompany(startDate, endDate, companyID); }

    @GetMapping("/getAllTypesInvoiceByDuration")
    public String getAllTypesInvoiceByDuration(@RequestParam String startDate, String endDate, String companyID, String location) { return allInvoicesHelper.getAllTypesInvoiceByDuration(startDate, endDate, companyID, location); }

    @GetMapping("/getAllTypesInvoiceByID")
    public String getAllTypesInvoiceByID(@RequestParam String id) { return allInvoicesHelper.getAllTypesInvoiceByID(id); }

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

    @GetMapping("/getInvoicesByDurationLocation")
    public String getInvoicesByDurationLocation(@RequestParam String startDate, String endDate, String companyID, String location){
        return invoiceHelper.getInvoicesByDurationLocation(startDate, endDate, companyID, location);
    }

    @GetMapping("/getReportFiltersInvoiceB2C")
    public String getReportFiltersInvoiceB2C(@RequestParam String companyID, String id, String startDate, String endDate){
        return reportsHelper.getReportFiltersInvoiceB2C(companyID, id, startDate, endDate);
    }

    @GetMapping("/getReportFilters")
    public String getReportFilters(@RequestParam String companyID, String id, String customer, String location, String startDate, String endDate){
        return reportsHelper.getReportFilters(companyID, id, customer, location, startDate, endDate);
    }

    @GetMapping("/getReportFiltersCompany")
    public String getReportFiltersCompany(@RequestParam String companyID, String id, String customer, String location, String startDate, String endDate){
        return reportsHelper.getReportFilters(companyID,"", customer, location, startDate, endDate);
    }

//    @GetMapping ("/getCurrencyRateSAR")
//    public String getCurrencyRateSAR(){
//        return String.valueOf(Utility.getCurrencyRateSAR());
//    }

    @GetMapping("/getReportB2CByCompany")
    public String getReportB2CByCompany(@RequestParam String companyID){
        return reportsHelper.getReportB2CByCompany(companyID);
    }

    @GetMapping("/getReportByCompany")
    public String getReportByCompany(@RequestParam String companyID){
        return reportsHelper.getReportByCompany(companyID);
    }

    @GetMapping("/getReportB2CByUser")
    public String getReportB2CByUser(@RequestParam String id){
        return reportsHelper.getReportB2CByUser(id);
    }

    @GetMapping("/getReportByUser")
    public String getReportByUser(@RequestParam String id, String location){
        return reportsHelper.getReportByUser(id, location);
    }

    @GetMapping("/getTopSoldProductsByDate")
    public String getTopSoldProductsByDate(@RequestParam String startDate, String endDate, String companyID) throws ParseException {
        return reportsHelper.getTopSoldProductsByDate(startDate, endDate, companyID);
    }

    @GetMapping("/getTotalSalesByDate")
    public String getTotalSalesByDate(@RequestParam String startDate, String endDate, String companyID) throws ParseException {
        return reportsHelper.getTotalSalesByDate(startDate, endDate, companyID);
    }

//    ToDo
//    @GetMapping("/getInvoicesByYear")
//    public String getInvoicesByYear(@RequestParam String companyID) {
//        return reportsHelper.getInvoicesByYear(companyID);
//    }

    @GetMapping("/getInvoicesByCustomer")
    public String getInvoicesByCustomer(@RequestParam String customerName) { return invoiceHelper.getInvoicesByCustomer(customerName); }

    @GetMapping("/getInvoicesByCustomerLocation")
    public String getInvoicesByCustomerLocation(@RequestParam String customerName, @RequestParam String location) { return invoiceHelper.getInvoicesByCustomerLocation(customerName, location); }

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

    @GetMapping("/getAllCreditDebitInvoices")
    public String getAllCreditDebitInvoices(@RequestParam String companyID){
        List<?> list = new ArrayList();
        invoiceHelper.addAllInvoices(companyID, (List<Invoice>) list);
        creditInvoiceHelper.addAllInvoices(companyID, (List<CreditInvoice>) list);
        debitInvoiceHelper.addAllInvoices(companyID, (List<DebitInvoice>) list);
        return gson.toJson(list);
    }

    @GetMapping("/getCreditInvoicesByCompany")
    public String getCreditInvoicesByCompany(@RequestParam String companyID) {return creditInvoiceHelper.getAllCreditInvoicesByCompanyID(companyID); }

    @GetMapping("/getDebitInvoicesByCompany")
    public String getDebitInvoicesByCompany(@RequestParam String companyID) {return debitInvoiceHelper.getAllDebitInvoicesByCompanyID(companyID); }

    @GetMapping("/getInvoicesByCompany")
    public String getInvoicesByCompany(@RequestParam String companyID) {return invoiceHelper.getInvoicesByCompany(companyID); }

    @GetMapping("/getAllInvoicesByLocation")
    public String getAllInvoicesByLocation(@RequestParam String companyID, @RequestParam String location) {
        return allInvoicesHelper.getAllInvoicesByLocation(companyID, location); }

    @GetMapping("/getAllInvoicesB2CByLocation")
    public String getAllInvoicesB2CByLocation(@RequestParam String companyID, @RequestParam String location) { return invoiceB2CHelper.getAllInvoicesB2CByLocation(companyID, location); }

    @GetMapping("/getInvoicesB2CByCompany")
    public String getInvoicesB2CByCompany(@RequestParam String companyID) { return invoiceB2CHelper.getInvoicesByCompany(companyID); }

    @GetMapping("/getInvoiceStatus")
    public String getInvoiceStatus(@RequestParam String id) {
        return invoiceHelper.getInvoiceStatus(id);
    }

    //for all is added below
//    @PostMapping("/setInvoiceStatus")
//    public String setInvoiceStatus(@RequestParam String id, String status) {
//        return invoiceHelper.setInvoiceStatus(id, status);
//    }

    @PostMapping("/setInvoiceStatus")
    public String setInvoiceStatus(@RequestParam String id, String status) {
        return allInvoicesHelper.setInvoiceStatus(id, status);
    }

    @PostMapping("/setInvoiceStatusByInvoiceID")
    public String setInvoiceStatusByInvoiceID(@RequestBody Invoice invoice) {
        return invoiceHelper.setInvoiceStatusByInvoiceID(invoice.getInvoiceNumber(), invoice.getStatus());
    }

    @PostMapping("/signIn")
    public String signIn(@RequestBody Login login)
    {
            return loginHelper.signIn(login);
    }

    @PostMapping("/signInOkta")
    public String signInOkta(@RequestBody Login login)
    {
        try {
            return loginHelper.signInOkta(login.getEmail());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "Okta signIn failed:" + e.getMessage();
        }
    }

    @PostMapping("/forgetPassword")
    public String forgetPassword(@RequestBody Login login) { return loginHelper.forgetPassword(login); }

    @PostMapping("/validateUpdatePassword")
    public String validateUpdatePassword(@RequestBody Login login) { return loginHelper.validateUpdatePassword(login); }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestBody Login login) { return loginHelper.resetPassword(login); }

    @PostMapping("/changePassword")
    public String changePassword(@RequestBody UpdatePassword updatePassword) { return updatePasswordHelper.changePassword(updatePassword); }

//    @PostMapping("/signInUser")
//    public String signInUser(@RequestBody User user){
//        return userHelper.signInUser(user);
//    }
//
//    @PostMapping("/signInCompany")
//    public String signInCompany(@RequestBody CompanyXML company){
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

    @GetMapping("/getProductsNames")
    public String getProductsNames(@RequestParam String companyId) { return productMainHelper.getProductsNames(companyId); }

    @GetMapping("/getProducts")
    public String getProducts(@RequestParam String companyId) {
        return productMainHelper.getProducts(companyId);
    }

//    @GetMapping("/getProductsNamesTest")
//    public String getProductsNamesTest(@RequestParam String companyId) {
//        return productMainHelper.getProductsNamesTest(companyId);
//    }

    @GetMapping("/searchAllProducts")
    public String searchAllProducts(@RequestParam String companyId, String name) {
        if(name.length() > 2)
            return productMainHelper.searchAllProducts(companyId, name);
        else
            return gson.toJson("Minimum length should be 3");
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


    @GetMapping("/getLoginToken")
    public String getLoginToken(@RequestParam String email,@RequestParam String token){
        return loginHelper.getLoginToken(email, token);
    }

    @GetMapping(value = "/getCurrencyRateSAR")
    public String getCurrencyRateSAR(@RequestParam String currency){
       //to remove "" from currency
        if(currency.charAt(0) == '\"' && currency.charAt(currency.length() - 1) == '\"')
            currency = currency.replaceAll("^\"|\"$", "");
        Utility util = new Utility();
        return util.getCurrencyRateSAR(currency);
    }

    //Contract
    @GetMapping("/getContractByID")
    public String getContractByID(@RequestParam String id){
        return contractHelper.getContractByID(id);
    }

    @GetMapping("/getContractsByCompanyID")
    public String getContractsByCompanyID(@RequestParam String companyID){ return contractHelper.getContractsByCompanyID(companyID);}

    @GetMapping("/deleteContract")
    public String deleteContract(@RequestParam String id){
        return contractHelper.deleteContract(id);
    }

    @PostMapping("/saveContract")
    public String saveContract(@RequestBody Contract contract){
        return contractHelper.save(contract);
    }

    @PostMapping("/updateContract")
    public String updateContract(@RequestParam Contract contract){
        return contractHelper.updateContract(contract);
    }

    @GetMapping(value="/getInvoiceXML")
    public ResponseEntity<Object> getInvoiceXML(@RequestParam String invoiceID) throws Exception {
        invoiceHelper.getInvoiceXML(invoiceID);
        //replaces Invoice tag with multiple values
        Utility.updateInvoiceXML();
        File file = new File(Constants.INVOICE_XML_PATH);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return ResponseEntity.ok().headers(headers).contentLength(file.length()).contentType(
                MediaType.parseMediaType("application/txt")).body(resource);
    }

//    @GetMapping("/getJournalEntries")
//    public String getJournalEntries(String invoiceNo){
//        return journalEntriesHelper.getByInvoiceNo(invoiceNo);
//    }

}
