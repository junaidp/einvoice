package com.einvoive.zatcaxml;

import javax.xml.bind.annotation.XmlElement;

public class Price {
    @XmlElement(name = "cbc:PriceAmount")
    private Amount priceAmount;
    @XmlElement(name = "cbc:BaseQuantity")
    private InvoicedQuantity baseQuantity;
    @XmlElement(name = "cac:AllowanceCharge")
    private AllowanceCharge allowanceCharge;
    public Price(){}
    public Price(Amount priceAmount, InvoicedQuantity baseQuantity, AllowanceCharge allowanceCharge) {
        this.priceAmount = priceAmount;
        this.baseQuantity = baseQuantity;
        this.allowanceCharge = allowanceCharge;
    }
}
