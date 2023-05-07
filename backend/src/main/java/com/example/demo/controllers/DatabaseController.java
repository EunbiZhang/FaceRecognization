package com.example.demo.controllers;

import com.example.demo.database.DatabaseRepository;
import com.example.demo.database.Image;
import com.example.demo.database.User;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;

@CrossOrigin(origins = "${FRONTEND_HOST:*}")
@RestController
public class DatabaseController {

    RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/backend-db-greeting")
    public String greeting(@RequestParam(value = "name", defaultValue = "World") final String name) {
        return "Hello World";
    }

    @RequestMapping(value = "/backend-db-test-post", method = RequestMethod.POST)
    public String postTest(@RequestBody String data) throws JSONException {
        JSONObject jsonObject= new JSONObject(data);
        String name = jsonObject.getString("name");
        String email = jsonObject.getString("email");

        UUID uuid = UUID.randomUUID();
        String userId = uuid.toString();

        User user = new User(userId, name, email);

        databaseImpl.saveUser(user);
        return "Received post request";
    }

    @Autowired
    DatabaseRepository databaseImpl;

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public ResponseEntity<String> postForm(@RequestBody String data) throws JSONException {
        JSONObject jsonObject= new JSONObject(data);
        String name = jsonObject.getString("name");
        String email = jsonObject.getString("email");
        String base64 = jsonObject.getString("image").replace("data:image/png;base64,", "");
        byte[] decode = Base64.getDecoder().decode(base64);

        UUID uuid = UUID.randomUUID();
        String userId = uuid.toString();

        User user = new User(userId, name, email);
        Image image = new Image(userId, decode);

        databaseImpl.saveUser(user);
        databaseImpl.saveImage(image);

        ResponseEntity<String> result = new ResponseEntity<String>(HttpStatus.OK);

        return result;

    }

    @GetMapping("/image/{userId}")
    ResponseEntity<Image> getImage(@PathVariable String userId) throws IOException {

        Image image = databaseImpl.getImage(userId);

        String imageName = image.userId + ".png";

        saveImageToFile(image.image, imageName);

        return new ResponseEntity<Image>(image, HttpStatus.OK);

    }

    private void saveImageToFile(byte[] image, String imageName) throws IOException {
        if (new File("/images/").exists()) {
            Files.write(new File("/images/" + imageName).toPath(), image);
        }
        else {
            new File("/images/").mkdir();
            Files.write(new File("/images/" + imageName).toPath(), image);
        }
    }
}
