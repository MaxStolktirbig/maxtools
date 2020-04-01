package com.iac.webshop.datasource;

import org.json.JSONObject;


public interface ProductDao {
    JSONObject getProducts();
    JSONObject getProduct(int productId);
    JSONObject getCategories();
    JSONObject getProductIdByCategory(int categoryId);
    JSONObject getDiscounts();
    JSONObject getDiscountByProduct(int productId);
    JSONObject getProductByDiscount(int discountId);
}
