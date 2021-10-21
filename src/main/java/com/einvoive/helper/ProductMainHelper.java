package com.einvoive.helper;

import com.einvoive.model.ErrorCustom;
import com.einvoive.model.ProductMain;
import com.einvoive.repository.ProductMainRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductMainHelper {

    @Autowired
    ProductMainRepository repository;
    @Autowired
    TranslationHelper translationHelper;
    @Autowired
    MongoOperations mongoOperation;
    Gson gson = new Gson();

    public String save(ProductMain productEnglish, ProductMain productArabic){
        ErrorCustom error = new ErrorCustom();
        String jsonError;
        ProductMain productMain = mongoOperation.findOne(new Query(Criteria.where("productName").is(productEnglish.getProductName())
                .and("companyID").is(productEnglish.getCompanyID())), ProductMain.class);
        if(productMain != null){
            error.setErrorStatus("Error");
            error.setError("Product Name Already Exists");
            jsonError = gson.toJson(error);
            return jsonError;
        }
        else {
            try {
                repository.save(productEnglish);
                saveProductArabic(productEnglish, productArabic);
                return "product saved";
            } catch (Exception ex) {
                error.setErrorStatus("Error");
                error.setError(ex.getMessage());
                jsonError = gson.toJson(error);
                return jsonError;
            }
        }
    }

    private void saveProductArabic(ProductMain productEnglish, ProductMain productArabic){
        translationHelper.mergeAndSave(productEnglish.getProductName(), productArabic.getProductName());
        translationHelper.mergeAndSave(productEnglish.getDescription(), productArabic.getDescription());
    }

    public ProductMain getProductArabic(ProductMain productEnglish) {
        ProductMain productArabic = new ProductMain();
        productArabic.setProductName(translationHelper.getTranslationMain(productEnglish.getProductName()));
        productArabic.setDescription(translationHelper.getTranslationMain(productEnglish.getDescription()));
        return productArabic;
    }

    public String getTopSaledProducts(String companyId){
        List<ProductMain> products = null;
        try {
            Query query = new Query();
            if(!companyId.isEmpty())
                query.limit(10).addCriteria(Criteria.where("companyID").is(companyId));
            products = mongoOperation.find(query, ProductMain.class);
        }catch(Exception ex){
            System.out.println("Error in get Products:"+ ex);
        }
        return gson.toJson(products);
    }

    public String getProducts(String companyId){
        List<List<ProductMain>> productsMain = new ArrayList<>();
        List<ProductMain> productsEnglish = null;
        List<ProductMain> productsArabic = new ArrayList<>();
        try {
            Query query = new Query();
            if(!companyId.isEmpty())
             query.addCriteria(Criteria.where("companyID").is(companyId));
            productsEnglish = mongoOperation.find(query, ProductMain.class);
            for(ProductMain productMainEnglish : productsEnglish)
                productsArabic.add(getProductArabic(productMainEnglish));
            if(productsEnglish != null && productsArabic != null){
                productsMain.add(productsEnglish);
                productsMain.add(productsArabic);
            }
        }catch(Exception ex){
            System.out.println("Error in get Products:"+ ex);
        }
        return gson.toJson(productsMain);
    }

    public String deleteProduct(String productID){
        List<ProductMain> products = mongoOperation.find(new Query(Criteria.where("id").is(productID)), ProductMain.class);
        repository.deleteAll(products);
        return "product deleted";
    }

    public String update(ProductMain productEnglish, ProductMain productArabic) {
        return save(productEnglish, productArabic);
    }

}
