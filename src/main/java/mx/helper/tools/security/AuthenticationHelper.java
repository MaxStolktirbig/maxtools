package mx.helper.tools.security;

import mx.helper.tools.communication.SystemMessage;
import mx.helper.tools.security.models.JWTToken;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationHelper {
    public static String authUrl;
    public static String loginUrl;
    public static String validateUrl;

    public static boolean validateToken(JWTToken jwt){
        try{
            URL obj = new URL(validateUrl);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestProperty("Authorization", "Bearer " + jwt.getJwtToken());
            if(con.getResponseCode() == 200){
                return true;
            }
        }catch (IOException e){
            SystemMessage.errorMessage("Malformed URL, check authentication url");
            SystemMessage.exceptionMessage(e);
        }
        return false;
    }

//    https://www.chillyfacts.com/java-send-http-getpost-request-and-read-json-response/
    public static Map<String,Object> getClaimsFromToken(JWTToken jwt){
        Map<String,Object> map = new HashMap<>();
        map.put("responseCode",null);
        try{
            URL obj = new URL(authUrl);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestProperty("Authorization", "Bearer "+ jwt.getJwtToken());

            if (con.getResponseCode() == 406){
                map.put("responseCode",406);
                return map;
            }

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            //Read JSON response and print
            JSONObject myResponse = new JSONObject(response.toString());
            map.put("responseCode",200);
            map.put("Username", myResponse.get("Username"));
            map.put("Role",myResponse.get("Role"));
            return map;
        }catch (IOException e){
            SystemMessage.exceptionMessage(e);
            return map;
        }
    }

    public static String getRole(JWTToken jwtToken){
        String role = null;
        if(getClaimsFromToken(jwtToken).get("Role") != null){
            role = (String) getClaimsFromToken(jwtToken).get("Role");
        }
        return role;
    }

    public static ResponseEntity adminCheck(JWTToken jwtToken, String responseBody){
        if(jwtToken.getJwtToken() != null && validateToken(jwtToken)) {
            String role = getRole(jwtToken);
            if(role != null) {
                if (role.equals("admin")) {
                    return ResponseEntity.ok().body(responseBody);
                } else {
                    return ResponseEntity.status(401).build();
                }
            }
        }
        return ResponseEntity.status(400).build();
    }

    public static JWTToken login(String username, String password){
        JWTToken jwtToken = null;
        try {
            //create connection and set headers
            URL obj = new URL(loginUrl);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            //write body
            String jsonInputString = "{ \"username\":\""+username+"\", \"password\":\""+password+"\"}";
            OutputStream outputStream = con.getOutputStream();
            byte[] input = jsonInputString.getBytes("utf-8");
            outputStream.write(input);
            outputStream.close();
            //read result
            if(con.getResponseCode() == 200) {
                String result;
                BufferedInputStream bis = new BufferedInputStream(con.getInputStream());
                ByteArrayOutputStream buf = new ByteArrayOutputStream();
                int result2 = bis.read();
                while (result2 != -1) {
                    buf.write((byte) result2);
                    result2 = bis.read();
                }
                result = buf.toString();
                JSONObject jsonObject = new JSONObject(result);
                jwtToken = new JWTToken();
                jwtToken.setJwtToken(jsonObject.getString("jwt"));
            } else if (con.getResponseCode() == 400){
                SystemMessage.errorMessage("User name/password combination not found");
            } else{
                SystemMessage.errorMessage("Something went wrong while trying to log in");
            }
        } catch (IOException e){
                SystemMessage.exceptionMessage(e);
        }
        return jwtToken;
    }
}
