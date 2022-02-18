package com.einvoive.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import com.google.cloud.translate.Detection;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.Translation;
import org.springframework.beans.factory.annotation.Autowired;

public class Translator {

     public static String translate(String text){
        //Translate translate = TranslateOptions.getDefaultInstance().getService();
       // Detection detection = translate.detect(mysteriousText);
       // String detectedLanguage = detection.getLanguage();
        //Translate translate = TranslateOptions.newBuilder().setApiKey("AIzaSyDsgz0Tuiv67MafA43HMjQSYbQwFwJKy8o/oG+vLhreqx\\nVbmo78LGceq7HZD+RrlV4qohMF+3MM6OKDBzWSyfimahjoFoMVExdGhDcbIg3j3w\\n/oqCEJVoHTOucIqDNmLuxdCkHUyRSYPHEvolHmpQf/AoqvBr+nB9on/S3jS5qgtt\\npW8NCF7Kfz+gr11EA6DZoK3BkAjqiCkQnHxPDpBLpgEavx0aMr2TssBwTmHmaHZQ\\nGuoir9SmW/sjXJhnBlOiU1j4NTpAHI3LMM/AnGLvPu/4z+ENlo44+/GOyJjdjWYy\\nP6W3/wW7E/raDl6LqQ4Krg+P/O0K1qPUS7MWF8CdfnDrNe4hOtPxJXNkk1MuFViv\\nbWOm4rjVAgMBAAECggEAXr/Edekwg+EmnTkaIq7IC/d9DOrY0UHFeEbK3IitBH8t\\nv0OJCZwKvbAXs+TjqtidwHXc32S4yjzjqvu7jHeap8NWAUSqjQG4EfdhFa5wWHpG\\nRSQpE3Ew4YOENLeXCeRBSEFbUUnB0YHsWSR4HZbEQNTpHWv9ViLqxZwr1N30Fv4H\\nXj3IVJpU+S1zJxcTLUd6uxPvbZ7v7Ml/0zNwMAK0wiS/EM5535B8atRZfYSOUB+V\\ndxnD8NQeb1Pqm4KAFneSnwzgDOKxgSTqzRs7ox7dQ+lftlvD3rUP4FC4u9ad/DG6\\nuULOeLIA/uJDu1ZYaYVjXYlBa7+SDln8pcr1nI+ixwKBgQD5+esNRUOg8dhZQqAy\\n6ScNIscBAw+0GTzLxGNS2VGQ2CmhCs6e+XpzWdoFAnLg2jEjAxAqD9f/+NUc2OgF\\nazsQ51W9qp6OZKH0zFCuRmf6TDals4lg3ZmoznyGK8dvWSr7XE9VzpNWsj9kwche\\n5+pcBoDSApbQWW6+G/RCcOx8lwKBgQDrYdzBhAhGbqtnEqBSYNDdMbnW2GqKnIEP\\nX+w5iNs6TEtdo6qz8nCGUpifhnA19krNpjxMAcY1SRXgHJyjlMDlxLBa3fb93dFO\\nBnOLHAnqMJHwKAa+gEKRcL1x3hKOSHNPySnr2cBGTQYB6uqFw4AOcYE0uGGNXRdC\\no5XnUGpncwKBgQCa67AowUxwanHSn9/4rVZuZac/kU4iJ6HuBwAnUFeklNL342K6\\nU5L3+i+L+0CG6bKaKEPUTY1oLialgigJc7Nffn881Ij/Zo6Y0CcQWsIF9UXI28PA\\nLJYWcCHsZSVsdK6WqVbhruzRRhbTsuUjLEeqUL11afVdmo9vcpOmuaVWcQKBgB8E\\nWk6101FUrAl9DttMHrnHH7IL+p4hWXBpN3utaRYZj6TbOFdWDzXkFHCRCqBRwDGx\\nQijR4wiKhPNvUcTMiU69yG9w6Gczn60kdH1USIovtEttOtZHmH4J2Sz0EoXlTiyp\\nWXjWzNt2st/10MmnpWxB0MDdux0hszr4y3wbwKm5AoGAaCGkMOqn0tr6V743WQvW\\nt40Bk/0EVz0dxSW8jk03BNTFnF9ZtI3A13Sbz7XNIkq9A2NC5v7RPHC88vPgX5Yd\\nMaLuWdHAvOVciEH+i205gzq7PvFY1mJX7M3CjbIRiv3g4ofuqFLgA8n4B+f3s0uo\\n5aELU/U3NryJEmNnQeOsvF8=").build().getService();
        try {
            Translate translate = TranslateOptions.newBuilder().setApiKey("AIzaSyDsgz0Tuiv67MafA43HMjQSYbQwFwJKy8o").build().getService();
            Translation translation = translate.translate(
                    text,
                    TranslateOption.sourceLanguage("en"),
                    TranslateOption.targetLanguage("ar"));
            return translation.getTranslatedText();
        }catch(Exception ex){
//            sendEmailToTeam("Exception occur in Translation API", "Translation api is not working:" + ex.getMessage());
            System.out.println("Translation api is not working:" + ex.getMessage());
            return "Translation api is not working:" + ex.getMessage();
        }
      }

    public static String translate(String langFrom, String langTo, String text) throws IOException {
        String urlStr = "https://script.google.com/macros/s/AKfycbwLDDEF68X1zY4sWHGgUDA96Dh5ggozGKw_7H2azxTGqWow4g6M97TR1JUcqUyBhKKqug/exec" +
                "?q=" + URLEncoder.encode(text, "UTF-8") +
                "&target=" + langTo +
                "&source=" + langFrom;
        URL url = new URL(urlStr);
        StringBuilder response = new StringBuilder();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

   public static String getTranslation(String english)  {
         if(english == null)
             return "";
         else {
             String arabic = null;
             try {
                 //  arabic = translate("en", "ar", english);
                 arabic = translate(english);
                 System.out.println(arabic);
             } catch (Exception ex) {
                 arabic = "Error in translation: " + ex.getMessage();
//            System.out.println("Error in translation:" + ex);
             }
             return arabic;
         }
    }

}
