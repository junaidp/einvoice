package com.einvoive.zatcaxml;

import javax.xml.bind.annotation.*;

public class PayeeFinancialAccount {
    @XmlElement(name = "cbc:ID")
    private String id;
    @XmlElement(name = "cac:FinancialInstitutionBranch")
    private FinancialInstitutionBranch financialInstitutionBranch;
    private String value;

    public PayeeFinancialAccount(){}
    public PayeeFinancialAccount(String id, FinancialInstitutionBranch financialInstitutionBranch, String value) {
        this.id = id;
        this.financialInstitutionBranch = financialInstitutionBranch;
        this.value = value;
    }

    public static class FinancialInstitutionBranch{
        @XmlElement(name = "cac:FinancialInstitution")
        private FinancialInstitution financialInstitution;

        public FinancialInstitutionBranch(){}
        public FinancialInstitutionBranch(FinancialInstitution financialInstitution) {
            this.financialInstitution = financialInstitution;
        }

        public static class FinancialInstitution{
            @XmlElement(name = "cbc:ID")
            private String id;
            public FinancialInstitution(){}
            public FinancialInstitution(String id) {
                this.id = id;
            }
        }
    }
}
