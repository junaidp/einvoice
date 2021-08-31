package com.einvoive.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

    @Document
    public class User {

        @Id
        private String id;
        private String userId;
        private String name;
        private String email;
        private String password;
        private String role;
        private String location;
        private String CellNo;
        private String comapnyID;

        public String getComapnyID() {
            return comapnyID;
        }

        public void setComapnyID(String comapnyID) {
            this.comapnyID = comapnyID;
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

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
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
