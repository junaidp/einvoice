package com.einvoive;

import com.einvoive.helper.InvoiceHelper;
import com.einvoive.helper.LoginHelper;
import com.einvoive.helper.ProductMainHelper;
import com.einvoive.helper.UserHelper;
import com.einvoive.model.Invoice;
import com.einvoive.model.Login;
import com.einvoive.model.User;
import com.einvoive.util.EmailSender;
import com.einvoive.util.Translator;
import com.einvoive.util.Utility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
    LoginHelper loginHelper;

    @Autowired
    UserHelper userHelper;

    @Test
    void contextLoads() {
    }

    //@Test
    void updateUserForToken(){
        User userTest = new User();
        userTest.setUserId("Zohaib1");
        userTest.setLoginToken("1234");
        userHelper.updateUserForToken(userTest);
    }

   // @Test
       void login(){
        Login login = new Login();
        login.setEmail("testcase@email.com");
        loginHelper.signIn(login);
    }

    @Test
    void getProductsTranslation(){
        String test = productMainHelper.getProductsNames("DarAlMaysan");
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
