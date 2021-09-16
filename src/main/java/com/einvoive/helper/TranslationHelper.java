package com.einvoive.helper;

import com.einvoive.model.Translation;
import com.einvoive.repository.TranslationRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TranslationHelper {

    @Autowired
    TranslationRepository translationRepository;

    @Autowired
    MongoOperations mongoOperation;

    Gson gson = new Gson();

    public String saveTranslation(Translation translation){
        translation.setId(getAvaiablaeId());
        translationRepository.save(translation);
        return "Translation saved";
    }

    public String getAvaiablaeId() {
        Long total = translationRepository.count();
        int count = total.intValue();
        return count+1+"";

    }

    public String getTranslation(String english) {
        List<Translation> translationList = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("english").is(english));
            translationList = mongoOperation.find(query, Translation.class);
        }catch(Exception ex){
            System.out.println("Error in get Translation:"+ ex);
        }
        return gson.toJson(translationList);
    }
}
