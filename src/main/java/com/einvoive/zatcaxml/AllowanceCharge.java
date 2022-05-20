package com.einvoive.zatcaxml;

import javax.xml.bind.annotation.XmlElement;

public class AllowanceCharge {
    @XmlElement(name = "cbc:ChargeIndicator")
    private boolean chargeIndicator;
    @XmlElement(name = "cbc:AllowanceChargeReason")
    private String allowanceChargeReason;
    @XmlElement(name = "cbc:MultiplierFactorNumeric")
    private String multiplierFactorNumeric;
    @XmlElement(name = "cbc:Amount")
    private Amount amount;
    @XmlElement(name = "cbc:BaseAmount")
    private Amount baseAmount;

    public AllowanceCharge(){}
    public AllowanceCharge(boolean chargeIndicator, String allowanceChargeReason, Amount amount) {
        this.chargeIndicator = chargeIndicator;
        this.allowanceChargeReason = allowanceChargeReason;
        this.amount = amount;
    }

    public void setChargeIndicator(boolean chargeIndicator) {
        this.chargeIndicator = chargeIndicator;
    }

    public void setAllowanceChargeReason(String allowanceChargeReason) {
        this.allowanceChargeReason = allowanceChargeReason;
    }

    public void setMultiplierFactorNumeric(String multiplierFactorNumeric) {
        this.multiplierFactorNumeric = multiplierFactorNumeric;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public void setBaseAmount(Amount baseAmount) {
        this.baseAmount = baseAmount;
    }
}
