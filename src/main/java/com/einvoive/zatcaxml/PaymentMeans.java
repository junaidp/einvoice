package com.einvoive.zatcaxml;

import com.einvoive.zatcaxml.PayeeFinancialAccount;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "cac:PaymentMeans")
public class PaymentMeans {
    @XmlElement(name = "cbc:PaymentMeansCode")
    private PaymentMeansCode paymentMeansCode;
    @XmlElement(name = "cbc:PaymentDueDate")
    private String paymentDueDate;
    @XmlElement(name = "cbc:PaymentChannelCode")
    private String paymentChannelCode;
    @XmlElement(name = "cbc:PaymentID")
    private String paymentID;
    @XmlElement(name = "cac:PayeeFinancialAccount")
    private PayeeFinancialAccount payeeFinancialAccount;

    public PaymentMeans(){}
    public PaymentMeans(PaymentMeansCode paymentMeansCode, String paymentDueDate, String paymentChannelCode, String paymentID, PayeeFinancialAccount payeeFinancialAccount) {
        this.paymentMeansCode = paymentMeansCode;
        this.paymentDueDate = paymentDueDate;
        this.paymentChannelCode = paymentChannelCode;
        this.paymentID = paymentID;
        this.payeeFinancialAccount = payeeFinancialAccount;
    }

    public static class PaymentMeansCode{
        @XmlAttribute(name = "listID")
        private String attribute;
        @XmlValue
        private String value;
        public PaymentMeansCode(){}
        public PaymentMeansCode(String attribute, String value) {
            this.attribute = attribute;
            this.value = value;
        }
    }
}
