package com.iac.webshop;

import mx.helper.tools.IAC2020.AuthenticationHelper;
import mx.helper.tools.IAC2020.security.models.JWTToken;
import mx.helper.tools.communication.Logger;
import mx.helper.tools.communication.SystemMessage;

public class test {
//    private static String authUrl = "http://82.169.175.69:8080/authService/getClaims/";
//    private static String loginUrl = "http://82.169.175.69:8080/authService/login/";
    private static String authUrl = "http://localhost:8089/authService/getClaims/";
    private static String loginUrl = "http://localhost:8089/authService/login/";

    public static void main(String[] args){
        Logger.log("logging test!",   System.getProperty("user.dir"));
        Logger.log(new Exception("test"),   System.getProperty("user.dir"));
    }
}
