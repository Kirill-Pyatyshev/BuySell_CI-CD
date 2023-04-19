package com.example.buysell.unittest.controllers;

import com.example.buysell.controller.ProductController;
import com.example.buysell.controller.UserController;
import com.example.buysell.models.Product;
import com.example.buysell.models.User;
import com.example.buysell.services.ProductService;
import com.example.buysell.services.UserService;
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

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class UserControllerTest {
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    public void loginTest(){

        Model model = mock(Model.class);
        Principal principal = mock(Principal.class);
        User user = mock(User.class);



        Assert.assertNotNull(userController.login(principal,model));
        Assert.assertEquals(userController.login(principal,model), "login");

        verify(userService, Mockito.times(2)).getUserByPrincipal(ArgumentMatchers.any());
    }

    @Test
    public void profileTest(){
        Model model = mock(Model.class);
        Principal principal = mock(Principal.class);
        User user = mock(User.class);

        when(userService.getUserByPrincipal(principal)).thenReturn(user);

        Assert.assertNotNull(userController.profile(principal,model));
        Assert.assertEquals(userController.profile(principal,model), "profile");

        verify(userService, Mockito.times(2)).getUserByPrincipal(ArgumentMatchers.any());
    }

    @Test
    public void registrationTest(){

        Model model = mock(Model.class);
        Principal principal = mock(Principal.class);
        User user = mock(User.class);

        when(userService.getUserByPrincipal(principal)).thenReturn(user);

        Assert.assertNotNull(userController.registration(principal,model));
        Assert.assertEquals(userController.registration(principal,model), "registration");

        verify(userService, Mockito.times(2)).getUserByPrincipal(ArgumentMatchers.any());
    }

    @Test
    public void createUserFailTest(){

        Model model = mock(Model.class);
        User user = mock(User.class);

        when(userService.createUser(user)).thenReturn(false);

        Assert.assertNotNull(userController.createUser(user,model));
        Assert.assertEquals(userController.createUser(user,model), "registration");

        verify(userService, Mockito.times(2)).createUser(user);
    }

    @Test
    public void createUserTest(){

        Model model = mock(Model.class);
        User user = mock(User.class);

        when(userService.createUser(user)).thenReturn(true);

        Assert.assertNotNull(userController.createUser(user,model));
        Assert.assertEquals(userController.createUser(user,model), "redirect:/login");

        verify(userService, Mockito.times(2)).createUser(user);
    }

    @Test
    public void userInfoTest(){

        Model model = mock(Model.class);
        Principal principal = mock(Principal.class);
        User user = mock(User.class);

        Assert.assertNotNull(userController.userInfo(user,model,principal));
        Assert.assertEquals(userController.userInfo(user,model,principal), "user-info");

        verify(userService, Mockito.times(2)).getUserByPrincipal(principal);
    }

}
