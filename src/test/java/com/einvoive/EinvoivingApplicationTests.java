package com.einvoive;

import com.einvoive.helper.InvoiceHelper;
import com.einvoive.model.Invoice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EinvoivingApplicationTests {

    @Autowired
    InvoiceHelper invoiceHelper;

    @Test
    void contextLoads() {
    }

    @Test
    void saveInvoice(){
        Invoice inv = new Invoice();
        inv.setCompanyID("hyphen");
        invoiceHelper.save(inv);
        System.out.println("");
    }

}
