package com.einvoive.zatcaxml;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Invoice")
public class InvoiceXML {
    @XmlElement(name = "cbc:UBLVersionID")
    private Double UBLVersionID;
    @XmlElement(name = "cbc:ID")
    private String ID;
    @XmlElement(name = "cbc:IssueDate")
    private String issueDate;
    @XmlElement(name = "cbc:InvoiceTypeCode")
    private InvoiceTypeCode invoiceTypeCode;
    @XmlElement(name = "cbc:Note")
    private Note note;
    @XmlElement(name = "cbc:TaxPointDate")
    private String taxPointDate;
    @XmlElement(name = "cbc:DocumentCurrencyCode")
    private DocumentCurrencyCode documentCurrencyCode;
    @XmlElement(name = "cbc:AccountingCost")
    private String accountingCost;
    @XmlElement(name = "cac:InvoicePeriod")
    private InvoicePeriod invoicePeriod;
    @XmlElement(name = "cac:OrderReference")
    private OrderReference orderReference;
    @XmlElement(name = "cac:ContractDocumentReference")
    private ContractDocumentReference contractDocumentReference;
    @XmlElement (name = "cac:AdditionalDocumentReference")
    private AdditionalDocumentReference additionalDocumentReference;
    @XmlElement(name = "cac:AccountingSupplierParty")
    private AccountingSupplierParty accountingSupplierParty;
    @XmlElement(name = "cac:AccountingCustomerParty")
    private AccountingCustomerParty accountingCustomerParty;
    @XmlElement(name = "cac:PayeeParty")
    private PayeeParty payeeParty;
    @XmlElement(name = "cac:Delivery")
    private Delivery delivery;
    @XmlElement(name = "cac:PaymentMeans")
    private PaymentMeans paymentMeans;
    @XmlElement(name = "cac:PaymentTerms")
    private PaymentTerms paymentTerms;
    @XmlElement(name = "cac:AllowanceCharge")
    private AllowanceCharge allowanceCharge;
    @XmlElement(name = "cac:TaxTotal")
    private TaxTotal taxTotal;
    @XmlElement(name = "cac:LegalMonetaryTotal")
    private LegalMonetaryTotal legalMonetaryTotal;
    @XmlElement(name = "cac:InvoiceLine")
    private InvoiceLine invoiceLine;

    public InvoiceXML(){}

    public InvoiceXML(Double UBLVersionID, String ID, String issueDate, InvoiceTypeCode invoiceTypeCode, Note note, String taxPointDate, DocumentCurrencyCode documentCurrencyCode, String accountingCost) {
        this.UBLVersionID = UBLVersionID;
        this.ID = ID;
        this.issueDate = issueDate;
        this.invoiceTypeCode = invoiceTypeCode;
        this.note = note;
        this.taxPointDate = taxPointDate;
        this.documentCurrencyCode = documentCurrencyCode;
        this.accountingCost = accountingCost;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class DocumentCurrencyCode{
        @XmlAttribute
        private String listID;
        @XmlAttribute
        private String listAgencyID;
        @XmlValue
        private String value;
        public DocumentCurrencyCode(){}
        public DocumentCurrencyCode(String listID, String listAgencyID, String value) {
            this.listID = listID;
            this.listAgencyID = listAgencyID;
            this.value = value;
        }
    }

    public void setInvoicePeriod(InvoicePeriod invoicePeriod) {
        this.invoicePeriod = invoicePeriod;
    }
    public void setOrderReference(OrderReference orderReference) { this.orderReference = orderReference; }
    public void setContractDocumentReference(ContractDocumentReference contractDocumentReference) {
        this.contractDocumentReference = contractDocumentReference;
    }
    public void setAdditionalDocumentReference(AdditionalDocumentReference additionalDocumentReference) {
        this.additionalDocumentReference = additionalDocumentReference;
    }

    public void setAccountingSupplierParty(AccountingSupplierParty accountingSupplierParty) {
        this.accountingSupplierParty = accountingSupplierParty;
    }

    public void setAccountingCustomerParty(AccountingCustomerParty accountingCustomerParty) {
        this.accountingCustomerParty = accountingCustomerParty;
    }

    public void setPayeeParty(PayeeParty payeeParty) {
        this.payeeParty = payeeParty;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public void setPaymentTerms(PaymentTerms paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    public void setPaymentMeans(PaymentMeans paymentMeans) {
        this.paymentMeans = paymentMeans;
    }

    public void setAllowanceCharge(AllowanceCharge allowanceCharge) {
        this.allowanceCharge = allowanceCharge;
    }

    public void setTaxTotal(TaxTotal taxTotal) {
        this.taxTotal = taxTotal;
    }

    public void setLegalMonetaryTotal(LegalMonetaryTotal legalMonetaryTotal) {
        this.legalMonetaryTotal = legalMonetaryTotal;
    }

    public void setInvoiceLine(InvoiceLine invoiceLine) {
        this.invoiceLine = invoiceLine;
    }
}
