package com.einvoive.authenticator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidateCodeDto {
    private Integer code;
    private String username;
}

