package com.example.buysell.unittest.entity;

import com.example.buysell.models.Image;
import com.example.buysell.models.Product;
import com.example.buysell.models.User;
import com.example.buysell.models.enums.Role;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

import static com.example.buysell.models.enums.Role.ROLE_USER;

public class UserEntityTest {
    Image image = new Image(1L,"testImage", "origin",5L,"image/jpg",true,"test".getBytes(),null);

    private final Set<Role> roles = new HashSet<>();

    private final List<Product> products = new ArrayList<>();

    private final LocalDateTime dateCreate = LocalDateTime.now();

    private final User user = new User(1L,"test@mail.ru", "123", "testUser", true, image,"active", "123", roles,products,dateCreate);

    @BeforeEach
    public void addRoles(){
        roles.add(ROLE_USER);

        Product product1 = new Product();
        product1.setId(1L);
        Product product2 = new Product();
        product2.setId(2L);
        products.add(product1);
        products.add(product2);
    }

    @Test
    public void setAndGetIdUserTest(){
        Assert.assertEquals(1L, user.getId().longValue());
        user.setId(2L);
        Assert.assertEquals(2L, user.getId().longValue());
    }

    @Test
    public void setAndGetEmailUserTest(){
        Assert.assertEquals("test@mail.ru", user.getEmail());
        user.setEmail("test2@mail.ru");
        Assert.assertEquals("test2@mail.ru", user.getEmail());
    }

    @Test
    public void setAndGetPhoneNumberUserTest(){
        Assert.assertEquals("123", user.getPhoneNumber());
        user.setPhoneNumber("999");
        Assert.assertEquals("999", user.getPhoneNumber());
    }

    @Test
    public void setAndGetNameUserTest(){
        Assert.assertEquals("testUser", user.getName());
        user.setName("testUserSet");
        Assert.assertEquals("testUserSet", user.getName());
    }

    @Test
    public void setAndGetActiveUserTest(){
        Assert.assertEquals(true, user.isActive());
        user.setActive(false);
        Assert.assertEquals(false, user.isActive());
    }
    @Test
    public void setAndGetImageUserTest(){
        Assert.assertEquals(1L, user.getAvatar().getId().longValue());
        Image image1 = new Image(2L,"testImage", "origin",5L,"image/jpg",true,"test".getBytes(),null);
        user.setAvatar(image1);
        Assert.assertEquals(2L, user.getAvatar().getId().longValue());
    }

    @Test
    public void setAndGetActiveCodeUserTest(){
        Assert.assertEquals("active", user.getActivationCode());
        user.setActivationCode("activeSet");
        Assert.assertEquals("activeSet", user.getActivationCode());
    }

    @Test
    public void setAndGetPasswordUserTest(){
        Assert.assertEquals("123", user.getPassword());
        user.setPassword("321");
        Assert.assertEquals("321", user.getPassword());
    }

    @Test
    public void setAndGetRolesUserTest(){

        Assert.assertEquals("[ROLE_USER]", user.getRoles().toString());
        roles.clear();
        roles.add(Role.ROLE_ADMIN);
        Assert.assertEquals("[ROLE_ADMIN]", user.getRoles().toString());
    }

    @Test
    public void setAndGetProductsUserTest() {

        Assert.assertEquals(2, user.getProducts().size());
        Assert.assertEquals(1L, user.getProducts().get(0).getId().longValue());
        Assert.assertEquals(2L, user.getProducts().get(1).getId().longValue());

        Product product3 = new Product();
        product3.setId(3L);
        products.add(product3);

        Assert.assertEquals(3, user.getProducts().size());
        Assert.assertEquals(3L, user.getProducts().get(2).getId().longValue());
    }

    @Test
    public void setAndGetCreateDateUserTest() {

        Assert.assertEquals(dateCreate, user.getDateOfCreate());

        user.setDateOfCreate(LocalDateTime.parse("2023-04-18T22:56:49.610415"));

        Assert.assertEquals(LocalDateTime.parse("2023-04-18T22:56:49.610415"), user.getDateOfCreate());
    }
}
