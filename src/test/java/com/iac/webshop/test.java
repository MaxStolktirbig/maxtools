package com.iac.webshop;

import mx.helper.tools.IAC2020.AuthenticationHelper;
import mx.helper.tools.IAC2020.security.models.JWTToken;

public class test {
//    private static String authUrl = "http://82.169.175.69:8080/authService/getClaims/";
//    private static String loginUrl = "http://82.169.175.69:8080/authService/login/";
    private static String authUrl = "http://localhost:8089/authService/getClaims/";
    private static String loginUrl = "http://localhost:8089/authService/login/";

    public static void main(String[] args){
        AuthenticationHelper authenticationHelper = new AuthenticationHelper();
        JWTToken jwtToken1 = authenticationHelper.login(loginUrl, "kcurrell0", "password");
        System.out.println(jwtToken1.getJwtToken());
        String role = authenticationHelper.getRole(jwtToken1, authUrl);
        System.out.println(role);
    }
}
