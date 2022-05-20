package com.einvoive.zatcaxml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "cac:RegistrationAddress")
public class RegistrationAddress {
    @XmlElement(name = "cbc:CityName")
    private String cityName;
    @XmlElement(name = "cbc:CountrySubentity")
    private String countrySubentity;
    @XmlElement(name = "cac:Country")
    private Country1 country1;
    public RegistrationAddress(){}
    public RegistrationAddress(String cityName, String countrySubentity, Country1 country1) {
        this.cityName = cityName;
        this.countrySubentity = countrySubentity;
        this.country1 = country1;
    }

    public static class Country1{
        @XmlElement(name = "cbc:IdentificationCode")
        private String identificationCode;
        public Country1(){}
        public Country1(String identificationCode) {
            this.identificationCode = identificationCode;
        }
    }
}
