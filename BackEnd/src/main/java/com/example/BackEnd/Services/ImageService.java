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
        String imageLink = "/products/" + imageName;
        try {
            String path = new File("..").getCanonicalPath() + "/FrontEnd/src/assets";
            System.out.println(path);
            image.transferTo(new File(path + imageLink));
            return imageLink;
        } catch (IOException e) {
            throw new IOException("Could not save image: " + imageName);
        }
    }

    public void deleteImage(String imageLink) {
        try {
            String path = new File("..").getCanonicalPath() + "/FrontEnd/src/assets";
            File file = new File(path + imageLink);
            if (file.delete()) {
                System.out.println("File deleted successfully");
            } else {
                System.out.println("Failed to delete the file");
            }
        } catch (IOException e) {
            System.out.println("Could not delete image: " + imageLink);
        }
    }
}