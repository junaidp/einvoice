package com.einvoive.authenticator;

import com.einvoive.helper.UserHelper;
import com.warrenstrange.googleauth.ICredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CredentialRepository implements ICredentialRepository {

    private final Map<String, UserTOTP> usersKeys = new HashMap<String, UserTOTP>() ;
    @Autowired
    UserHelper userHelper;

    @Override
    public String getSecretKey(String userName) {
        try {
            UserTOTP userTOTP = userHelper.getUserAuthenticatorInfo(userName);
            return userTOTP.getSecretKey();
        }
        catch(Exception ex){
            return ex.getMessage();
        }
    }

    @Override
    public void saveUserCredentials(String userName,
                                    String secretKey,
                                    int validationCode,
                                    List<Integer> scratchCodes) {
        UserTOTP userTOTP = new UserTOTP(userName, secretKey, validationCode, scratchCodes);
        usersKeys.put(userName, userTOTP);
        userHelper.saveAuthenticatorInfo(userTOTP);
    }

    public UserTOTP getUser(String username) {
        return usersKeys.get(username);
    }


}
