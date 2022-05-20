package com.einvoive.zatcaxml;

import javax.xml.bind.annotation.XmlElement;

public class CommodityClassification {
    @XmlElement(name = "cbc:ItemClassificationCode")
    private final IdentificationCode itemClassificationCode;

    public CommodityClassification(IdentificationCode itemClassificationCode) {
        this.itemClassificationCode = itemClassificationCode;
    }
}
