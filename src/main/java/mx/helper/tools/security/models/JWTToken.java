package mx.helper.tools.security.models;


public class JWTToken {
    private String jwtToken;

    public JWTToken(){
    }
    public JWTToken(String jwtToken){
        this.jwtToken = jwtToken;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}