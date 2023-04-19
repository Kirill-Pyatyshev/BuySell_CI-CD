package com.example.buysell.unittest.repo;

import com.example.buysell.models.Product;
import com.example.buysell.models.User;
import com.example.buysell.repositories.ProductRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProductRepositoryTest {

    @MockBean
    private ProductRepository productRepository;

    @Test
    public void saveProductRepoTest() {
        Product product = new Product();
        Product product1 = new Product();

        productRepository.save(product);
        productRepository.save(product1);

        List<Product> productList = new ArrayList<>();

        productList.add(product1);
        productList.add(product1);

        when(productRepository.findAll()).thenReturn(productList);

        Assert.assertNotNull(productRepository.findAll());
        Assert.assertEquals(2, productRepository.findAll().size());
    }

    @Test
    public void productFindByEmailRepoTest() {
        Product product = new Product();
        product.setTitle("test1");
        Product product1 = new Product();
        product1.setTitle("test1");
        Product product3 = new Product();
        product1.setTitle("test2");

        productRepository.save(product);
        productRepository.save(product1);
        productRepository.save(product3);

        List<Product> productList = new ArrayList<>();

        productList.add(product);
        productList.add(product1);

        when(productRepository.findByTitle("test1")).thenReturn(productList);

        Assert.assertNotNull(productRepository.findByTitle("test1"));
        Assert.assertEquals(2, productRepository.findByTitle("test1").size());
    }

    @Test
    public void productFindByIdRepoTest() {
        Product product = new Product();
        product.setId(1L);

        productRepository.save(product);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Assert.assertNotNull(productRepository.findById(1L));
        Assert.assertEquals(Optional.of(product), productRepository.findById(1L));
    }

    @Test
    public void productFindByUserRepoTest() {
        User user = mock(User.class);
        user.setId(1L);

        Product product = new Product();
        product.setId(1L);
        product.setUser(user);

        productRepository.save(product);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Assert.assertNotNull(productRepository.findById(1L));
        Assert.assertEquals(Optional.of(product), productRepository.findById(1L));
    }
}
