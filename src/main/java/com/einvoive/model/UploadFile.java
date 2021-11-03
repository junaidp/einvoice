package com.einvoive.model;

import org.springframework.web.multipart.MultipartFile;

public class UploadFile {

    private MultipartFile file;
    private String companyID;
    private String loggedInUserID;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getLoggedInUserID() {
        return loggedInUserID;
    }

    public void setLoggedInUserID(String loggedInUserID) {
        this.loggedInUserID = loggedInUserID;
    }
}
