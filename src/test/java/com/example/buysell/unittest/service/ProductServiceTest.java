package com.example.buysell.unittest.service;

import com.example.buysell.models.Image;
import com.example.buysell.models.Product;
import com.example.buysell.models.User;
import com.example.buysell.repositories.ProductRepository;
import com.example.buysell.repositories.UserRepository;
import com.example.buysell.services.ProductService;
import com.example.buysell.services.UserService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest

public class ProductServiceTest {
    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    public void createProductTest() throws IOException {

        Product product = new Product();
        product.setCity("Москва");
        File file = new File("/Users/pyatyshev/IdeaProjects/Buysell-Lesson8/src/main/resources/static/images/624224_0.jpg");
        String name = file.getName();
        String originalFileName = file.getName();
        String contentType = "image/jpg";
        byte[] content = Files.readAllBytes(file.toPath());

        MultipartFile result = new MockMultipartFile(name, originalFileName, contentType, content);

        when(productRepository.save(product)).thenReturn(product);
        productService.saveProduct(null, product,result,result,result);

        Assert.assertEquals(product.getCity(),"Москва");

        Mockito.verify(productRepository, Mockito.times(2)).save(product);
    }

    @Test
    public void createProductFailTest() throws IOException {

        Product product = new Product();
        product.setCity("Москва");
        product.setPrice(1000);

        File file = new File("/Users/pyatyshev/IdeaProjects/Buysell-Lesson8/src/main/resources/static/images/624224_0.jpg");
        String name = file.getName();
        String originalFileName = file.getName();
        String contentType = "image/jpg";
        byte[] content = Files.readAllBytes(file.toPath());

        MultipartFile result = new MockMultipartFile(name, originalFileName, contentType, content);

        when(productRepository.save(product)).thenReturn(product);
        productService.saveProduct(null, product,result,result,result);

        Assert.assertEquals(product.getCity(),"Москва");
        Assert.assertEquals(product.getPrice(),1000);

        Mockito.verify(productRepository, Mockito.times(2)).save(product);
    }

    @Test
    public void findAllTest() throws IOException {

        Product product1 = new Product();
        product1.setCity("Москва");
        product1.setPrice(1000);
        Product product2 = new Product();
        product2.setCity("Краснодар");
        product2.setPrice(2500);

        File file = new File("/Users/pyatyshev/IdeaProjects/Buysell-Lesson8/src/main/resources/static/images/624224_0.jpg");
        String name = file.getName();
        String originalFileName = file.getName();
        String contentType = "image/jpg";
        byte[] content = Files.readAllBytes(file.toPath());

        MultipartFile result = new MockMultipartFile(name, originalFileName, contentType, content);

        when(productRepository.save(product1)).thenReturn(product1);
        when(productRepository.save(product2)).thenReturn(product2);
        productService.saveProduct(null, product1,result,result,result);
        productService.saveProduct(null, product1,result,result,result);

        List<Product> list = new ArrayList<>();
        list.add(product1);
        list.add(product2);

        Mockito.doReturn(list).when(productRepository).findAll();

        Assert.assertEquals(productService.listProducts(null).get(0),product1);
        Assert.assertEquals(product1.getCity(),"Москва");
        Assert.assertEquals(product1.getPrice(),1000);
        Assert.assertEquals(productService.listProducts(null).get(1),product2);
        Assert.assertEquals(product2.getCity(),"Краснодар");
        Assert.assertEquals(product2.getPrice(),2500);
    }

    @Test
    public void findAllWithTitleTest() throws IOException {

        Product product1 = new Product();
        product1.setTitle("title1");
        product1.setCity("Москва");
        product1.setPrice(1000);
        Product product2 = new Product();
        product2.setTitle("title2");
        product2.setCity("Краснодар");
        product2.setPrice(2500);
        Product product3 = new Product();
        product3.setTitle("title2");
        product3.setCity("Москва");
        product3.setPrice(2200);

        File file = new File("/Users/pyatyshev/IdeaProjects/Buysell-Lesson8/src/main/resources/static/images/624224_0.jpg");
        String name = file.getName();
        String originalFileName = file.getName();
        String contentType = "image/jpg";
        byte[] content = Files.readAllBytes(file.toPath());

        MultipartFile result = new MockMultipartFile(name, originalFileName, contentType, content);

        when(productRepository.save(product1)).thenReturn(product1);
        when(productRepository.save(product2)).thenReturn(product1);
        when(productRepository.save(product3)).thenReturn(product1);
        productService.saveProduct(null, product1,result,result,result);
        productService.saveProduct(null, product2,result,result,result);
        productService.saveProduct(null, product3,result,result,result);

        List<Product> list1 = new ArrayList<>();
        list1.add(product1);

        List<Product> list2 = new ArrayList<>();
        list2.add(product2);
        list2.add(product3);

        Mockito.doReturn(list1).when(productRepository).findByTitle("title1");
        Mockito.doReturn(list2).when(productRepository).findByTitle("title2");

        Product testProduct1 = productService.listProducts("title1").get(0);
        Product testProduct2 = productService.listProducts("title2").get(0);
        Product testProduct3 = productService.listProducts("title2").get(1);

        Assert.assertEquals(testProduct1,product1);
        Assert.assertEquals(testProduct1.getCity(),"Москва");
        Assert.assertEquals(testProduct1.getPrice(),1000);
        Assert.assertEquals(testProduct2,product2);
        Assert.assertEquals(testProduct2.getCity(),"Краснодар");
        Assert.assertEquals(testProduct2.getPrice(),2500);
        Assert.assertEquals(testProduct3,product3);
        Assert.assertEquals(testProduct3.getCity(),"Москва");
        Assert.assertEquals(testProduct3.getPrice(),2200);
    }

    @Test
    public void findAllWithTitleFailTest() throws IOException {

        Product product1 = new Product();
        product1.setTitle("title1");
        product1.setCity("Москва");
        product1.setPrice(1000);
        Product product2 = new Product();
        product2.setTitle("title2");
        product2.setCity("Краснодар");
        product2.setPrice(2500);

        File file = new File("/Users/pyatyshev/IdeaProjects/Buysell-Lesson8/src/main/resources/static/images/624224_0.jpg");
        String name = file.getName();
        String originalFileName = file.getName();
        String contentType = "image/jpg";
        byte[] content = Files.readAllBytes(file.toPath());

        MultipartFile result = new MockMultipartFile(name, originalFileName, contentType, content);

        when(productRepository.save(product1)).thenReturn(product1);
        when(productRepository.save(product2)).thenReturn(product1);
        productService.saveProduct(null, product1,result,result,result);
        productService.saveProduct(null, product2,result,result,result);

        List<Product> list1 = new ArrayList<>();
        list1.add(product1);

        List<Product> list2 = new ArrayList<>();
        list2.add(product2);

        Mockito.doReturn(list1).when(productRepository).findByTitle("title1");
        Mockito.doReturn(list2).when(productRepository).findByTitle("title2");

        Product testProduct1 = productService.listProducts("title1").get(0);
        Product testProduct2 = productService.listProducts("title2").get(0);

        Assert.assertEquals(testProduct1,product1);
        Assert.assertEquals(testProduct1.getCity(),"Москва");
        Assert.assertEquals(testProduct1.getPrice(),1000);
        Assert.assertEquals(testProduct2,product2);
        Assert.assertEquals(testProduct2.getCity(),"Краснодар");
        Assert.assertEquals(testProduct2.getPrice(),2500);
    }

    @Test
    public void getUserByPrincipalTest(){
        User user = new User();
        user.setName("TestUser1");
        user.setEmail("testuser1@mail.ru");
        Principal principal = (UserPrincipal) () -> "TestUser1";

        Mockito.doReturn(user).when(userRepository).findByEmail(principal.getName());

        Assert.assertEquals(productService.getUserByPrincipal(principal), user);

    }

    @Test
    public void getUserByUserTest(){
        User user = new User();
        user.setName("TestUser1");
        user.setEmail("testuser1@mail.ru");
        Principal principal = (UserPrincipal) () -> "TestUser1";

        Mockito.doReturn(user).when(userRepository).findByEmail(principal.getName());

        Assert.assertEquals(productService.getUserByPrincipal(principal), user);

    }

    @Test
    public void getUserByPrincipalFailTest(){
        Assert.assertEquals(productService.getUserByPrincipal(null), new User());
    }

    @Test
    public void getUserByIdTest(){
        User user = new User();
        user.setId(1L);
        user.setEmail("testuser1@mail.ru");

        Optional<User> opt = Optional.ofNullable(user);

        Mockito.doReturn(opt).when(userRepository).findById(1L);

        Assert.assertEquals(productService.getUserById(1L), user);
        Assert.assertEquals(productService.getUserById(1L).getEmail(), "testuser1@mail.ru");
    }

    @Test
    public void getUserByIdWithNullTest(){
        User user = new User();
        user.setId(1L);
        user.setEmail("testuser1@mail.ru");

        Optional<User> opt = Optional.ofNullable(user);

        Mockito.doReturn(opt).when(userRepository).findById(2L);

        Assert.assertEquals(productService.getUserById(1L), new User());
        Assert.assertEquals(productService.getUserById(1L).getEmail(), null);
    }

    @Test
    public void deleteProductTest() throws IOException {
        User user = new User();
        user.setId(1L);

        Product product1 = new Product();
        product1.setUser(user);
        product1.setId(1L);

        Optional<Product> opt = Optional.ofNullable(product1);

        Mockito.doReturn(opt).when(productRepository).findById(1L);


        productService.deleteProduct(user,1L);

        Mockito.verify(productRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(productRepository, Mockito.times(1)).delete(ArgumentMatchers.any(Product.class));
    }

    @Test
    public void deleteProductFailTest() throws IOException {
        User user = new User();
        user.setId(1L);

        Product product1 = new Product();
        product1.setUser(user);
        product1.setId(1L);

        Optional<Product> opt = Optional.ofNullable(product1);

        Mockito.doReturn(opt).when(productRepository).findById(1L);


        productService.deleteProduct(user,2L);

        Mockito.verify(productRepository, Mockito.times(1)).findById(2L);
        Mockito.verify(productRepository, Mockito.times(0)).delete(ArgumentMatchers.any(Product.class));
    }

    @Test
    public void getProductByIdTest(){
        Product product1 = new Product();
        product1.setId(1L);
        product1.setTitle("Test1");

        Optional<Product> opt = Optional.ofNullable(product1);

        Mockito.doReturn(opt).when(productRepository).findById(1L);

        Assert.assertEquals(productService.getProductById(1L), product1);
        Assert.assertEquals(productService.getProductById(1L).getTitle(), "Test1");

        Mockito.verify(productRepository, Mockito.times(2)).findById(1L);
    }

    @Test
    public void getProductByIdFailTest(){
        Product product1 = new Product();
        product1.setId(1L);
        product1.setTitle("Test1");

        Optional<Product> opt = Optional.ofNullable(product1);

        Mockito.doReturn(opt).when(productRepository).findById(1L);

        Assert.assertEquals(productService.getProductById(2L), null);

        Mockito.verify(productRepository, Mockito.times(1)).findById(2L);
    }

}
