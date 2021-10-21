package com.einvoive.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Translation {
    @Id
    private String id;
    private String arabic;
    private String english;
//    private String loggedInID;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getArabic() {
        return arabic;
    }

    public void setArabic(String arabic) {
        this.arabic = arabic;
    }

//    public String getLoggedInID() {
//        return loggedInID;
//    }
//
//    public void setLoggedInID(String loggedInID) {
//        this.loggedInID = loggedInID;
//    }
}
