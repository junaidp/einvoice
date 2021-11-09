package com.einvoive.helper;

import com.einvoive.constants.AccountReceivableConstant;
import com.google.gson.Gson;

public class ConstantClassesHelper {

    private Gson gson = new Gson();

    public String getAccountReceivableConstant(){
        return gson.toJson(AccountReceivableConstant.class);
    }

}
