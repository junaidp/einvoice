package com.einvoive.authenticator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTOTP {
    private String username;
    private String secretKey;
    private int validationCode;
    private List<Integer> scratchCodes;
}
