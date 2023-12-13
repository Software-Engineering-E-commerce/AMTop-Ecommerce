package com.example.BackEnd.Services;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    @Mock
    private File file;
    @InjectMocks
    private ImageService imageService;

    @Test
    void saveImage_WhenImageIsNotAnImage_ThrowsIllegalStateException() {
        // Arrange
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file", "test.txt", "text/plain", "Spring Framework".getBytes());
        Long id = 1L;

        // Act and Assert
        assertThrows(IllegalStateException.class, () ->
                imageService.saveImage(mockMultipartFile, id));
    }

    @Test
    void saveImage_WhenIOExceptionOccurs_ThrowsIOException() throws IOException {
        // Arrange
        MultipartFile mockMultipartFile = mock(MultipartFile.class);
        Long id = 2L;

        when(mockMultipartFile.getContentType()).thenReturn("image/png");
        when(mockMultipartFile.getOriginalFilename()).thenReturn("test.png");

        doThrow(new IOException()).when(mockMultipartFile).transferTo(any(File.class));

        // Act and Assert
        assertThrows(IOException.class, () ->
                imageService.saveImage(mockMultipartFile, id));
    }

    @Test
    void saveImage_SuccessfullySavesImage() throws IOException {
        // Arrange
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file", "test.png", "image/png", "Image Content".getBytes());
        Long id = 3L;


        // Act
        String result = imageService.saveImage(mockMultipartFile, id);

        // Assert
        assertEquals("/BackEnd/src/main/resources/static/images/Products/" +
                id + "-" + mockMultipartFile.getOriginalFilename(), result);
    }

    @Test
    void saveImage_WhenImageIsEmpty_ThrowsIllegalStateException() {
        // Arrange
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file", "test.png", "image/png", "".getBytes());
        Long id = 4L;

        // Act and Assert
        assertThrows(IllegalStateException.class, () ->
                imageService.saveImage(mockMultipartFile, id));
    }

    @Test
    void deleteImage_success() throws IOException {
        // Arrange
        String imageLink = "/BackEnd/src/main/resources/static/images/Products/3-test.png";
        // Act and Assert
        imageService.deleteImage(imageLink);
    }
}