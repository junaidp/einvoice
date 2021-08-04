package com.einvoive.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

    @Document
    public class Product {

        @Id
        private String id;
        private String name;
        private String price;
        private String quantity;
        private String taxableAmount;
        private String discount;
        private String taxRate;
        private String taxAmount;
        private String itemSubTotal;
        private String invoiceId;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }


        public String getTaxableAmount() {
            return taxableAmount;
        }

        public void setTaxableAmount(String taxableAmount) {
            this.taxableAmount = taxableAmount;
        }

        public String getInvoiceId() {
            return invoiceId;
        }

        public void setInvoiceId(String invoiceId) {
            this.invoiceId = invoiceId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getTaxRate() {
            return taxRate;
        }

        public void setTaxRate(String taxRate) {
            this.taxRate = taxRate;
        }

        public String getTaxAmount() {
            return taxAmount;
        }

        public void setTaxAmount(String taxAmount) {
            this.taxAmount = taxAmount;
        }

        public String getItemSubTotal() {
            return itemSubTotal;
        }

        public void setItemSubTotal(String itemSubTotal) {
            this.itemSubTotal = itemSubTotal;
        }
}
