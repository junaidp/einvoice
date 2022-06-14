package com.einvoive.util;

import com.einvoive.constants.Constants;
import com.einvoive.helper.InvoiceXMLHelper;
import com.einvoive.model.Company;
import com.einvoive.model.User;
import com.einvoive.zatcaxml.InvoiceXML;
import com.posadskiy.currencyconverter.CurrencyConverter;
import com.posadskiy.currencyconverter.config.ConfigBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class Utility {
    @Autowired
    MongoOperations mongoOperation;
    @Autowired
    EmailSender emailSender;
    private static String hexIP = null;
    private static final java.security.SecureRandom SEEDER = new java.security.SecureRandom();
    private final Logger logger = LoggerFactory.getLogger(Utility.class);

    private static String encryptBase64(InvoiceXML invoiceXML){
        // Getting encoder
        Base64.Encoder encoder = Base64.getUrlEncoder();
        return encoder.encodeToString(invoiceXML.toString().getBytes());
    }

    private static byte[] getSHA(String text) throws NoSuchAlgorithmException
    {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(text.getBytes(StandardCharsets.UTF_8));
    }

    public static String encrypt(String text) throws NoSuchAlgorithmException {
        byte[] hash = getSHA(text);
        BigInteger number = new BigInteger(1, hash);
        StringBuilder hexString = new StringBuilder(number.toString(16));
        while (hexString.length() < 32)
        {
            hexString.insert(0, '0');
        }
        return hexString.toString();
    }

    public static final String generate(Object o)
    {
        StringBuffer tmpBuffer = new StringBuffer(16);
        if (hexIP == null)
        {
            java.net.InetAddress localInetAddress = null;
            try
            {
                // get the inet address
                localInetAddress = java.net.InetAddress.getLocalHost();
                byte[] serverIP = localInetAddress.getAddress();
                hexIP = hexFormat(getInt(serverIP), 8);
            }
            catch (java.net.UnknownHostException uhe)
            {
                uhe.printStackTrace();
                hexIP="";
            }
        }
        String hashcode = hexFormat(System.identityHashCode(o), 8);
        tmpBuffer.append(hexIP);
        tmpBuffer.append(hashcode);
        long timeNow = System.currentTimeMillis();
        int timeLow = (int) timeNow & 0xFFFFFFFF;
        int node = SEEDER.nextInt();
        StringBuffer guid = new StringBuffer(32);
        guid.append(hexFormat(timeLow, 8));
        guid.append(tmpBuffer);
        guid.append(hexFormat(node, 8));
        return guid.toString();
    }
    private static int getInt(byte[] bytes)
    {
        int i = 0;
        int j = 24;
        for (int k = 0; j >= 0; k++)
        {
            int l = bytes[k] & 0xff;
            i += l << j;
            j -= 8;
        }
        return i;
    }
    private static String hexFormat(int i, int j)
    {
        String s = Integer.toHexString(i);
        return padHex(s, j) + s;
    }
    private static String padHex(String s, int i)
    {
        StringBuffer tmpBuffer = new StringBuffer();
        if (s.length() < i)
        {
            for (int j = 0; j < i - s.length(); j++)
            {
                tmpBuffer.append('0');
            }
        }
        return tmpBuffer.toString();
    }

    public static String getRandomNumber(){
        Random rand = new Random();
        String random = String.valueOf(rand.nextInt(10000));
        return random;
    }

    public static String getUserName(String id, MongoOperations mongoOperation){
        // use findById for this case
        User user = mongoOperation.findById(id, User.class);
        //User user = mongoOperation.findOne(new Query(Criteria.where("id").is(id)), User.class);
        if(user == null)
            user = mongoOperation.findOne(new Query(Criteria.where("userId").is(id)), User.class);
        if(user != null)
            return user.getName();
        else
            return "No user found";
    }

    public static String getCompanyName(String companyID, MongoOperations mongoOperation) {
        Company company = mongoOperation.findOne(new Query(Criteria.where("companyID").is(companyID)), Company.class);
        if(company != null)
            return company.getCompanyName();
        else
            return "No company found";
    }

    public static int getAttachedNo(String invoiceNo, String format) {
        int num = 1;
        String[] inv = StringUtils.split(invoiceNo, format);
        num = Integer.parseInt(inv[inv.length - 1]);
        return num;
    }

    public static String[] getSplitString(String value) {
        String[] inv = StringUtils.split(value, " ");
        return inv;
    }

    public void sendEmailToTeam(String subject, String message){
        emailSender.sendEmail("junaidp@gmail.com", subject, message);
        emailSender.sendEmail("amoqeet43@gmail.com", subject, message);
        emailSender.sendEmail("mfaheempiracha@gmail.com", subject, message);
        emailSender.sendEmail("abdurrafe7211@gmail.com", subject, message);
        emailSender.sendEmail("adnankhokhar747@gmail.com", subject, message);
    }

    public static boolean isNumeric(String text){
        try{
            int num = Integer.parseInt(text);
            return true;
        }catch(Exception ex){
            return false;
        }
    }

    public String getCurrencyRateSAR(String currency){
        try {

//           final String CURRENCY_CONVERTER_API_API_KEY = "a0aed9fcf769b85a287b";
 //           final String CURRENCY_CONVERTER_API_API_KEY = "61f698eab958d16a271515be";
            final String CURRENCY_CONVERTER_API_API_KEY = "147a4faa04c94fd4aa4b0f10e83e8273"; //PURCHASED
            CurrencyConverter converter = new CurrencyConverter(
                    new ConfigBuilder()
                            .currencyConverterApiApiKey(CURRENCY_CONVERTER_API_API_KEY)
                            .build()
            );

            Double usdToEuroRate = converter.rate(currency, "SAR");
            return usdToEuroRate.toString();
        }catch(Exception ex){
            logger.warn("Error in getting currency rate:" + ex.getMessage());
            return "";
        }
    }

    public static String getDateHH(String date) throws ParseException {
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
//        return dateFormat.parse(date).toString();
        return date;
    }

    public static void updateInvoiceXML() throws IOException {
        Path path = Paths.get(Constants.INVOICE_XML_PATH);
        List<String> fileContent = new ArrayList<>(Files.readAllLines(path, StandardCharsets.UTF_8));
        for (int i = 0; i < fileContent.size(); i++) {
            if (fileContent.get(i).equals("<Invoice>")) {
                fileContent.set(i, Constants.INVOICE_XML_ATTRIBUTES);
                break;
            }
        }
        Files.write(path, fileContent, StandardCharsets.UTF_8);
    }

    public boolean emailExists(String email){
        Company company = mongoOperation.findOne(new Query(Criteria.where("email").is(email)), Company.class);
        if(company != null)
            return true;
        User user = mongoOperation.findOne(new Query(Criteria.where("email").is(email)), User.class);
       if(user != null)
           return true;
       else
            return false;
    }

}

