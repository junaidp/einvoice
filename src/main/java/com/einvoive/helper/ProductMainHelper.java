package com.einvoive.helper;

import com.einvoive.model.ErrorCustom;
import com.einvoive.model.Logs;
import com.einvoive.model.ProductMain;
import com.einvoive.model.User;
import com.einvoive.repository.ProductMainRepository;
import com.einvoive.util.Translator;

import com.einvoive.util.Utility;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Component
public class ProductMainHelper {
    private Logger logger = LoggerFactory.getLogger(ProductMainHelper.class);
    @Autowired
    ProductMainRepository repository;
    @Autowired
    TranslationHelper translationHelper;
    @Autowired
    LogsHelper logsHelper;
    @Autowired
    MongoOperations mongoOperation;
    @Autowired
    CompanyHelper companyHelper;
    @Autowired
    UserHelper userHelper;
    Gson gson = new Gson();

//    public String save(ProductMain productEnglish, ProductMain productArabic){
//        ErrorCustom error = new ErrorCustom();
//        String jsonError;
//        ProductMain productMain = mongoOperation.findOne(new Query(Criteria.where("productName").is(productEnglish.getProductName())
//                .and("companyID").is(productEnglish.getCompanyID())), ProductMain.class);
//        if(productMain != null){
//            logger.info("Product Name: "+ productEnglish.getProductName()+" is already exists");
//            error.setErrorStatus("Error");
//            error.setError("Product Name: "+ productEnglish.getProductName()+" is already exists");
//            jsonError = gson.toJson(error);
//            return jsonError;
//        }
//        else {
//            try {
//                repository.save(productEnglish);
//                logsHelper.save(new Logs(productEnglish.getProductName()+" product saved", "Added product for company "+Utility.getCompanyName(productEnglish.getCompanyID(), mongoOperation)+" User "+ Utility.getUserName(productEnglish.getUserId(), mongoOperation)+" Price "+productEnglish.getPrice()+" Assigned charts of account "+productEnglish.getAssignedChartofAccounts()+" Description "+productEnglish.getDescription()+" code "+productEnglish.getCode()));
//                if(!(productArabic == null))
//                    saveProductArabic(productEnglish, productArabic);
//                return "product saved";
//            } catch (Exception ex) {
//                logger.info("Exception in product saved "+ex.getMessage());
//                error.setErrorStatus("Error");
//                error.setError(ex.getMessage());
//                jsonError = gson.toJson(error);
//                return jsonError;
//            }
//        }
//    }

    public String save(ProductMain productMain) {
        ErrorCustom error = new ErrorCustom();
        String jsonError;
        ProductMain productSaved = mongoOperation.findOne(new Query(Criteria.where("productName").is(productMain.getProductName())
                .and("companyID").is(productMain.getCompanyID())), ProductMain.class);
        if (productSaved != null) {
            logger.info("Product Name: " + productMain.getProductName() + " is already exists");
            error.setErrorStatus("Error");
            error.setError("Product Name: " + productMain.getProductName() + " is already exists");
            jsonError = gson.toJson(error);
            return jsonError;
        } else {
            try {
                repository.save(productMain);
                logsHelper.save(new Logs(productMain.getProductName() + " product saved", "Added product for company " + Utility.getCompanyName(productMain.getCompanyID(), mongoOperation) + " User " + Utility.getUserName(productMain.getUserId(), mongoOperation) + " Price " + productMain.getPrice() + " Assigned charts of account " + productMain.getAssignedChartofAccounts() + " Description " + productMain.getDescription() + " code " + productMain.getCode()));
                return "Product "+productMain.getProductName()+" has been added successfully";
            } catch (Exception ex) {
                logger.info("Exception in product saved " + ex.getMessage());
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
            logger.info("Error in get Products:"+ ex.getMessage());
            System.out.println("Error in get Products:"+ ex.getMessage());
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
            logger.info("Error in get Products:"+ ex.getMessage());
            System.out.println("Error in get Products:"+ ex.getMessage());
        }
        return gson.toJson(products);
    }
//test
    public String getProductsNamesTest(String companyId){
        List<ProductMain> productsLanguagesList = new ArrayList<>();
//        List<ProductMain> productsEnglish = new ArrayList<>();
//        List<ProductMain> productsArabic = new ArrayList<>();
        try {
            Query query = new Query(Criteria.where("companyID").is(companyId));
            productsLanguagesList = mongoOperation.find(query, ProductMain.class);
            for(ProductMain productMainEnglish : productsLanguagesList) {
                if (productMainEnglish.getNameArabic() == null && !Utility.isNumeric(productMainEnglish.getProductName())) {
                    productMainEnglish.setNameArabic(translationHelper.getTranslationTest(productMainEnglish.getProductName()));
//                    if(!productMainEnglish.getProductName().equalsIgnoreCase(productMainEnglish.getNameArabic()))
//                        updateProductNameArabic(productMainEnglish);
                    if(productMainEnglish.getDescription() != null)
                        productMainEnglish.setDescriptionArabic(translationHelper.getTranslationTest(productMainEnglish.getDescription()));
//                    if(!productMainEnglish.getDescription().equalsIgnoreCase(productMainEnglish.getDescriptionArabic())
//                    &&!productMainEnglish.getProductName().equalsIgnoreCase(productMainEnglish.getNameArabic()))
                    updateProductDescriptionArabic(productMainEnglish);
                }
            }
            setProductNameWithArabic(productsLanguagesList);
        }catch(Exception ex){
            logger.info("Error in get Products Names Test:"+ ex.getMessage());
            System.out.println("Error in get Products Names Test:"+ ex.getMessage());
        }
        return gson.toJson(productsLanguagesList);
    }

    private void setProductNameWithArabic(List<ProductMain> productsLanguagesList) {
        for(ProductMain productMain: productsLanguagesList){
            if(productMain.getNameArabic() != null && !productMain.getNameArabic().isEmpty())
               productMain.setProductName(productMain.getProductName()+" - "+productMain.getNameArabic());
        }
    }

    //Product Name in both languages
    public String getProductsNames(String companyId){
        List<ProductMain> productMainList = new ArrayList<>();
        try {
            productMainList = mongoOperation.find(new Query(Criteria.where("companyID").is(companyId)), ProductMain.class);
            setProductNameWithArabic(productMainList);
            return gson.toJson(productMainList);
        }catch(Exception ex){
            logger.info("Error in get Products:"+ ex.getMessage());
            System.out.println("Error in get Products:"+ ex.getMessage());
        }
        return "Sorry! No Product found";
    }

    public String getProducts(String companyId){
        List<ProductMain> productMainList = new ArrayList<>();
        try {
            Query query = new Query(Criteria.where("companyID").is(companyId));
            productMainList = mongoOperation.find(query, ProductMain.class);
            return gson.toJson(productMainList);
        }catch(Exception ex){
            logger.info("Error in get Products:"+ ex.getMessage());
            System.out.println("Error in get Products:"+ ex.getMessage());
        }
        return "Sorry! No Product found";
    }

    //search products
    public String searchAllProducts(String companyId, String name){
        List<List<ProductMain>> productsMain = new ArrayList<>();
        List<ProductMain> productsEnglish = null;
        List<ProductMain> productsArabic = new ArrayList<>();
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("productName").regex("^"+name).and("companyID").is(companyId));
            productsEnglish = mongoOperation.find(query, ProductMain.class);
            //for(ProductMain productMainEnglish : productsEnglish)
               // productsArabic.add(getProductArabic(productMainEnglish));
            if(productsEnglish != null ){
                productsMain.add(productsEnglish);
               // productsMain.add(productsArabic);//
            }
//            productsMain.add(productsEnglish); //Only English to test
        }catch(Exception ex){
            logger.info("Error in get Products:"+ ex.getMessage());
            System.out.println("Error in get Products:"+ ex.getMessage());
        }
        return gson.toJson(productsMain);
    }

    public String deleteProduct(String productID){
        ProductMain product = mongoOperation.findOne(new Query(Criteria.where("id").is(productID)), ProductMain.class);
        repository.delete(product);
//        translationHelper.deleteTranslation(product.getProductName());
//        translationHelper.deleteTranslation(product.getDescription());
        logger.info("Product deleted:"+product.getProductName());
        return "product deleted";
    }

    public String deleteProductTest(String companyID){
        List<ProductMain> product = mongoOperation.find(new Query(Criteria.where("companyID").is(companyID)
                .and("assignedChartofAccounts").is(null)), ProductMain.class);
        repository.deleteAll(product);
        return "products deleted";
    }

    public String updateProductNameArabic(ProductMain productEnglish) {
        deleteProduct(productEnglish.getId());
        repository.save(productEnglish);
//        Update update = new Update();
//        update.set("nameArabic", productEnglish.getNameArabic());
//        mongoOperation.updateFirst(new Query(Criteria.where("nameArabic").is(productEnglish.getNameArabic())), update, ProductMain.class);
        System.out.println("Name: "+productEnglish.getProductName()+" has Arabic: "+productEnglish.getNameArabic());
        return "Arabic Name of Product has been Updated";
    }

    private String updateProductDescriptionArabic(ProductMain productEnglish) {
        deleteProduct(productEnglish.getId());
        repository.save(productEnglish);
//        Update update = new Update();
//        update.set("descriptionArabic", productEnglish.getNameArabic());
//        mongoOperation.updateFirst(new Query(Criteria.where("descriptionArabic").is(productEnglish.getDescriptionArabic())), update, ProductMain.class);
//        System.out.println("Name: "+productEnglish.getDescription()+" has Arabic: "+productEnglish.getDescriptionArabic());
        return "Arabic Description of Product has been Updated";
    }

    public String update(ProductMain productMain) {
        logger.info("Product updation "+productMain.getProductName());
        deleteProduct(productMain.getId());
        repository.save(productMain);
        return "Product "+ productMain.getProductName()+" has been updated";
    }

    public String updateCompanyID(String companyId){
        List<ProductMain> productMainList = new ArrayList<>();
        try {
            productMainList = mongoOperation.find(new Query(Criteria.where("companyID").is(companyId)), ProductMain.class);
           Update update = new Update();
            return gson.toJson(productMainList);
        }catch(Exception ex){
            logger.info("Error in get Products:"+ ex.getMessage());
            System.out.println("Error in get Products:"+ ex.getMessage());
        }
        return "Sorry! No Product found";
    }



}
