package com.einvoive.util;

import com.einvoive.helper.VatHelper;
import com.einvoive.model.Company;
import com.einvoive.model.User;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.posadskiy.currencyconverter.CurrencyConverter;
import com.posadskiy.currencyconverter.config.ConfigBuilder;
import com.posadskiy.currencyconverter.enums.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import org.springframework.util.StringUtils;


import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.Random;


public class Utility {

    private static String hexIP = null;
    private static final java.security.SecureRandom SEEDER = new java.security.SecureRandom();
    private Logger logger = LoggerFactory.getLogger(Utility.class);

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
                byte serverIP[] = localInetAddress.getAddress();
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
        guid.append(tmpBuffer.toString());
        guid.append(hexFormat(node, 8));
        return guid.toString();
    }
    private static int getInt(byte bytes[])
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
    };

}

