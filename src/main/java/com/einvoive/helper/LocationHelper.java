package com.einvoive.helper;

import com.einvoive.model.Location;
import com.einvoive.model.Vat;
import com.einvoive.repository.LocationRepository;
import com.einvoive.repository.VatRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
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

    public String save(Location location){
        try {

            repository.save(location);
        }catch(Exception ex){
            return "location Not saved"+ ex;
        }
        return "location saved";
    }

    public String getAllLocations(){
        List<Location> locations = null;
        try {
            Query query = new Query();
            locations = mongoOperation.find(query, Location.class);
        }catch(Exception ex){
            System.out.println("Error in get locations:"+ ex);
        }
        return gson.toJson(locations);
    }
}
