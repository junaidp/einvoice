package com.einvoive.helper;

import com.einvoive.model.ErrorCustom;
import com.einvoive.model.Location;
import com.einvoive.model.Vat;
import com.einvoive.repository.LocationRepository;
import com.einvoive.repository.VatRepository;
import com.google.gson.Gson;
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

    Gson gson = new Gson();

    public String save(Location location) {
            ErrorCustom error = new ErrorCustom();
            String jsonError;
            Location loc = mongoOperation.findOne(new Query(Criteria.where("name").is(location.getLocationName())), Location.class);
            if (loc != null) {
                try {
                    repository.save(location);
                    return "Location saved";
                } catch (Exception ex) {
                    error.setErrorStatus("error");
                    error.setError(ex.getMessage());
                    jsonError = gson.toJson(error);
                    return jsonError;
                }
            } else {
                error.setErrorStatus("Error");
                error.setError("Location Name Already Exists");
                jsonError = gson.toJson(error);
                return jsonError;
            }
        }

    public String update(Location location){
        try {
            repository.save(location);
        }catch(Exception ex){
            return "location Not updated"+ ex;
        }
        return "location update";
    }

    public String deleteLocation(String id){
        List<Location> locationList = mongoOperation.find(new Query(Criteria.where("id").is(id)), Location.class);
        repository.deleteAll(locationList);
        return "Location deleted";
    }

    public String getAllLocations(String companyId){
        List<Location> locations = null;
        try {
            locations = mongoOperation.find(new Query(Criteria.where("companyID").is(companyId)), Location.class);
        }catch(Exception ex){
            System.out.println("Error in get locations:"+ ex);
        }
        return gson.toJson(locations);
    }
}
