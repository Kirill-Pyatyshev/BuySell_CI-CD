package com.example.buysell.unittest.service;

import com.example.buysell.models.User;
import com.example.buysell.models.enums.Role;
import com.example.buysell.repositories.UserRepository;
import com.example.buysell.services.UserService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    public void createUserTest(){
        User user = new User();

        boolean isCreated = userService.createUser(user);

        Assert.assertTrue(isCreated);
        Assert.assertNull(user.getActivationCode());
        Assert.assertEquals(user.getRoles().toString(),"[ROLE_ADMIN]");

        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    public void createUserFailTest(){
        User user = new User();
        user.setEmail("user1@mail.ru");

        Mockito.doReturn(new User()).when(userRepository).findByEmail("user1@mail.ru");

        boolean isCreated = userService.createUser(user);

        Assert.assertFalse(isCreated);

        Mockito.verify(userRepository, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
    }

    @Test
    public void setRoleTest(){

        User user = new User();

        Map<String, String> form = new HashMap<>();
        form.put("userId", "1");
        form.put("ROLE_USER", "on");

        userService.changeUserRoles(user, form);

        Assert.assertEquals(user.getRoles().toString(),"[ROLE_USER]");

        Mockito.verify(userRepository, Mockito.times(1)).save(ArgumentMatchers.any(User.class));
    }

    @Test
    public void setRoleFailTest(){

        User user = new User();

        Map<String, String> form = new HashMap<>();
        form.put("userId", "1");
        form.put("ROLE_USER", "on");

        userService.changeUserRoles(user, form);

        Assert.assertEquals(user.getRoles().toString(),"[ROLE_USER]");

        Mockito.verify(userRepository, Mockito.times(1)).save(ArgumentMatchers.any(User.class));
    }

    @Test
    public void findAllTest(){

        User user1 = new User();
        User user2 = new User();
        boolean isCreated1 = userService.createUser(user1);
        boolean isCreated2 = userService.createUser(user2);

        List<User> list = new ArrayList<>();
        list.add(user1);
        list.add(user2);

        Mockito.doReturn(list).when(userRepository).findAll();

        Assert.assertTrue(isCreated1);
        Assert.assertTrue(isCreated2);
        Assert.assertNull(user1.getActivationCode());
        Assert.assertNull(user2.getActivationCode());
        Assert.assertEquals(user1.getRoles().toString(),"[ROLE_ADMIN]");
        Assert.assertEquals(user2.getRoles().toString(),"[ROLE_ADMIN]");


        Assert.assertEquals(userService.list().get(0),user1);
        Assert.assertEquals(userService.list().get(1),user2);

    }

    @Test
    public void findAllFailTest(){

        User user1 = new User();
        User user2 = new User();

        List<User> list = new ArrayList<>();
        list.add(user1);
        list.add(user2);

        Mockito.doReturn(list).when(userRepository).findAll();

        Assert.assertEquals(userService.list().get(0),user1);
        Assert.assertEquals(userService.list().get(1),user2);

    }

    @Test
    public void banUserTest(){

        User user1 = new User();
        Set<Role> roles = new HashSet<>();
        roles.add(Role.ROLE_USER);

        userService.banUser(user1.getId());
        user1.setRoles(roles);

        Assert.assertEquals(user1.getRoles().toString(),"[ROLE_USER]");

        Mockito.verify(userRepository, Mockito.times(1)).findById(user1.getId());

    }

    @Test
    public void unBanUserTest(){

        User user1 = new User();
        user1.setActive(false);


        userService.banUser(user1.getId());
        user1.setActive(true);

        Assert.assertEquals(user1.isActive(),true);

        Mockito.verify(userRepository, Mockito.times(1)).findById(user1.getId());

    }

    @Test
    public void banUserFailTest(){

        User user1 = new User();
        Set<Role> roles = new HashSet<>();
        roles.add(Role.ROLE_USER);

        userService.banUser(user1.getId());
        user1.setRoles(roles);

        Assert.assertEquals(user1.getRoles().toString(),"[ROLE_USER]");

        Mockito.verify(userRepository, Mockito.times(1)).findById(user1.getId());

    }

}
