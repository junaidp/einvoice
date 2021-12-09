package com.einvoive.helper;

import com.einvoive.model.ErrorCustom;
import com.einvoive.model.ProductMain;
import com.einvoive.repository.ProductMainRepository;
import com.einvoive.util.Translator;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

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
            error.setError("Product Name: "+ productEnglish.getProductName()+" is already exists");
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
        if(productEnglish.getProductName() != null && (!productEnglish.getProductName().isEmpty()))
            productArabic.setProductName(translationHelper.getTranslationMain(productEnglish.getProductName()));
        if(productEnglish.getDescription() != null && (!productEnglish.getDescription().isEmpty()))
            productArabic.setDescription(translationHelper.getTranslationMain(productEnglish.getDescription()));
        productArabic.setId(productEnglish.getId());
        return productArabic;
    }

    public ProductMain getProductArabicOnline(ProductMain productEnglish) {
        ProductMain productArabic = new ProductMain();
        productArabic.setProductName(Translator.getTranslation(productEnglish.getProductName()));
        productArabic.setDescription(Translator.getTranslation(productEnglish.getDescription()));
        return productArabic;
    }

//    String url = HOST_MALL_ADMIN + "/productAttribute/category/create";
    //设置头信息
//    HttpHeaders headers = new HttpHeaders();
//    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//    //构造表单参数
//    MultiValueMap<String, String> params= new LinkedMultiValueMap<>();
//    params.add("name", name);
//    HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
//    ResponseEntity<CommonResult> responseEntity = restTemplate.postForEntity(url, requestEntity, CommonResult.class);
//    return responseEntity.getBody();

//    String translate(String english)  {
//        String arabic = null;
//        try{
//            arabic = Translator.translate("en", "ar", english);
////            System.out.println(translation);
//        }
//        catch(Exception ex)
//        {
//            arabic = "Error in translation: " + ex.getMessage();
////            System.out.println("Error in translation:" + ex);
//        }
//        return arabic;
//    }

//    public String getPostResponse(String english){
//        String url = "http://fad2-101-50-88-15.ngrok.io/Translate/translate-text";
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        MultiValueMap<String, String> params= new LinkedMultiValueMap<>();
//        params.add("text", english);
//        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
//        ResponseEntity<Json> responseEntity = null;
//        try {
//            responseEntity = restTemplate.postForEntity(url, requestEntity, Json.class);
//        }
//        catch (Exception ex) {
//            System.out.println(ex.getMessage());
//        }
//        String arabic = responseEntity.getBody().toString();
//        return arabic;
//    }

    //search
    public String searchProducts(String productName){
        List<ProductMain> products = null;
        try {
            productName = "^"+productName;
            Query query = new Query();
            query.addCriteria(Criteria.where("productName").regex(productName));
            products = mongoOperation.find(query, ProductMain.class);
        }catch(Exception ex){
            System.out.println("Error in get Products:"+ ex);
        }
        return gson.toJson(products);
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

    //Product Name in both languages
    public String getProductsNames(String companyId){
        List<ProductMain> productsLanguagesList = new ArrayList<>();
        List<ProductMain> productsEnglish = null;
        List<ProductMain> productsArabic = new ArrayList<>();
        try {
            Query query = new Query(Criteria.where("companyID").is(companyId));
            productsEnglish = mongoOperation.find(query, ProductMain.class);
            for(ProductMain productMainEnglish : productsEnglish)
                productsArabic.add(getProductArabic(productMainEnglish));
            for(int i=0; i<productsEnglish.size(); i++){
                productsLanguagesList.add(productsEnglish.get(i));
                productsLanguagesList.get(i).setProductName(productsLanguagesList.get(i).getProductName()+" - "+productsArabic.get(i).getProductName());
                productsLanguagesList.get(i).setDescription(productsLanguagesList.get(i).getDescription()+" - "+productsArabic.get(i).getDescription());
            }
        }catch(Exception ex){
            System.out.println("Error in get Products Names:"+ ex);
        }
        return gson.toJson(productsEnglish);
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
//            productsMain.add(productsEnglish); //Only English to test
        }catch(Exception ex){
            System.out.println("Error in get Products:"+ ex);
        }
        return gson.toJson(productsMain);
    }

    public String deleteProduct(String productID){
        ProductMain product = mongoOperation.findOne(new Query(Criteria.where("id").is(productID)), ProductMain.class);
        repository.delete(product);
        translationHelper.deleteTranslation(product.getProductName());
        translationHelper.deleteTranslation(product.getDescription());
        return "product deleted";
    }

    public String update(ProductMain productEnglish, ProductMain productArabic) {
        repository.save(productEnglish);
        saveProductArabic(productEnglish, productArabic);
        return "product updated";
    }

}
