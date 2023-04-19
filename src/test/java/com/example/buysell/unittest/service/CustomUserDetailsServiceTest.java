package com.example.buysell.unittest.service;

import com.example.buysell.models.User;
import com.example.buysell.repositories.UserRepository;
import com.example.buysell.services.CustomUserDetailsService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CustomUserDetailsServiceTest {

    private MockMvc mockMvc;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    public void loadUserByUsernameTest(){
        User user = new User();
        user.setEmail("test@mail.ru");

        when(userRepository.findByEmail("test@mail.ru")).thenReturn(user);

        Assert.assertNotNull(customUserDetailsService.loadUserByUsername("test@mail.ru"));
        Assert.assertEquals(user, customUserDetailsService.loadUserByUsername("test@mail.ru"));

        verify(userRepository, Mockito.times(2)).findByEmail("test@mail.ru");

    }

}
