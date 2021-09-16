package com.einvoive.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
    public class User {

        @Id
        private String id;
        private String userId;
        private String name;
        private String email;
        private String password;
        private String location;
        private String CellNo;
        private String companyID;
        private List<String> listRoles;

        public List<String> getListRoles() {
        return listRoles;
    }

        public void setListRoles(List<String> listRoles) {
        this.listRoles = listRoles;
    }

        public String getCompanyID() {
            return companyID;
        }

        public void setCompanyID(String companyID) {
            this.companyID = companyID;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getCellNo() {
            return CellNo;
        }

        public void setCellNo(String cellNo) {
            CellNo = cellNo;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }


        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
