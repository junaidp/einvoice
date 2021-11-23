package com.einvoive;

import com.einvoive.helper.InvoiceHelper;
import com.einvoive.model.Invoice;
import com.einvoive.model.InvoiceWithFile;
import com.einvoive.util.Translator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
//import com.google.cloud.translate.*;

@SpringBootTest
class EinvoivingApplicationTests {

    @Autowired
    InvoiceHelper invoiceHelper;

    @Test
    void contextLoads() {
    }

    @Test
    void saveInvoice(){
        InvoiceWithFile inv = new InvoiceWithFile();
        inv.getInvoice().setCompanyID("hyphen");
       //TODO  invoiceHelper.save(inv);
        System.out.println("");
    }


    //USE THIS
      @Test
    void translate()  {
        try{
            String translation = Translator.translate("en", "ar", "Hello");
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
