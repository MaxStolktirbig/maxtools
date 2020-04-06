package com.iac.webshop;

import mx.helper.tools.IAC2020.AuthenticationHelper;
import mx.helper.tools.IAC2020.security.models.JWTToken;
import mx.helper.tools.communication.Logger;
import mx.helper.tools.communication.SystemMessage;

import javax.sound.midi.Soundbank;
import java.sql.SQLOutput;

public class test {
    public static void main(String[] args) {
//        AuthenticationHelper.authUrl = "http://localhost:8089/authService/getClaims/";
//        AuthenticationHelper.loginUrl = "http://localhost:8089/authService/login/";
        AuthenticationHelper.authUrl = "http://82.169.175.69:8080/authService/getClaims/";
        AuthenticationHelper.loginUrl = "http://82.169.175.69:8080/authService/login/";
        SystemMessage.stacktraceEnabled = true;

        AuthenticationHelper authenticationHelper = new AuthenticationHelper();
        JWTToken jwtToken1 = authenticationHelper.login("kcurrell0", "password");
        if (jwtToken1.getJwtToken() == null) {
            SystemMessage.exceptionMessage(new NullPointerException());
        } else {
            System.out.println(jwtToken1.getJwtToken());
        }
        SystemMessage.exceptionMessage(new Exception("exception!"));
        String role = authenticationHelper.getRole(jwtToken1);
        if (role == null) {
            SystemMessage.exceptionMessage(new NullPointerException("role cannot be null"));
        } else {
            System.out.println(role);
        }
    }
}
