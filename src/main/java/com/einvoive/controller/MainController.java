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
    CustomerHelper customerHelper;

    @Autowired
    ProductHelper productHelper;

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

    @GetMapping("/getCustomers")
    public String getCustomers(@RequestParam String userId) {

        return customerHelper.getAllCustomers(userId);
    }

    @GetMapping("/getInvoices")
    public String getInvoices(@RequestParam String userId) {
        return invoiceHelper.getAllInvoices(userId);
    }

    @GetMapping("/signIn")
    public String singIn(@RequestBody User user){
        return userHelper.singIn(user);
    }

    @GetMapping("/getProducts")
    public String getProducts(@RequestParam String invoiceId) {
        return productHelper.getProducts(invoiceId);
    }

    @GetMapping("/getProductsMain")
    public String getProductsMain(@RequestParam String userId) {
        return productMainHelper.getProducts(userId);
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
