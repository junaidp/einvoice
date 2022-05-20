package com.einvoive.helper;

import com.einvoive.model.*;
import com.einvoive.repository.ContractRepository;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ContractHelper {
    @Autowired
    MongoOperations mongoOperation;

    Gson gson = new Gson();

    @Autowired
    LogsHelper logsHelper;

    private final Logger logger = LoggerFactory.getLogger(LineItemHelper.class);

    @Autowired
    ContractRepository contractRepository;

    @Autowired
    ContractItemHelper contractItemHelper;

    public String save(Contract contract){
        ErrorCustom error = new ErrorCustom();
        String jsonError;
        try {
            contractRepository.save(contract);
            for (ContractItem contractItem : contract.getContractItems()) {
                contractItemHelper.save(contractItem);
                logsHelper.save(new Logs("Contract Name "+contract.getProjectName(), " Contract description is "+ contract.getProjectDescription()+ " Raised Invoice "+ contractItem.getRaiseInvoice()+" Amount "+contractItem.getAmount()));
            }
            logsHelper.save(new Logs("Contract Name "+contract.getProjectName(), " Contract description is "+ contract.getProjectDescription()));
            return "Contract saved";
        }catch(Exception ex){
            logger.info("Exception in saving contract "+ex.getMessage());
            error.setErrorStatus("Error");
            error.setError(ex.getMessage());
            jsonError = gson.toJson(error);
            return jsonError;
        }
    }

    public String deleteContract(String id){
        Contract contract = mongoOperation.findById(id, Contract.class);
        contractRepository.delete(contract);
        contractItemHelper.deletecContractItems(id);
        logsHelper.save(new Logs("Delete Contract "+contract.getProjectName(), "Contract delection: "+contract.getProjectName()+ ", "+contract.getProjectDescription()+", "+contract.getProjectManager()+" will be deleted"));
        logger.info("Contract deleted "+contract.getProjectName());
        return "Contract deleted";
    }

    public String updateContract(Contract contract) {
        deleteContract(contract.getId());
        logger.info("Contract updated "+contract.getProjectName());
        logsHelper.save(new Logs("Contract updated "+contract.getProjectName(), "Contract updated "+contract.getProjectName()+", "+contract.getProjectDescription()+" will be deleted and saved for updation"));
        return save(contract);
    }

    public String getContractByID(String id){
        Contract contract = null;
        try {
            contract = mongoOperation.findById(id, Contract.class);
        }catch(Exception ex){
            logger.info("Error in get Contract:"+ ex.getMessage());
            System.out.println("Error in get Contract:"+ ex.getMessage());
        }
        return gson.toJson(contract);
    }

    public String getContractsByCompanyID(String companyID){
        List<Contract> contractList = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("companyID").is(companyID));
            contractList = mongoOperation.find(query, Contract.class);
//            for(Contract invoice : contractList) {
//                contractItemHelper.getLineItems(invoice.getId());
//                invoice.setLineItemList(lineItemHelper.getLineItems());
//            }
        }catch(Exception ex){
            logger.info("Error in get contractList:"+ ex.getMessage());
            System.out.println("Error in get contractList:"+ ex.getMessage());
        }
        return gson.toJson(contractList);
    }

}
