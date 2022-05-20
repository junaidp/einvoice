package com.einvoive.constants;

public enum JournalEntriesEnum {

    TAX_INVOICE(1,Constants.TAX_INVOICE),
    TAX_INVOICE_AMOUNT(2, Constants.TAX_INVOICE_AMOUNT),
    RETENTION_INVOICE(3, Constants.RETENTION_INVOICE),
    DEBIT_NOTE_APPROVAL(4,Constants.DEBIT_NOTE_APPROVAL),
    DEBIT_NOTE_RECEIPT(5, Constants.DEBIT_NOTE_RECEIPT),
    CREDIT_NOTE_APPROVAL(6, Constants.CREDIT_NOTE_APPROVAL),
    CREDIT_NOTE_RECEIPT(7, Constants.CREDIT_NOTE_RECEIPT);

    private int value;
    private String name;

    JournalEntriesEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

}
