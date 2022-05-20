package com.einvoive.zatcaxml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "cac:LegalMonetaryTotal")
public class LegalMonetaryTotal {
    @XmlElement(name = "cbc:LineExtensionAmount")
    private Amount lineExtensionAmount;
    @XmlElement(name = "cbc:TaxExclusiveAmount")
    private Amount taxExclusiveAmount;
    @XmlElement(name = "cbc:TaxInclusiveAmount")
    private Amount taxInclusiveAmount;
    @XmlElement(name = "cbc:AllowanceTotalAmount")
    private Amount allowanceTotalAmount;
    @XmlElement(name = "cbc:ChargeTotalAmount")
    private Amount chargeTotalAmount;
    @XmlElement(name = "cbc:PrepaidAmount")
    private Amount prepaidAmount;
    @XmlElement(name = "cbc:PayableRoundingAmount")
    private Amount payableRoundingAmount;
    @XmlElement(name = "cbc:PayableAmount")
    private Amount payableAmount;
    public LegalMonetaryTotal(){}
    public LegalMonetaryTotal(Amount lineExtensionAmount, Amount taxExclusiveAmount, Amount taxInclusiveAmount, Amount allowanceTotalAmount, Amount chargeTotalAmount, Amount prepaidAmount, Amount payableRoundingAmount, Amount payableAmount) {
        this.lineExtensionAmount = lineExtensionAmount;
        this.taxExclusiveAmount = taxExclusiveAmount;
        this.taxInclusiveAmount = taxInclusiveAmount;
        this.allowanceTotalAmount = allowanceTotalAmount;
        this.chargeTotalAmount = chargeTotalAmount;
        this.prepaidAmount = prepaidAmount;
        this.payableRoundingAmount = payableRoundingAmount;
        this.payableAmount = payableAmount;
    }
}
