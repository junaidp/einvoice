package com.einvoive.util;

import com.einvoive.helper.JournalEntriesHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class JournalEntriesHandler {

    @Autowired
    JournalEntriesHelper journalEntriesHelper;
    private String entryStage;
    private String invoiceNo;

    public JournalEntriesHandler(String entryStage, String invoiceNo) {
        this.entryStage = entryStage;
        this.invoiceNo = invoiceNo;
        switch (1){
            case 1:
                journalEntriesHelper.getByInvoiceNo(invoiceNo);
                break;
        }
    }


}
