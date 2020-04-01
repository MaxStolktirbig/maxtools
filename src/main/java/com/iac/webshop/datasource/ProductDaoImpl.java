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
            JSONObject returnJSON = dataTool.convertToJSON(resultSet, description);
            dataConnectionPool.releaseConnection(connection);
            return returnJSON;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject getProducts(){
        return query("select * from product", "products");
    }
    public JSONObject getProduct(int productId){
        return query("select * from product where productId = "+productId, "product");
    }
    public JSONObject getCategories() {
        return query("select * from category", "categories");
    }
    public JSONObject getProductIdByCategory(int categoryId) {
        return query("select productId from product where categoryId = "+ categoryId, "categoryProducts");
    }

    public JSONObject getDiscounts(){
        return query("select * from discount", "discounts");
    }

    public JSONObject getDiscountByProduct(int productId){
        return query("select * from discount where productId = "+productId, "discount-by-product");
    }

    public JSONObject getProductByDiscount(int discountId){
        return query("select * from product p, discount d where d.productid = p.productid and d.discountid = "+discountId, "product-by-discount");
    }
}
