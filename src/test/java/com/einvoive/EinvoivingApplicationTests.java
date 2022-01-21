package com.einvoive;

import com.einvoive.helper.*;
import com.einvoive.model.*;
import com.einvoive.repository.CompanyRepository;
import com.einvoive.repository.UserRepository;
import com.einvoive.util.EmailSender;
import com.einvoive.util.Translator;
import com.einvoive.util.Utility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.security.NoSuchAlgorithmException;
import java.util.List;
//import com.google.cloud.translate.*;

@SpringBootTest
class EinvoivingApplicationTests {

    @Autowired
    InvoiceHelper invoiceHelper;

    @Autowired
    EmailSender emailSender;

    @Autowired
    ProductMainHelper productMainHelper;

    @Autowired
    CompanyHelper companyHelper;

    @Autowired
    UserHelper userHelper;

    @Autowired
    LoginHelper loginHelper;

    @Autowired
    MongoOperations mongoOperation;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LogsHelper logsHelper;

    @Autowired
    CustomerHelper customerHelper;

//    @Test
    void contextLoads() {
    }

    //@Test
    void updateUserForToken(){
        User userTest = new User();
        userTest.setUserId("Zohaib1");
        userTest.setLoginToken("1234");
        userHelper.updateUserForToken(userTest);
    }

//    @Test
       void login() throws NoSuchAlgorithmException {
        Login login = new Login();
        login.setEmail("testcase@email.com");
        loginHelper.signIn(login);
    }

//    @Test
    void getProductsTranslation(){
        String test = productMainHelper.getProductsNames("DarAlMaysan");
        System.out.println(test);
    }

//    @Test
    void saveLogs(){
        logsHelper.save(new Logs("Test Case", "Testing its working"));
    }

//    @Test
    void saveProduct(){
        ProductMain productMainEnglish = new ProductMain();
        productMainEnglish.setProductName("Test12");
        productMainEnglish.setDescription("Testing through backend");
        productMainEnglish.setUserId("123");
        productMainEnglish.setCompanyID("hype");
        productMainEnglish.setCode("1122");
        productMainEnglish.setPrice("1000");
        productMainEnglish.setAssignedChartofAccounts("Pie chart");
        ProductMain productMainArabic = new ProductMain();
        productMainArabic.setProductName(Translator.translate(productMainEnglish.getProductName()));
        productMainArabic.setDescription(Translator.translate(productMainEnglish.getDescription()));
        productMainHelper.save(productMainEnglish, productMainArabic);
    }

    @Test
    void addingMissingLocationsInvoices(){
        List<Invoice> invoiceList = mongoOperation.findAll(Invoice.class);
        for(Invoice invoice:invoiceList){
            if(invoice.getLocation() == null)
                continue;
            if(invoice.getLocation().isEmpty()){
                User user = mongoOperation.findById(invoice.getUserId(), User.class);
                Update update = new Update();
                try {
                    update.set("location", user.getLocation());
                    mongoOperation.updateFirst(new Query(Criteria.where("id").is(invoice.getId())), update, Invoice.class);
                }catch (Exception exception) {
                    System.out.println(exception.getMessage());
                }
            }
        }
    }

//    @Test
    void passwordHash() throws NoSuchAlgorithmException {
        List<Company> companyList = companyRepository.findAll();
        for(Company company:companyList) {
            Company company1 = new Company();
            company1 = company;
            companyRepository.delete(company);
            company1.setPassword(Utility.encrypt(company.getPassword()));
            companyHelper.saveCompany(company);
            List<User> userList = mongoOperation.find(new Query(Criteria.where("companyID").is(company.getCompanyID())), User.class);
           for (User user:userList){
              User user1 = new User();
              user1 = user;
               user1.setPassword(Utility.encrypt(user.getPassword()));
              userRepository.delete(user);
              userHelper.saveUser(user1, false);
            }
        }

    }

    @Test
            public void getCurrency(){
        Utility util = new Utility();
        util.getCurrencyRateSAR();
    }

//    @Test
    void getInvoicesByUser(){
        System.out.println("Invoices: "+invoiceHelper.getInvoicesByUser("61ab4c8d95d6ba231072305e"));
    }

//    @Test
    void getNextInvoiceNumbersForUsersUnderComapny(){
//        Company company = companyRepository.findUserBycompanyID("FugroSuhaimiLtd.");
        Company company = mongoOperation.findOne(new Query(Criteria.where("companyID").is("FugroSuhaimiLtd.")), Company.class);
        System.out.println(company.getCompanyName());
        List<User> userList = mongoOperation.find(new Query(Criteria.where("companyID").is(company.getCompanyID())), User.class);
        for(User user:userList)
            System.out.println("Next Invoice No for User: "+user.getName()+" is: "+invoiceHelper.getNextInvoiceNoByUserID(user.getId()));
    }

//    @Test
    void getAllCustomers(){
        String test = customerHelper.getAllCustomers("FugroSuhaimiLtd.");
        System.out.println(test);
    }

//    @Test
    void sendEmail(){
        String randomNumber = Utility.getRandomNumber();
        emailSender.sendEmail("junaidp@gmail.com", "Login Token", "email body here"+ randomNumber);
    }

   // @Test
    void saveInvoice(){
        Invoice inv = new Invoice();
        inv.setCompanyID("hyphen");
       //TODO  invoiceHelper.save(inv);
        System.out.println("");
    }


    //USE THIS
//      @Test
    void translate()  {
        try{
           // String translationBk = TranslatorHelper.translate("en", "ar", "Hello");
            String translation = Translator.translate("Hello");

            System.out.println(translation);
          }
        catch(Exception ex)
        {
            System.out.println("Error in translation:" + ex);
        }
    }

    //DONT USE THIS !
   /* @Test
    void translateWithGoogle() {
        Translate translate = TranslateOptions.getDefaultInstance().getService();

        Translation translation = translate.translate("¡Hola Mundo!");
        System.out.printf("Translated Text:\n\t%s\n", translation.getTranslatedText());

    }*/



}
