package com.example.buysell.unittest;

import com.example.buysell.models.User;
import com.example.buysell.repositories.UserRepository;
import com.example.buysell.services.UserService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class PasswordEncoderTest {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void passwordEncoderTest(){

        User user1 = new User();
        user1.setPassword("123");
        boolean isCreated = userService.createUser(user1);

        Assert.assertTrue(isCreated);
        Assert.assertNull(user1.getActivationCode());
        Assert.assertEquals(user1.getRoles().toString(),"[ROLE_ADMIN]");

        Assert.assertTrue(passwordEncoder.matches("123", user1.getPassword()));
    }
}
