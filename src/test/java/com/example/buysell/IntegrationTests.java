package com.example.buysell;

import com.example.buysell.controller.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")

class IntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserController controller;

    @Test
    @Sql(value = {"/user1-create-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/user1-create-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithUserDetails("user1@mail.ru")
    public void mainPageTest() throws Exception{
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/nav/div/span").string("user1"));
    }

    @Test
    @Sql(value = {"/admin-create-before.sql", "/user1-create-before.sql","/user1-product1-create-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/user1-create-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithUserDetails("user1@mail.ru")
    public void productListTest() throws Exception{
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='product-list']/a").nodeCount(1));
    }

    @Test
    @Sql(value = {"/admin-create-before.sql", "/user1-create-before.sql","/user1-product1-create-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/user1-create-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithUserDetails("user1@mail.ru")
    public void productListSearchWordTest() throws Exception{
        this.mockMvc.perform(get("/").param("searchWord", "Product1"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='product-list']/a").nodeCount(1));
    }

    @Test
    @Sql(value = {"/admin-create-before.sql", "/user1-create-before.sql","/user1-product1-create-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/user1-create-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithUserDetails("user1@mail.ru")
    public void productListSearchCityTest() throws Exception{
        this.mockMvc.perform(get("/").param("searchWord", "Product1"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='product-list']/a").nodeCount(1));
    }

    @Test
    @Sql(value = {"/admin-create-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/user1-create-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithUserDetails("admin@mail.ru")
    public void profileAdminTest() throws Exception{
        this.mockMvc.perform(get("/profile"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/div/h2").string("admin"))
                .andExpect(xpath("/html/body/div/div/b[1]").string("admin@mail.ru"))
                .andExpect(xpath("/html/body/div/div/a[1]").string("Панель администратора"));
    }

    @Test
    @Sql(value = {"/admin-create-before.sql", "/user1-create-before.sql","/user1-product1-create-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/user1-create-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithUserDetails("user1@mail.ru")
    public void productProfileTest() throws Exception{
        this.mockMvc.perform(get("/my/products"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/h1").string("Мои товары"))
                .andExpect(xpath("//*[@id='my-product']/a").nodeCount(1));
    }

    @Test
    public void contextLoadsTest() throws Exception {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("BUYSELL")));
    }

    @Test
    @Sql(value = {"/user1-create-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/user1-create-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void correctLoginTest() throws Exception{
        this.mockMvc.perform(formLogin().user("user1@mail.ru").password("123"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void badCredentialsTest() throws Exception{
        this.mockMvc.perform(post("/login").param("user1", "kirill"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @Sql(value = {"/user1-create-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/user1-create-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithUserDetails("user1@mail.ru")
    public void accessDeniedUserToAdminTest() throws Exception{
        this.mockMvc.perform(get("/admin/"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void accessDeniedPathProductTest() throws Exception{
        this.mockMvc.perform(get("/product/"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void accessDeniedPathUserTest() throws Exception{
        this.mockMvc.perform(get("/user/"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void registrationUserTest() throws Exception {
        MockHttpServletRequestBuilder multipart1 = multipart("/registration")
                .param("name", "user4")
                .param("email", "user4@mail.ru")
                .param("phoneNumber", "123")
                .param("password", "123")
                .with(csrf());

        this.mockMvc.perform(multipart1)
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    @Sql(value = {"/admin-create-before.sql", "/user1-create-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/user1-create-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithUserDetails("admin@mail.ru")
    public void banUserTest() throws Exception {
        this.mockMvc.perform(get("/admin"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/h1").string("Панель администратора"))
                .andExpect(xpath("/html/body/div/table/tr[3]/th[1]").string("user1@mail.ru"))
                .andExpect(xpath("/html/body/div/table/tr[3]/th[3]").string("true"));

        this.mockMvc.perform(post("/admin/user/ban/{id}",2).with(csrf()))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"));

        this.mockMvc.perform(get("/admin"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/h1").string("Панель администратора"))
                .andExpect(xpath("/html/body/div/table/tr[3]/th[1]").string("user1@mail.ru"))
                .andExpect(xpath("/html/body/div/table/tr[3]/th[3]").string("false"));
    }


}