package com.iac.webshop;

import com.iac.webshop.controller.ProductController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

//@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class ProductTests {


	private ProductController productController = new ProductController();

	@Test
	void whenGetProducts_thenReturnProductsJsonObject() throws Exception{
		System.out.println();
		String jsonObject = productController.getProducts();
		System.out.println(jsonObject);
		assert (jsonObject.contains("product"));
	}
	@Test
	void whenGetSingleProduct_thenReturnProductJsonObject() throws Exception{
		assert (productController.getProduct(1).contains("product"));
	}
	@Test
	void whenGetCategories_thenReturnCategoriesJsonObject() throws Exception{
		assert (productController.getCategories().contains("categories"));
	}
	@Test
	void whenGetSingleCategory_thenReturnProductsJsonObject() throws Exception{
		assert (productController.getProductByCategory(1).contains("categoryProducts"));
	}

	@Test
	void whenGetDiscounts_thenReturnDiscountsJsonObject() throws Exception{
		assert (productController.getDiscounts().contains("discounts"));
	}
	@Test
	void whenGetDiscountByProduct_thenReturnSingleDiscountsJsonObject() throws Exception{
		assert (productController.getDiscountByProduct(1).contains("discount-by-product"));
	}
	@Test
	void whenGetProductbyDiscounts_thenReturnProductJsonObject() throws Exception{
		assert (productController.getProductByDiscount(1).contains("product-by-discount"));
	}
}
