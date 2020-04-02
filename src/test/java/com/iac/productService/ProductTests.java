package com.iac.productService;

import com.iac.productService.controller.ProductController;
import com.iac.productService.datasource.DataConnectionPool;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest
class ProductTests {


	private ProductController productController = new ProductController();

	@Test
	void whenGetProducts_thenReturnProductsJsonObject() throws Exception{
		assertTrue (productController.getProducts().contains("product"));
	}
	@Test
	void whenGetSingleProduct_thenReturnProductJsonObject() throws Exception{
		assertTrue (productController.getProduct(1).contains("product"));
	}
	@Test
	void whenGetCategories_thenReturnCategoriesJsonObject() throws Exception{
		assertTrue (productController.getCategories().contains("categories"));
	}
	@Test
	void whenGetSingleCategory_thenReturnProductsJsonObject() throws Exception{
		assertTrue (productController.getProductByCategory(1).contains("categoryProducts"));
		assertTrue(productController.getProductByCategory(-1).contains("categoryProducts"));
	}

	@Test
	void whenGetDiscounts_thenReturnDiscountsJsonObject() throws Exception{
		assertTrue (productController.getDiscounts().contains("discounts"));
	}
	@Test
	void whenGetDiscountByProduct_thenReturnSingleDiscountsJsonObject() throws Exception{
		assertTrue (productController.getDiscountByProduct(1).contains("discount-by-product"));
		assertTrue (productController.getDiscountByProduct(-1).contains("discount-by-product"));
	}
	@Test
	void whenGetProductbyDiscounts_thenReturnProductJsonObject() throws Exception{
		assertTrue (productController.getProductByDiscount(1).contains("product-by-discount"));
		assertTrue(productController.getProductByDiscount(-1).contains("product-by-discount"));
	}
}
