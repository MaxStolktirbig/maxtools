package com.iac.webshop;

import com.iac.webshop.controller.ProductController;
import com.iac.webshop.datasource.ProductDao;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(ProductController.class)
class ProductTests {
	@Autowired
	private MockMvc mvc;

	@MockBean
	private ProductDao productDao;

	@Test
	void whenGetProducts_thenReturnProductsJsonObject() throws Exception{
		assert (productDao.getProducts().has("products"));
	}
	@Test
	void whenGetSingleProduct_thenReturnProductJsonObject() throws Exception{
		assert (productDao.getProduct(1).has("product"));
	}
	@Test
	void whenGetCategories_thenReturnCategoriesJsonObject() throws Exception{
		assert (productDao.getCategories().has("categories"));
	}
	@Test
	void whenGetSingleCategory_thenReturnProductsJsonObject() throws Exception{
		assert (productDao.getProductIdByCategory(1).has("categoryProducts"));
	}
}
