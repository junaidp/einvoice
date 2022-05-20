package com.einvoive.zatcaxml;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="cbc:Note")
public class Note {
    @XmlAttribute
    private String languageID;
    @XmlValue
    private String value;

    public Note(){}

    public Note(String value) {
        this.value = value;
    }

    public Note(String languageID, String value) {
        this.languageID = languageID;
        this.value = value;
    }
}
