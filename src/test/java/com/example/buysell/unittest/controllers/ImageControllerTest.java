package com.example.buysell.unittest.controllers;

import com.example.buysell.controller.ImageController;
import com.example.buysell.models.Image;
import com.example.buysell.repositories.ImageRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
public class ImageControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ImageController imageController;

    @Test
    public void getImageByIdTest() {

        Image image = mock(Image.class);
        image.setOriginalFileName("624224_0.jpg");
        image.setSize(30051L);
        image.setBytes("Test".getBytes());
        image.setContentType("image/jpeg");

        when(imageRepository.findById(1L)).thenReturn(Optional.ofNullable(image));
        when(image.getContentType()).thenReturn("image/jpeg");
        when(image.getBytes()).thenReturn("Test".getBytes());

        Assert.assertNotNull(imageController.getImageById(1L));
        Assert.assertEquals(imageController.getImageById(1l).toString(),"<200 OK OK,InputStream resource [resource loaded through InputStream],[fileName:\"null\", Content-Type:\"image/jpeg\", Content-Length:\"0\"]>");

        verify(imageRepository, Mockito.times(2)).findById(1L);
    }
}
