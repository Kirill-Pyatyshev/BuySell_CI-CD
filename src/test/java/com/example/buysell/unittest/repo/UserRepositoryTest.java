package com.example.buysell.unittest.repo;

import com.example.buysell.models.User;
import com.example.buysell.repositories.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
public class UserRepositoryTest {

    @MockBean
    private UserRepository userRepository;

    @Test
    public void saveUserRepoTest(){
        User user = new User();
        User user1 = new User();

        userRepository.save(user);
        userRepository.save(user1);

        List<User> userList = new ArrayList<>();

        userList.add(user);
        userList.add(user1);

        when(userRepository.findAll()).thenReturn(userList);

        Assert.assertNotNull(userRepository.findAll());
        Assert.assertEquals(2,userRepository.findAll().size());
    }

    @Test
    public void UserFindByEmailRepoTest(){
        User user = new User();
        user.setEmail("testRepo@mail.ru");

        userRepository.save(user);

        when(userRepository.findByEmail("testRepo@mail.ru")).thenReturn(user);

        Assert.assertNotNull(userRepository.findByEmail("testRepo@mail.ru"));
        Assert.assertEquals(user,userRepository.findByEmail("testRepo@mail.ru"));
    }

    @Test
    public void UserFindByIdRepoTest(){
        User user = new User();
        user.setId(1L);

        userRepository.save(user);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Assert.assertNotNull(userRepository.findById(1L));
        Assert.assertEquals(Optional.of(user),userRepository.findById(1L));
    }

}
