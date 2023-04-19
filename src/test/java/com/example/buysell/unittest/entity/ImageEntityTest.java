package com.example.buysell.unittest.entity;

import com.example.buysell.models.Image;
import com.example.buysell.models.Product;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class ImageEntityTest {

    Product product = new Product();

    byte[] aByte1 = "test".getBytes();

    byte[] aByte2 = "testSet".getBytes();

    Image image = new Image(1L, "file1", "original", 100L, "image/jpeg", true, aByte1, product);

    @BeforeEach
    public void addRoles() {
        product.setId(1L);
    }

    @Test
    public void setAndGetIdImageTest() {
        Assert.assertEquals(1L, image.getId().longValue());
        image.setId(2L);
        Assert.assertEquals(2L, image.getId().longValue());
    }

    @Test
    public void setAndGetNameImageTest() {
        Assert.assertEquals("file1", image.getName());
        image.setName("file2");
        Assert.assertEquals("file2", image.getName());
    }

    @Test
    public void setAndGetOriginalNameImageTest() {
        Assert.assertEquals("original", image.getOriginalFileName());
        image.setOriginalFileName("originalSet");
        Assert.assertEquals("originalSet", image.getOriginalFileName());
    }

    @Test
    public void setAndGetSizeImageTest() {
        Assert.assertEquals(100L, image.getSize().longValue());
        image.setSize(200L);
        Assert.assertEquals(200L, image.getSize().longValue());
    }

    @Test
    public void setAndGetContentTypeImageTest() {
        Assert.assertEquals("image/jpeg", image.getContentType());
        image.setContentType("image/img");
        Assert.assertEquals("image/img", image.getContentType());
    }

    @Test
    public void setAndGetPreviewImageTest() {
        Assert.assertEquals(true, image.isPreviewImage());
        image.setPreviewImage(false);
        Assert.assertEquals(false, image.isPreviewImage());
    }

    @Test
    public void setAndGetBytesImageTest() {
        Assert.assertEquals(aByte1, image.getBytes());
        image.setBytes(aByte2);
        Assert.assertEquals(aByte2, image.getBytes());
    }

    @Test
    public void setAndGetProductImageTest() {
        Assert.assertEquals(1L, image.getProduct().getId().longValue());

        Product product1 = new Product();
        product1.setId(2L);

        image.setProduct(product1);

        Assert.assertEquals(2L, image.getProduct().getId().longValue());
    }

}
