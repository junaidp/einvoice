package com.einvoive.zatcaxml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "cac:InvoiceLine")
@XmlAccessorType(XmlAccessType.FIELD)
public class InvoiceLine {
    @XmlElement(name = "cbc:ID")
    private String id;
    @XmlElement(name = "cbc:Note")
    private String note;
    @XmlElement(name = "cbc:InvoicedQuantity")
    private InvoicedQuantity invoicedQuantity;
    @XmlElement(name = "cbc:LineExtensionAmount")
    private Amount lineExtensionAmount;
    @XmlElement(name = "cbc:AccountingCost")
    private String accountingCost;
    @XmlElement(name = "cac:OrderLineReference")
    private OrderLineReference orderLineReference;
    @XmlElement(name = "cac:AllowanceCharge")
    private AllowanceCharge allowanceCharge;
    @XmlElement(name = "cac:TaxTotal")
    private TaxTotal1 taxTotal;
    @XmlElement(name = "cac:Item")
    private Item item;
    @XmlElement(name = "cac:Price")
    private Price price;

    public InvoiceLine(){}
    public InvoiceLine(String id, String note, InvoicedQuantity invoicedQuantity, Amount lineExtensionAmount, String accountingCost, OrderLineReference orderLineReference, AllowanceCharge allowanceCharge, TaxTotal1 taxTotal, Item item, Price price) {
        this.id = id;
        this.note = note;
        this.invoicedQuantity = invoicedQuantity;
        this.lineExtensionAmount = lineExtensionAmount;
        this.accountingCost = accountingCost;
        this.orderLineReference = orderLineReference;
        this.allowanceCharge = allowanceCharge;
        this.taxTotal = taxTotal;
        this.item = item;
        this.price = price;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class TaxTotal1{
        @XmlElement(name = "cbc:TaxAmount")
        private Amount taxTotal;

        public TaxTotal1(){}
        public TaxTotal1(Amount taxTotal) {
            this.taxTotal = taxTotal;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class OrderLineReference{
        @XmlElement(name = "cbc:LineID")
        private String lineID;
        public OrderLineReference(){}
        public OrderLineReference(String lineID) {
            this.lineID = lineID;
        }
    }

}
