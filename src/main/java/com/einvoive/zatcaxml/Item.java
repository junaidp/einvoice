package com.einvoive.zatcaxml;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class Item {
    @XmlElement(name = "cbc:Description")
    private Description description;
    @XmlElement(name = "cbc:Name")
    private String name;
    @XmlElement(name = "cac:SellersItemIdentification")
    private ItemIdentification sellersItemIdentification;
    @XmlElement(name = "cac:StandardItemIdentification")
    private ItemIdentification standardItemIdentification;
    @XmlElement(name = "cac:CommodityClassification")
    private CommodityClassification commodityClassification;
    @XmlElement(name = "ClassifiedTaxCategory")
    private TaxCategory taxCategory;

    public Item(){}

    public Item(Description description, String name, ItemIdentification sellersItemIdentification, ItemIdentification standardItemIdentification, CommodityClassification commodityClassification, TaxCategory taxCategory) {
        this.description = description;
        this.name = name;
        this.sellersItemIdentification = sellersItemIdentification;
        this.standardItemIdentification = standardItemIdentification;
        this.commodityClassification = commodityClassification;
        this.taxCategory = taxCategory;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Description{
        @XmlAttribute
        private String languageID;
        @XmlValue
        private String value;
        public Description(){}
        public Description(String languageID, String value) {
            this.languageID = languageID;
            this.value = value;
        }
    }
}
