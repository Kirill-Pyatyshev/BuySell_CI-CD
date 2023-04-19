package com.example.buysell.unittest.entity;

import com.example.buysell.models.Image;
import com.example.buysell.models.Product;
import com.example.buysell.models.User;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductEntityTest {

    List<Image> listImages = new ArrayList<>();

    User user = new User();
    User user1 = new User();

    private final LocalDateTime dateCreate = LocalDateTime.now();

    final private Product product = new Product(1L, "Product", "TestProduct", 5000, "Москва", listImages, 1L, user, dateCreate);

    @BeforeEach
    public void addRoles() {
        Image image1 = new Image();
        image1.setId(1L);

        Image image2 = new Image();
        image2.setId(2L);

        listImages.add(image1);
        listImages.add(image2);

        user.setId(1L);
        user1.setId(2L);
    }

    @Test
    public void setAndGetIdProductTest() {
        Assert.assertEquals(1L, product.getId().longValue());
        product.setId(2L);
        Assert.assertEquals(2L, product.getId().longValue());
    }

    @Test
    public void setAndGetTitleProductTest() {
        Assert.assertEquals("Product", product.getTitle());
        product.setTitle("ProductTest");
        Assert.assertEquals("ProductTest", product.getTitle());
    }

    @Test
    public void setAndGetDescriptionProductTest() {
        Assert.assertEquals("TestProduct", product.getDescription());
        product.setDescription("TestProductSet");
        Assert.assertEquals("TestProductSet", product.getDescription());
    }

    @Test
    public void setAndGetPriceProductTest() {
        Assert.assertEquals(5000, product.getPrice());
        product.setPrice(10000);
        Assert.assertEquals(10000, product.getPrice());
    }

    @Test
    public void setAndGetCityProductTest() {
        Assert.assertEquals("Москва", product.getCity());
        product.setCity("Киров");
        Assert.assertEquals("Киров", product.getCity());
    }

    @Test
    public void setAndGetImagesProductTest() {
        Assert.assertEquals(2, product.getImages().size());
        Assert.assertEquals(1L, product.getImages().get(0).getId().longValue());
        Assert.assertEquals(2L, product.getImages().get(1).getId().longValue());

        Image image3 = new Image();
        image3.setId(3L);
        listImages.add(image3);

        Assert.assertEquals(3, product.getImages().size());
        Assert.assertEquals(3L, product.getImages().get(2).getId().longValue());
    }

    @Test
    public void setAndGetPrevImageProductTest() {
        Assert.assertEquals(1L, product.getPreviewImageId().longValue());
        product.setPreviewImageId(2L);
        Assert.assertEquals(2L, product.getPreviewImageId().longValue());
    }

    @Test
    public void setAndGetUserProductTest() {
        Assert.assertEquals(1L, product.getUser().getId().longValue());
        product.setUser(user1);
        Assert.assertEquals(2L, product.getUser().getId().longValue());
    }
    @Test
    public void setAndGetCreateDateUserTest() {
        Assert.assertEquals(dateCreate, product.getDateOfCreated());
        product.setDateOfCreated(LocalDateTime.parse("2023-04-18T22:56:49.610415"));
        Assert.assertEquals(LocalDateTime.parse("2023-04-18T22:56:49.610415"), product.getDateOfCreated());
    }
}
