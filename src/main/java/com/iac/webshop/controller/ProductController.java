package com.iac.webshop.controller;
import com.iac.webshop.datasource.ProductDao;
import com.iac.webshop.datasource.ProductDaoImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ProductController {
    private static ProductDao productDao = new ProductDaoImpl();
    @GetMapping("/products")
    public String getProducts(){
        return productDao.getProducts().toString();
    }
    @GetMapping("/products/{productId}")
    public String getProduct(@PathVariable int productId){
        return productDao.getProduct(productId).toString();
    }
    @GetMapping("/categories")
    public String getCategories(){
        return productDao.getCategories().toString();
    }
    @GetMapping("/categories/{categoryId}")
    public String getProductByCategory(@PathVariable int categoryId){
        return productDao.getProductIdByCategory(categoryId).toString();
    }
}
