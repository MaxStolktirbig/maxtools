package com.dbhelper.tools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.ResultSet;

public class DataTool {
    //edit from http://biercoff.com/nice-and-simple-converter-of-java-resultset-into-jsonarray-or-xml/

    public static JSONObject convertToJSON(ResultSet resultSet, String description)
            throws Exception {
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
}
