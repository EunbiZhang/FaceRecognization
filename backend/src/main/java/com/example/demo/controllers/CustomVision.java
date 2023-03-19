package com.example.demo.controllers;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class CustomVision {
    final static String trainingEndpoint = "https://westus2.api.cognitive.microsoft.com";
    final static String trainingApiKey = "bc0b2682818e48b0810e1a99afa640c2";
    static RestTemplate restTemplate = new RestTemplate();
    final static String projectID = "0234f884-3c83-42b5-8e47-d12e66fa0a3f";
    public final static String tagIdEunbi = "3902d21a-44ab-4f1d-bcb7-9550bdde1c10";
    public final static String tagIdOthers = "0f7262d2-b614-4404-8669-50e1ac6a0f81";

    public static void createProject(String projectName) throws JSONException {
        String url = "{endpoint}/customvision/v3.3/Training/projects?name={name}";
        Map<String, String> params = new HashMap<>();
        params.put("endpoint", trainingEndpoint);
        params.put("name", projectName);
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        // System.out.println(builder.buildAndExpand(params).toUri());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Training-key", trainingApiKey);
        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<String> response = restTemplate.postForEntity(builder.buildAndExpand(params).toUri(), request,
                String.class);
        // System.out.println(response.getBody())
        JSONObject jsonObject = new JSONObject(response.getBody());
        System.out.println(jsonObject.getString("id"));
    }

    public static void createTag(String tagName) throws JSONException {
        String url = "{endpoint}/customvision/v3.3/Training/projects/{projectId}/tags?name={name}";
        Map<String, String> params = new HashMap<>();
        params.put("endpoint", trainingEndpoint);
        params.put("projectId", projectID);
        params.put("name", tagName);
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        URI uri = builder.buildAndExpand(params).toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Training-key", trainingApiKey);
        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);
        JSONObject jsonObject = new JSONObject(response.getBody());
        System.out.println(jsonObject.getString("id") + ": " + tagName);
    }

    public static void uploadImage(String tagId, byte[] fileData) throws JSONException, IOException {
        String url = "{endpoint}/customvision/v3.3/training/projects/{projectId}/images?tagIds={tagIds}";
        Map<String, String> params = new HashMap<>();
        params.put("endpoint", trainingEndpoint);
        params.put("projectId", projectID);
        params.put("tagIds", tagId);
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        URI uri = builder.buildAndExpand(params).toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.set("Training-key", trainingApiKey);
        HttpEntity<byte[]> request = new HttpEntity<>(fileData, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);
        System.out.println(response.getBody());
    }

    public static ResponseEntity<String> validate(byte[] data) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/octet-stream");
        headers.add("Prediction-Key", "bc0b2682818e48b0810e1a99afa640c2");
        HttpEntity<byte[]> entity = new HttpEntity<>(data, headers);
        String URL = "https://westus2.api.cognitive.microsoft.com/customvision/v3.0/Prediction/0234f884-3c83-42b5-8e47-d12e66fa0a3f/classify/iterations/iteration1/image";
        ResponseEntity<String> result = restTemplate.postForEntity(URL, entity, String.class);
        return result;
    }

    public static void main(String args[]) throws JSONException, IOException {
        // createProject("customVision");
//        createTag("Eunbi");
//        createTag("Others");
    }
}