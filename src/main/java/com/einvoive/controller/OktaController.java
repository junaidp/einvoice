//package com.einvoive.controller;
//
//import com.einvoive.helper.LoginHelper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.oauth2.core.oidc.user.OidcUser;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@CrossOrigin(origins = "*", maxAge = 3600)
//public class OktaController {
//    @Autowired
//    LoginHelper loginHelper;
//
//        @GetMapping("/loginOkta")
//        String loginOkta(@AuthenticationPrincipal OidcUser oidcUser) {
//            try {
//                return loginHelper.signInOkta(oidcUser.getEmail());
//            } catch (Exception ex) {
//                return "Error in okta signing:" + ex.getMessage();
//            }
//        }
//
//}
