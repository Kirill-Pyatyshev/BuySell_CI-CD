package com.example.buysell.unittest.controllers;

import com.example.buysell.controller.AdminController;
import com.example.buysell.models.User;
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
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

@SpringBootTest
public class AdminControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private AdminController adminController;

    @Test
    public void adminTest() {

        Model model = mock(Model.class);
        Principal principal = mock(Principal.class);

        Assert.assertNotNull(adminController.admin(model, principal));
        Assert.assertEquals(adminController.admin(model, principal), "admin");

        verify(userService, Mockito.times(2)).list();
        verify(userService, Mockito.times(2)).getUserByPrincipal(ArgumentMatchers.any());
    }

    @Test
    public void userBanTest() {

        Assert.assertNotNull(adminController.userBan(1L));
        Assert.assertEquals(adminController.userBan(1L), "redirect:/admin");

        verify(userService, Mockito.times(2)).banUser(1L);
    }

    @Test
    public void userEditGetTest() {

        User user = mock(User.class);
        Model model = mock(Model.class);
        Principal principal = mock(Principal.class);

        Assert.assertNotNull(adminController.userEdit(user,model,principal));
        Assert.assertEquals(adminController.userEdit(user,model,principal), "user-edit");

        verify(userService, Mockito.times(2)).getUserByPrincipal(principal);
    }

    @Test
    public void userEditPostTest() {

        User user = mock(User.class);
        Map<String, String> form = new HashMap<>();

        Assert.assertNotNull(adminController.userEdit(user,form));
        Assert.assertEquals(adminController.userEdit(user,form), "redirect:/admin");

        verify(userService, Mockito.times(2)).changeUserRoles(user, form);
    }
}
