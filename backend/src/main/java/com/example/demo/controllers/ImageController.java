package com.example.demo.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.UUID;

import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.azure.storage.blob.*;
import java.io.*;

@RestController
@CrossOrigin(origins = "${FRONTEND_HOST:*}")
public class ImageController {
    @GetMapping("/greeting")
    public String greeting(@RequestParam(value = "name", defaultValue = "World") final String name) {
        return "Hello World";
    }
    
    @PostMapping("/images")
    public ResponseEntity<Object> upload(@RequestBody final String data) throws IOException, JSONException {
        final String base64 = data.replace("data:image/png;base64,", "");
        final byte[] imageBytes = Base64.getDecoder().decode(base64);
        final String imageName = UUID.randomUUID() + ".png";
        // saveImageToFile(imageBytes, imageName);
        // saveImageToCloud(imageBytes, imageName);
        CustomVision.uploadImage(CustomVision.tagIdEunbi, imageBytes);
        return new ResponseEntity<Object>("Image saved successfully", HttpStatus.OK);
    }

    @PostMapping("/validate")
    public ResponseEntity<String> validate(@RequestBody final String data) throws IOException, JSONException {
        final String base64 = data.replace("data:image/png;base64,", "");
        final byte[] imageBytes = Base64.getDecoder().decode(base64);
        ResponseEntity<String> result = CustomVision.validate(imageBytes);
        return result;
    }

    private void saveImageToFile(byte[] image, String imageName) throws IOException {
        File path = new File("./images/");
        if (!path.exists()) {
            path.mkdir();
        }
        Files.write(new File("./images/" + imageName).toPath(), image);
    }

    private void saveImageToCloud(byte[] image, String imageName) {
        String connectionString = "DefaultEndpointsProtocol=https;AccountName=imageclassification123;AccountKey=UxdujUD4B+9piJRju6hmKImrQ3cFzu3z+5JSJ6HCVA7Hh8T7cTjaDqO53n08+2+f2va7a/kHUjoG+AStCEfhlw==;EndpointSuffix=core.windows.net";
        String containerName = "images";

        // Create a BlobServiceClient object to interact with the Blob service
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(connectionString).buildClient();

        // Create a container with public access
        BlobContainerClient blobContainerClient = blobServiceClient.createBlobContainerIfNotExists(containerName);

        // Create a BlobClient object to interact with the blob
        BlobClient blobClient = blobContainerClient.getBlobClient(imageName);

        // Upload content to the blob
        ByteArrayInputStream dataStream = new ByteArrayInputStream(image);
        blobClient.upload(dataStream);
    }
}
