package com.einvoive.helper;

import com.einvoive.model.ErrorCustom;
import com.einvoive.model.Location;
import com.einvoive.model.Logs;
import com.einvoive.model.Vat;
import com.einvoive.repository.LocationRepository;
import com.einvoive.repository.VatRepository;
import com.einvoive.util.Utility;
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
public class LocationHelper {

    @Autowired
    LocationRepository repository;
    @Autowired
    MongoOperations mongoOperation;
    @Autowired
    CompanyHelper companyHelper;
    @Autowired
    LogsHelper logsHelper;
    Gson gson = new Gson();
    private Logger logger = LoggerFactory.getLogger(LocationHelper.class);

    public String save(Location location) {
            ErrorCustom error = new ErrorCustom();
            String jsonError;
            Location loc = mongoOperation.findOne(new Query(Criteria.where("locationName").is(location.getLocationName())
                    .and("companyID").is(location.getCompanyID())), Location.class);
            if (loc == null) {
                try {
                    repository.save(location);
                    logsHelper.save(new Logs("Saving Location Under "+ Utility.getCompanyName(location.getCompanyID(), mongoOperation), "Location "+location.getLocationName()+" has been saved under "+Utility.getCompanyName(location.getCompanyID(), mongoOperation)));
                    return "Location saved";
                } catch (Exception ex) {
                    logger.info("Exception in save Location "+ex.getMessage());
                    error.setErrorStatus("Error");
                    error.setError(ex.getMessage());
                    jsonError = gson.toJson(error);
                    return jsonError;
                }
            } else {
                logger.info("Location Name Already Exists");
                error.setErrorStatus("Error");
                error.setError("Location Name Already Exists");
                jsonError = gson.toJson(error);
                return jsonError;
            }
        }

    public String update(Location location){
        logger.info("Location update "+location.getLocationName());
        deleteLocation(location.getId());
        return save(location);
    }

    public String deleteLocation(String id){
        List<Location> locationList = mongoOperation.find(new Query(Criteria.where("id").is(id)), Location.class);
        repository.deleteAll(locationList);
        logger.info("Location deleted "+locationList.get(0).getLocationName());
        return "Location deleted";
    }

    public String getAllLocations(String companyId){
        List<Location> locations = null;
        try {
            locations = mongoOperation.find(new Query(Criteria.where("companyID").is(companyId)), Location.class);
        }catch(Exception ex){
            logger.info("Error in get locations:"+ ex.getMessage());
            System.out.println("Error in get locations:"+ ex.getMessage());
        }
        return gson.toJson(locations);
    }
}
