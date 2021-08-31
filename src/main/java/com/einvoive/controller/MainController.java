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
    CompanyHelper companyHelper;

    @Autowired
    CustomerHelper customerHelper;

    @Autowired
    ProductHelper productHelper;

    @Autowired
    AccountsHelper accountsHelper;

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

    @PostMapping("/saveProduct")
    public String saveProduct(@RequestBody Product product) {
        return productHelper.save(product);
    }

    @PostMapping("/saveAccounts")
    public String saveAccounts(@RequestBody Accounts accounts) {
        return accountsHelper.save(accounts);
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

    //update

    @PostMapping("/updateUser")
    public String updateUser(@RequestBody User user) {
        return userHelper.updateUser(user);
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
    public String updateProduct(@RequestBody Product product) {
        return productHelper.update(product);
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
//    @GetMapping("/deleteCustomers")
//    public String deleteCustomers(@RequestParam String userId) {
//
//        return customerHelper.deleteCustomers(userId);
//    }
//
//    @GetMapping("/deleteInvoices")
//    public String deleteInvoices(@RequestParam String userId) {
//        return invoiceHelper.deleteInvoices(userId);
//    }
//
//    @GetMapping("/deleteUser")
//    public String singIn(@RequestBody User user){
//        return userHelper.delete(user);
//    }
//
//    @GetMapping("/deleteProducts")
//    public String deleteProducts(@RequestParam String invoiceId) {
//        return productHelper.deleteProducts(invoiceId);
//    }
//
//    @GetMapping("/deleteProductsMain")
//    public String deleteProductsMain(@RequestParam String userId) {
//        return productMainHelper.deleteProducts(userId);
//    }
//
//    @GetMapping("/deleteBankAccounts")
//    public String deleteBankAccounts(@RequestParam String code) {
//        return bankAccountHelper.deleteBankAccounts(code);
//    }
//
//    @GetMapping("/deleteRevenues")
//    public String deleteRevenues(@RequestParam String code) {
//        return revenueHelper.deleteRevenues(code);
//    }
//
//    @GetMapping("/deleteVats")
//    public String deleteVats() {
//        return vatHelper.deleteVats();
//    }
//
//    @GetMapping("/deleteLocations")
//    public String gdeleteLocations() {
//        return locationHelper.deleteLocations();
//    }

    //get

    @GetMapping("/getCustomers")
    public String getCustomers(@RequestParam String comapnyID) {

        return customerHelper.getAllCustomers(comapnyID);
    }

    @GetMapping("/getInvoices")
    public String getInvoices(@RequestParam String userId) {
        return invoiceHelper.getAllInvoices(userId);
    }

    @GetMapping("/signIn")
    public String singIn(@RequestBody User user){
        return userHelper.singIn(user);
    }

    @GetMapping("/signInCompany")
    public String signInCompany(@RequestBody Company company){
        return companyHelper.singIn(company);
    }

    @GetMapping("/getProducts")
    public String getProducts(@RequestParam String invoiceId) {
        return productHelper.getProducts(invoiceId);
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
    public String getAllVats() {
        return vatHelper.getAllVats();
    }

    @GetMapping("/getAllLocations")
    public String getAllLocations() {
        return locationHelper.getAllLocations();
    }

}
