package com.einvoive.constants;

import java.nio.file.Paths;

public class Constants {

    //User Types
    public static final String TYPE_COMPANY= "company";
    public static final String TYPE_INDIVIDUAL = "individual";

    public static final String VAT_PAYABLE = "VAT Payable";
    public static String PROJECT_PATH = Paths.get("").toAbsolutePath().normalize().toString();
    public static String INVOICES_PATH = Constants.PROJECT_PATH+"\\src\\main\\resources\\Invoices\\";
//    public static String COMPANY_ID;
//    public static String LOGGED_IN_USER_ID;
    public static String STATUS_APPROVED = "APPROVED";
    public static String STATUS_UNAPPROVED = "UNAPPROVED";
    public static String STATUS_FORAPPROVAL = "FORAPPROVAL";
    public static String TYPE_LOCAL = "LOCAL";
    public static String TYPE_EXPORT = "EXPORT";

    //Journal Entries Constants
    public static String TAX_INVOICE = "TAX INVOICE";
    public static String TAX_INVOICE_AMOUNT = "TAX INVOICE AMOUNT";
    public static String RETENTION_INVOICE = "RETENTION INVOICE";
    public static String DEBIT_NOTE_APPROVAL = " DEBIT NOTE APPROVAL";
    public static String DEBIT_NOTE_RECEIPT = " DEBIT NOTE RECEIPT";
    public static String CREDIT_NOTE_APPROVAL = " CREDIT NOTE APPROVAL";
    public static String CREDIT_NOTE_RECEIPT = " CREDIT NOTE RECEIPT";

    //Categories
    public static String LOG_SAVE_INVOICE = "Saving Invoice";

}
