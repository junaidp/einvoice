package com.einvoive.zatcaxml;

import javax.xml.bind.annotation.XmlElement;

public class TaxCategory {
    @XmlElement(name = "cbc:ID")
    private ID id;
    @XmlElement(name = "cbc:Percent")
    private String percent;
    @XmlElement(name = "cbc:TaxExemptionReasonCode")
    private EndpointID taxExemptionReasonCode;
    @XmlElement(name = "cbc:TaxExemptionReason")
    private String taxExemptionReason;
    @XmlElement(name = "cac:TaxScheme")
    private TaxScheme taxScheme;

    public TaxCategory() {}

    public TaxCategory(ID id, String percent, EndpointID taxExemptionReasonCode, String taxExemptionReason, TaxScheme taxScheme) {
        this.id = id;
        this.percent = percent;
        this.taxExemptionReasonCode = taxExemptionReasonCode;
        this.taxExemptionReason = taxExemptionReason;
        this.taxScheme = taxScheme;
    }

}
