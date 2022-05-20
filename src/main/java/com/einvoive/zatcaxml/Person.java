package com.einvoive.zatcaxml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "cac:Person")
public class Person {
    @XmlElement(name = "cbc:FirstName")
    private String firstName;
    @XmlElement(name = "cbc:FamilyName")
    private String familyName;
    @XmlElement(name = "cbc:MiddleName")
    private String middleName;
    @XmlElement(name = "cbc:JobTitle")
    private String jobTitle;
    public Person(){}
    public Person(String firstName, String familyName, String middleName, String jobTitle) {
        this.firstName = firstName;
        this.familyName = familyName;
        this.middleName = middleName;
        this.jobTitle = jobTitle;
    }
}
