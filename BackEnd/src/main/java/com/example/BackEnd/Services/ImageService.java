package com.example.BackEnd.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ImageService {

    public String saveImage(MultipartFile image, Long id) throws IOException, IllegalStateException {
        if (!Objects.requireNonNull(image.getContentType()).startsWith("image/")) {
            throw new IllegalStateException("File must be an image");
        }
        if (image.isEmpty()) {
            throw new IllegalStateException("Cannot upload empty image");
        }
        String imageName = id + "-" + image.getOriginalFilename();
        String imageLink = "/BackEnd/src/main/resources/static/images/Products/" + imageName;
        try {
            String path = new File("..").getCanonicalPath();
            System.out.println(path);
            image.transferTo(new File(path + imageLink));
            return imageLink;
        } catch (IOException e) {
            throw new IOException("Could not save image: " + imageName);
        }
    }


    // TODO: after implementing the add product method, implement this method
    public void updateImage() {

    }


    // TODO: after implementing the update product method, implement this method
    public void deleteImage(String imageLink) throws IOException {

    }
}
