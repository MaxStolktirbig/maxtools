package com.iac.webshop.datasource;

import com.iac.webshop.datatools.DataTool;

import com.iac.webshop.datatools.DataTool;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ProductDaoImpl implements ProductDao {
    private DataConnectionPool dataConnectionPool = DataConnectionPool.getInstance();
    private DataTool dataTool = new DataTool();

    private JSONObject query(String query, String description){
        try {
            Connection connection = dataConnectionPool.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            dataConnectionPool.releaseConnection(connection);
            return dataTool.convertToJSON(resultSet, description);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject getProducts(){
        return query("select * from products", "products");
    }
    public JSONObject getProduct(int productId){
        return query("select * from products where product_id = "+productId, "product");
    }
    public JSONObject getCategories() {
        return query("select * from categories", "categories");
    }
    public JSONObject getProductIdByCategory(int categoryId) {
        return query("select product_id from products where category_id = "+ categoryId, "categoryProducts");
    }
}
