package mx.helper.tools.IAC2020;

import mx.helper.tools.SystemMessage;
import mx.helper.tools.IAC2020.security.models.JWTToken;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationHelper {

//    https://www.chillyfacts.com/java-send-http-getpost-request-and-read-json-response/

    public Map<String,Object> getClaimsFromToken(JWTToken jwt, String url){
        Map<String,Object> map = new HashMap<>();
        map.put("responseCode",null);
        try{
            URL obj = new URL(url);
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

    public String getRole(JWTToken jwtToken, String url){
        return (String) getClaimsFromToken(jwtToken, url).get("Role");
    }

    public ResponseEntity adminCheck(JWTToken jwtToken, String url, Object responseBody){
        if(jwtToken.getJwtToken() != null) {
            String role = getRole(jwtToken, url);
            if (role.equals("admin")) {
                return ResponseEntity.ok().body(responseBody);
            } else {
                return ResponseEntity.status(401).build();
            }
        }
        return ResponseEntity.status(400).build();
    }

    public JWTToken login(String url, String username, String password){
        JWTToken jwtToken = new JWTToken();
        try {
            //create connection and set headers
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();


            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            //write body
            String jsonInputString = "{ \"username\":\""+username+"\", \"password\":\""+password+"\"}";
            con.setRequestProperty("Body", jsonInputString);
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
