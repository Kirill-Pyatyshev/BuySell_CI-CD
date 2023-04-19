package com.example.buysell.unittest.controllers;

import com.example.buysell.controller.ProductController;
import com.example.buysell.models.Product;
import com.example.buysell.models.User;
import com.example.buysell.services.ProductService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @Test
    public void productInfoTest() throws Exception {

        Model model = mock(Model.class);
        Product product1 = mock(Product.class);
        product1.setId(1L);

        when(productService.getProductById(1L)).thenReturn(product1);

        Assert.assertNotNull(productController.productInfo(1L, model, null));
        Assert.assertEquals(productController.productInfo(1L, model, null), "product-info");

        verify(productService, Mockito.times(2)).getProductById(1L);
        verify(productService, Mockito.times(2)).getUserByPrincipal(ArgumentMatchers.any());
    }

    @Test
    public void createProductTest() throws Exception {

        Product product1 = mock(Product.class);
        product1.setId(1L);

        Assert.assertNotNull(productController.createProduct(null, null, null, product1, null));
        Assert.assertEquals(productController.createProduct(null, null, null, product1, null), "redirect:/my/products");

        verify(productService, Mockito.times(2)).saveProduct(null, product1, null, null, null);
    }

    @Test
    public void deleteProductTest() throws Exception {

        Product product1 = mock(Product.class);
        product1.setId(1L);

        User user = mock(User.class);
        user.setId(1L);

        when(productService.getUserById(1L)).thenReturn(user);

        Assert.assertNotNull(productController.deleteProduct(1L, 1L));
        Assert.assertEquals(productController.deleteProduct(1L, 1L), "redirect:/my/products");

        verify(productService, Mockito.times(2)).deleteProduct(user, 1L);
        verify(productService, Mockito.times(2)).getUserById(1L);
    }

    @Test
    public void userProductsTest() throws Exception {

        Product product1 = mock(Product.class);
        product1.setId(1L);
        Model model = mock(Model.class);
        Principal principal = mock(Principal.class);

        when(productService.getUserByPrincipal(principal)).thenReturn(new User());

        Assert.assertNotNull(productController.userProducts(principal, model));
        Assert.assertEquals(productController.userProducts(principal, model), "my-products");

        verify(productService, Mockito.times(2)).getUserByPrincipal(principal);
    }

    @Test
    public void productsTest() throws Exception {

        Product product1 = mock(Product.class);
        product1.setId(1L);
        Model model = mock(Model.class);
        Principal principal = mock(Principal.class);

        List<Product> productList = new ArrayList<>();

        when(productService.listProducts("")).thenReturn(productList);
        when(productService.getUserByPrincipal(principal)).thenReturn(new User());

        Assert.assertNotNull(productController.products("", "", principal, model));
        Assert.assertEquals(productController.products("", "", principal, model), "products");

        verify(productService, Mockito.times(2)).getUserByPrincipal(principal);
        verify(productService, Mockito.times(2)).listProducts("");
    }

}
