package com.einvoive.helper;

import com.einvoive.model.ContractItem;
import com.einvoive.model.Invoice;
import com.einvoive.model.LineItem;
import com.einvoive.model.Logs;
import com.einvoive.repository.ContractItemRepository;
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
public class ContractItemHelper {
    @Autowired
    MongoOperations mongoOperation;

    Gson gson = new Gson();

    @Autowired
    LogsHelper logsHelper;

    private final Logger logger = LoggerFactory.getLogger(LineItemHelper.class);

    @Autowired
    ContractItemRepository contractItemRepository;

    public String save(ContractItem contractItem) {
        try {
            contractItemRepository.save(contractItem);
            return "contract item saved";
        } catch (Exception ex) {
            logger.info("contract item not saved " + ex.getMessage());
            return "contract item Not saved" + ex.getMessage();
        }
    }

    public String deletecContractItems(String contractID){
        List<ContractItem> contractItemList = mongoOperation.find(new Query(Criteria.where("contractID").is(contractID)), ContractItem.class);
        contractItemRepository.deleteAll(contractItemList);
        logsHelper.save(new Logs("Contract Items deleted with ID "+contractID, "All Contract Items related to this ID "+contractID+" has been deleted"));
        logger.info("Contract Items deleted with ID "+contractID);
        return "Contract Items deleted";
    }

}
