package com.einvoive.controller;

import com.einvoive.helper.CustomerHelper;
import com.einvoive.helper.InvoiceHelper;
import com.einvoive.helper.ProductHelper;
import com.einvoive.helper.UserHelper;
import com.einvoive.model.Customer;
import com.einvoive.model.Invoice;
import com.einvoive.model.Product;
import com.einvoive.model.User;
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
    InvoiceHelper invoiceHelper;

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
}
