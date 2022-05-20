package com.einvoive.zatcaxml;

import javax.xml.bind.annotation.XmlElement;

public class Delivery {
    @XmlElement(name = "cbc:ActualDeliveryDate")
    private String actualDeliveryDate;
    @XmlElement(name = "cac:DeliveryLocation")
    private DeliveryLocation deliveryLocation;

    public Delivery(){}
    public Delivery(String actualDeliveryDate, DeliveryLocation deliveryLocation) {
        this.actualDeliveryDate = actualDeliveryDate;
        this.deliveryLocation = deliveryLocation;
    }

    public static class DeliveryLocation{
        @XmlElement(name = "cbc:ID")
        private Scheme scheme;
        @XmlElement(name = "cac:Address")
        private Address address;

        public DeliveryLocation(){}
        public DeliveryLocation(Scheme scheme, Address address) {
            this.scheme = scheme;
            this.address = address;
        }
    }
}
