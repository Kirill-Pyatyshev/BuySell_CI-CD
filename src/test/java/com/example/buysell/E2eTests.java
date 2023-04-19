package com.example.buysell;

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
@Sql(value = {"/admin-create-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/admin-create-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class E2eTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void mainTest() throws Exception{

        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("BUYSELL")));

        MockHttpServletRequestBuilder multipart1 = multipart("/registration")
                .param("name", "user1")
                .param("email", "user1@mail.ru")
                .param("phoneNumber", "123")
                .param("password", "123")
                .with(csrf());

        this.mockMvc.perform(multipart1)
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        this.mockMvc.perform(formLogin().user("user1@mail.ru").password("123"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

    }

    @Test
    @Sql(value = {"/user1-create-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/user1-create-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithUserDetails("user1@mail.ru")
    public void mainTest2() throws Exception{

        this.mockMvc.perform(formLogin().user("user1@mail.ru").password("123"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        MockHttpServletRequestBuilder multipart2 = multipart("/product/create").file("file1" ,"**/main/resources/static/images/624224_0.jpg)".getBytes())
                .file("file2","".getBytes())
                .file("file3","".getBytes())
                .param("title", "producte1")
                .param("description", "ProductTest1")
                .param("price", "1000")
                .param("city", "Москва")
                .with(csrf());

        this.mockMvc.perform(multipart2)
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/my/products"));

        this.mockMvc.perform(get("/profile"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/div/h2").string("user1"))
                .andExpect(xpath("/html/body/div/div/b[1]").string("user1@mail.ru"));

        this.mockMvc.perform(get("/my/products"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/h1").string("Мои товары"))
                .andExpect(xpath("//*[@id='my-product']/a").nodeCount(1));
    }

    @Test
    @Sql(value = {"/user1-create-before.sql","/user1-product1-create-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/user1-create-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithUserDetails("user1@mail.ru")
    public void mainTest3() throws Exception{

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

        this.mockMvc.perform(formLogin().user("user4@mail.ru").password("123"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/h1").string("Поиск товаров"))
                .andExpect(xpath("//*[@id='product-list']/a").nodeCount(1));

        this.mockMvc.perform(get("/product/{id}", 1))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/p[1]").string("Product1 | 1,000 ₽"))
                .andExpect(xpath("/html/body/div/p[4]/a/h4").string("user1"));

        this.mockMvc.perform(get("/user/{id}",2))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/h1").string("Информация о пользователе"))
                .andExpect(xpath("/html/body/div/h1[2]").string("Товары пользователя user1"))
                .andExpect(xpath("/html/body/div/div[2]/a").nodeCount(1));
    }

    @Test
    @Sql(value = {"/admin-create-before.sql", "/user1-create-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/user1-create-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithUserDetails("admin@mail.ru")
    public void mainTest4() throws Exception{

        this.mockMvc.perform(formLogin().user("admin@mail.ru").password("123"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        this.mockMvc.perform(get("/profile"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/div/h2").string("admin"))
                .andExpect(xpath("/html/body/div/div/b[1]").string("admin@mail.ru"))
                .andExpect(xpath("/html/body/div/div/a[1]").string("Панель администратора"));

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

    @Test
    @Sql(value = {"/admin-create-before.sql", "/user1-create-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/user1-create-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithUserDetails("admin@mail.ru")
    public void mainTest5() throws Exception{

        this.mockMvc.perform(formLogin().user("admin@mail.ru").password("123"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        this.mockMvc.perform(get("/profile"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/div/h2").string("admin"))
                .andExpect(xpath("/html/body/div/div/b[1]").string("admin@mail.ru"))
                .andExpect(xpath("/html/body/div/div/a[1]").string("Панель администратора"));

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

        this.mockMvc.perform(post("/logout").with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?logout"));

        this.mockMvc.perform(formLogin().user("user1@mail.ru").password("123"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    @Sql(value = {"/admin-create-before.sql", "/user1-create-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/user1-create-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithUserDetails("admin@mail.ru")
    public void mainTest6() throws Exception{

        this.mockMvc.perform(formLogin().user("admin@mail.ru").password("123"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        this.mockMvc.perform(get("/profile"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/div/h2").string("admin"))
                .andExpect(xpath("/html/body/div/div/b[1]").string("admin@mail.ru"))
                .andExpect(xpath("/html/body/div/div/a[1]").string("Панель администратора"));

        this.mockMvc.perform(get("/admin"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/h1").string("Панель администратора"))
                .andExpect(xpath("/html/body/div/table/tr[3]/th[1]").string("user1@mail.ru"))
                .andExpect(xpath("/html/body/div/table/tr[3]/th[4]").string("ROLE_USER"));


        this.mockMvc.perform(post("/admin/user/edit")
                        .param("userId", "2")
                        .param("ROLE_ADMIN", "on")
                        .with(csrf()))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection());

        this.mockMvc.perform(get("/admin"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/h1").string("Панель администратора"))
                .andExpect(xpath("/html/body/div/table/tr[3]/th[1]").string("user1@mail.ru"))
                .andExpect(xpath("/html/body/div/table/tr[3]/th[4]").string("ROLE_ADMIN"));

    }

    @Test
    @Sql(value = {"/admin-create-before.sql", "/user1-create-before.sql","/user1-product1-create-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/user1-create-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithUserDetails("user1@mail.ru")
    public void mainTest7() throws Exception{

        this.mockMvc.perform(formLogin().user("user1@mail.ru").password("123"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        MockHttpServletRequestBuilder multipart2 = multipart("/product/create").file("file1" ,"624224_0.jpg)".getBytes())
                .file("file2","".getBytes())
                .file("file3","".getBytes())
                .param("title", "producte1")
                .param("description", "ProductTest1")
                .param("price", "1000")
                .param("city", "Москва")
                .with(csrf());

        this.mockMvc.perform(multipart2)
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/my/products"));

        this.mockMvc.perform(get("/profile"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/div/h2").string("user1"))
                .andExpect(xpath("/html/body/div/div/b[1]").string("user1@mail.ru"));

        this.mockMvc.perform(get("/my/products"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/h1").string("Мои товары"))
                .andExpect(xpath("//*[@id='my-product']/a").nodeCount(2));

        this.mockMvc.perform(delete("/product/delete/{id}",1)
                        .param("id", "1")
                        .param("userId", "2")
                        .with(csrf()))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/my/products"));

        this.mockMvc.perform(get("/my/products"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/h1").string("Мои товары"))
                .andExpect(xpath("//*[@id='my-product']/a").nodeCount(2));
    }

    @Test
    @Sql(value = {"/admin-create-before.sql", "/user1-create-before.sql","/user1-product1-create-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/user1-create-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithUserDetails("user1@mail.ru")
    public void mainTest8() throws Exception{

        this.mockMvc.perform(formLogin().user("admin@mail.ru").password("123"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='product-list']/a").nodeCount(1));

        this.mockMvc.perform(get("/profile"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/div/h2").string("admin"))
                .andExpect(xpath("/html/body/div/div/b[1]").string("admin@mail.ru"))
                .andExpect(xpath("/html/body/div/div/a[1]").string("Панель администратора"));

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

        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='product-list']/a").nodeCount(1));

    }
}
