package mx.helper.tools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;


public class DataTool {
    //edit from http://biercoff.com/nice-and-simple-converter-of-java-resultset-into-jsonarray-or-xml/

    public static JSONObject convertResultsetToJSON(ResultSet resultSet, String description)
            throws SQLException {
        JSONObject returnObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        while (resultSet.next()) {
            int total_rows = resultSet.getMetaData().getColumnCount();
            JSONObject obj = new JSONObject();
            for (int i = 0; i < total_rows; i++) {
                obj.put(resultSet.getMetaData().getColumnLabel(i + 1)
                        .toLowerCase(), resultSet.getObject(i + 1));
            }
            jsonArray.put(obj);
        }
        returnObject.put(description, jsonArray);
        return returnObject;
    }

    public static JSONObject convertJsonFileToJSONObject(File jsonFile)throws IOException {
        String jsonString = "";
        Scanner jsonReader = new Scanner(jsonFile);
        while (jsonReader.hasNextLine()) {
            jsonString += jsonReader.nextLine();
        }
        jsonReader.close();
        JSONObject returnJson = new JSONObject(jsonString);
        return returnJson;

    }
}
