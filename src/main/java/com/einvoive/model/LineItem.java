package com.einvoive.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

    @Document
    public class LineItem {

        @Id
        private String id;
        private String productName;
        private String price;
        private String quantity;
        private String taxableAmount;
        private String discount;
        private String vat;
        private String taxAmount;
        private String itemSubTotal;
        private String invoiceId;
        private String amountBeforeTax;
        private String description;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

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

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
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

        public String getVat() {
            return vat;
        }

        public void setVat(String vat) {
            this.vat = vat;
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

        public String getAmountBeforeTax() {
            return amountBeforeTax;
        }

        public void setAmountBeforeTax(String amountBeforeTax) {
            this.amountBeforeTax = amountBeforeTax;
        }
    }
