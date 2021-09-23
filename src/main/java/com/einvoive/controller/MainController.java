package com.einvoive.controller;

import com.einvoive.helper.*;
import com.einvoive.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    TranslationHelper translationHelper;

    @Autowired
    ProductMainHelper productMainHelper;

    @Autowired
    InvoiceHelper invoiceHelper;

    @Autowired
    RollsHelper rollsHelper;

    @Autowired
    VatHelper vatHelper;

    @Autowired
    LocationHelper locationHelper;

    @PostMapping("/saveUser")
    public String saveUser(@RequestBody User userEntity) {
        return userHelper.saveUser(userEntity);
    }

    @PostMapping("/saveCompany")
    public String saveCompany(@RequestBody Company company) { return companyHelper.saveCompany(company);
    }

    @PostMapping("/saveCustomer")
    public String saveCustomer(@RequestBody Customer customer) {
        return customerHelper.save(customer);
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

    @PostMapping("/savePaymentCard")
    public String savePaymentCard(@RequestBody PaymentCard paymentCard) {
        return paymentCardHelper.savePaymentCard(paymentCard);
    }

    @PostMapping("/saveProductMain")
    public String saveProductMain(@RequestBody ProductMain product) {
        return productMainHelper.save(product);
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

    @PostMapping("/updateUser")
    public String updateUser(@RequestBody User user) {
        return userHelper.updateUser(user);
    }

    @PostMapping("/updatePaymentCard")
    public String updatePaymentCard(@RequestBody PaymentCard paymentCard) {
        return paymentCardHelper.updatePaymentCard(paymentCard);
    }

    @PostMapping("/updateCompany")
    public String updateCompany(@RequestBody Company company) {
        return companyHelper.updateCompany(company);
    }

    @PostMapping("/updateCustomer")
    public String updateCustomer(@RequestBody Customer customer) {
        return customerHelper.update(customer);
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

    @PostMapping("/updateProductMain")
    public String updateProductMain(@RequestBody ProductMain product) {
        return productMainHelper.update(product);
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


    @GetMapping("/getTopSaledProducts")
    public String getTopSaledProducts(@RequestParam String companyID) {
        return productMainHelper.getTopSaledProducts(companyID);
    }

    @GetMapping("/getPaymentCard")
    public String getPaymentCard(@RequestParam String userId) {
        return paymentCardHelper.getPaymentCards(userId);
    }

    @GetMapping("/getAllCustomers")
    public String getAllCustomers(@RequestParam String companyID) {
        return customerHelper.getAllCustomers(companyID);
    }

    @GetMapping("/getAllInvoices")
    public String getAllInvoices(@RequestParam String companyID) {
        return invoiceHelper.getAllInvoices(companyID);
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

    @GetMapping("/getProducts")
    public String getProducts(@RequestParam String invoiceId) {
        return lineItemHelper.getItems(invoiceId);
    }

    @GetMapping("/getProductsMain")
    public String getProductsMain(@RequestParam String companyId) {
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
    public String getAllLocations() {
        return locationHelper.getAllLocations();
    }

    @GetMapping("/getTranslation")
    public String getTranslation(@RequestParam String english) {
        return translationHelper.getTranslation(english);
    }

}
