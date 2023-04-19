package com.example.buysell.unittest.repo;

import com.example.buysell.models.User;
import com.example.buysell.repositories.ImageRepository;
import com.example.buysell.repositories.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
public class ImageRepositoryTest {

    @MockBean
    private ImageRepository imageRepository;

    @Test
    public void saveUserRepoTest() {
        com.example.buysell.models.Image image = new com.example.buysell.models.Image();
        com.example.buysell.models.Image image1 = new com.example.buysell.models.Image();

        imageRepository.save(image);
        imageRepository.save(image1);

        List<com.example.buysell.models.Image> imageList = new ArrayList<>();

        imageList.add(image);
        imageList.add(image);

        when(imageRepository.findAll()).thenReturn(imageList);

        Assert.assertNotNull(imageRepository.findAll());
        Assert.assertEquals(2, imageRepository.findAll().size());
    }

    @Test
    public void UserFindByIdRepoTest() {
        com.example.buysell.models.Image image = new com.example.buysell.models.Image();
        image.setId(1L);

        imageRepository.save(image);

        when(imageRepository.findById(1L)).thenReturn(Optional.of(image));

        Assert.assertNotNull(imageRepository.findById(1L));
        Assert.assertEquals(Optional.of(image), imageRepository.findById(1L));
    }
}
