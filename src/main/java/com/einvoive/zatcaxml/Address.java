package com.einvoive.zatcaxml;

import javax.xml.bind.annotation.XmlElement;

public class Address {
    @XmlElement(name = "cbc:StreetName")
    private String streetName;
    @XmlElement(name = "cbc:AdditionalStreetName")
    private String additionalStreetName;
    @XmlElement(name = "cbc:BuildingNumber")
    private String buildingNumber;
    @XmlElement(name = "cbc:CityName")
    private String cityName;
    @XmlElement(name = "cbc:PostalZone")
    private String postalZone;
    @XmlElement(name = "cbc:CountrySubentity")
    private String countrySubentity;
    @XmlElement(name = "cac:Country")
    private Country country;
    public Address(){}
    public Address(String streetName, String additionalStreetName, String buildingNumber, String cityName, String postalZone, String countrySubentity, Country country) {
        this.streetName = streetName;
        this.additionalStreetName = additionalStreetName;
        this.buildingNumber = buildingNumber;
        this.cityName = cityName;
        this.postalZone = postalZone;
        this.countrySubentity = countrySubentity;
        this.country = country;
    }
}
