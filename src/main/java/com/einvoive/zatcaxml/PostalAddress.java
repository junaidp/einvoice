package com.einvoive.zatcaxml;

import javax.xml.bind.annotation.XmlElement;

public class PostalAddress {
    @XmlElement(name = "cbc:ID")
    private ID id;
    @XmlElement(name = "cbc:Postbox")
    private String postbox;
    @XmlElement(name = "cbc:StreetName")
    private String streetName;
    @XmlElement(name = "cbc:AdditionalStreetName")
    private String additionalStreetName;
    @XmlElement(name = "cbc:BuildingNumber")
    private String buildingNumber;
    @XmlElement(name = "cbc:Department")
    private String department;
    @XmlElement(name = "cbc:CityName")
    private String cityName;
    @XmlElement(name = "cbc:PostalZone")
    private String postalZone;
    @XmlElement(name = "cbc:CountrySubentity")
    private String countrySubentity;
    @XmlElement(name = "cac:Country")
    private Country country;
    public PostalAddress(){}
    public PostalAddress(ID id, String postbox, String streetName, String additionalStreetName, String buildingNumber, String department, String cityName, String postalZone, String countrySubentity, Country country) {
        this.id = id;
        this.postbox = postbox;
        this.streetName = streetName;
        this.additionalStreetName = additionalStreetName;
        this.buildingNumber = buildingNumber;
        this.department = department;
        this.cityName = cityName;
        this.postalZone = postalZone;
        this.countrySubentity = countrySubentity;
        this.country = country;
    }

    public static class ID extends EndpointID{
        public ID() {
        }

        public ID(String schemeID, String schemeAgencyID, String value) {
            super(schemeID, schemeAgencyID, value);
        }
    }
}
