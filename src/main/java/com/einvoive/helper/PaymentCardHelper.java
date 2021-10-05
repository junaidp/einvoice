package com.einvoive.helper;

import com.einvoive.model.Company;
import com.einvoive.model.ErrorCustom;
import com.einvoive.model.PaymentCard;
import com.einvoive.repository.PaymentCardRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaymentCardHelper {

    @Autowired
    PaymentCardRepository paymentCardRepository;

    @Autowired
    MongoOperations mongoOperation;

    Gson gson = new Gson();

    public String savePaymentCard(PaymentCard paymentCard){
        String msg = validationBeforeSave(paymentCard);
        ErrorCustom error = new ErrorCustom();
        String jsonError;
        if(msg == null || msg.isEmpty()) {
            try {
                paymentCardRepository.save(paymentCard);
                return "Payment Card Saved";
            } catch (Exception ex) {
                error.setErrorStatus("Error");
                error.setError(ex.getMessage());
                jsonError = gson.toJson(error);
                return jsonError;
            }
        }
        else{
            error.setErrorStatus("Error");
            error.setError(msg+"--Already Exists");
            jsonError = gson.toJson(error);
            return jsonError;
        }
    }

    private String validationBeforeSave(PaymentCard paymentCard) {
        String msg = null;
        String msg1 = "";
        PaymentCard paymentCard1 = mongoOperation.findOne(new Query(Criteria.where("cardNo").is(paymentCard.getCardNo())), PaymentCard.class);
        if(paymentCard1 != null )
            msg1 = "--Card No";
        msg = msg1 ;
        return msg;
    }
    public String getPaymentCards(String userId){
        List<PaymentCard> paymentCardList = null;
        try {
            Query query = new Query();
            if(!userId.isEmpty())
                query.addCriteria(Criteria.where("userId").is(userId));
            paymentCardList = mongoOperation.find(query, PaymentCard.class);
        }catch(Exception ex){
            System.out.println("Error in get paymentCard:"+ ex);
        }
        return gson.toJson(paymentCardList);
    }

    public String updatePaymentCard(PaymentCard paymentCard) {
        return savePaymentCard(paymentCard);
    }

}
