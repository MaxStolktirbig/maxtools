package mx.helper.tools.security.models;

public class JsonResponse {
    private String responseString;

    public JsonResponse(String responseString) {
        this.responseString = responseString;
    }

    public String getResponseString() {
        return responseString;
    }
}
