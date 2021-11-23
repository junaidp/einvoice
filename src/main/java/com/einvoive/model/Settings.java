package com.einvoive.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Settings {

    @Id
    private String id;
    private String companyID;
    private boolean approvalAsPerAssignedFinancialLimits;
    private boolean approvalAsPerRefortipLine;
    private boolean approvalBasedLocationOfSuperivisorProspectiveOfAmount;
    private boolean anyOneHavingRights;
    private boolean approvalBasedLocationOfSuperivisorAndFinancialLimits;

}
