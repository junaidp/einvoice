package com.einvoive.helper;

import com.einvoive.model.ErrorCustom;
import com.einvoive.model.Translation;
import com.einvoive.repository.TranslationRepository;
import com.einvoive.util.Translator;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
public class TranslationHelper {

    @Autowired
    TranslationRepository translationRepository;

    @Autowired
    MongoOperations mongoOperation;

    Gson gson = new Gson();

    public String saveTranslation(Translation translation){
        ErrorCustom error = new ErrorCustom();
        String jsonError;
        Translation translation1 = mongoOperation.findOne(new Query(Criteria.where("english").is(translation.getEnglish())), Translation.class);
        if(translation1 == null){
//            translation.setId(getAvaiablaeId());
            translationRepository.save(translation);
            return "Translation saved";
        }
        else{
            error.setErrorStatus("Error");
            error.setError("English text already saved");
            jsonError = gson.toJson(error);
            return jsonError;
        }
    }

    public String updateTranslation(Translation translation){
        deleteTranslation(translation.getEnglish());
        return saveTranslation(translation);
    }

    public String getAvaiablaeId() {
        Long total = translationRepository.count();
        int count = total.intValue();
        return count+1+"";

    }

    public String mergeAndSave(String english, String arabic){
        Translation translationSaved = mongoOperation.findOne(new Query(Criteria.where("english").is(english)), Translation.class);
       if(translationSaved != null){
           translationSaved.setArabic(arabic);
           return updateTranslation(translationSaved);
       }
       else {
           Translation translationSave = new Translation();
           translationSave.setEnglish(english);
           translationSave.setArabic(arabic);
           return saveTranslation(translationSave);
       }
    }

    public String getTranslation(String english) {
        ErrorCustom error = new ErrorCustom();
        String jsonError;
        Translation translation = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("english").is(english));
            translation = mongoOperation.findOne(query, Translation.class);
            if(translation.getArabic() != null)
                return gson.toJson(translation);
            else {
                error.setErrorStatus("Error");
                error.setError("Translation not found");
                jsonError = gson.toJson(error);
                return jsonError;
            }
        }catch(Exception ex){
            System.out.println("Error in get Translation:"+ ex);
            error.setErrorStatus("Error");
            error.setError(ex.getMessage());
            jsonError = gson.toJson(error);
            return jsonError;
        }
    }

    public String checkAndGetTranslation(String english){
        if(!(english == null) && !english.isEmpty())
             return getTranslationMain(english);
        else
            return "";
    }

    public String getTranslationMain(String english) {
        Translation translation = null;
        try {
            translation = mongoOperation.findOne(new Query(Criteria.where("english").is(english)), Translation.class);
        }catch (Exception exception) {
            System.out.println("No Translation found for text "+ english +
                    "having Exception: "+ exception.getMessage());
        }
        if(translation == null){
            translation = new Translation();
            System.out.println("No Translation saved: Assigning Translation through API");
            translation.setArabic(Translator.getTranslation(english));
            translation.setEnglish(english);
            //saveing Translation
            translationRepository.save(translation);
        }
        return translation.getArabic();
    }

    public String deleteTranslation(String english) {
        Translation translation = mongoOperation.findOne(new Query(Criteria.where("english").is(english)), Translation.class);
        translationRepository.delete(translation);
        return translation.getEnglish() + "deleted";
    }
}
