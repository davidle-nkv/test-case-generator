package com.nakivo.testgen.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.util.Base64;

public class GitHubFileService {

    private final String owner;
    private final String repo;
    private final String branch;
    private final String pat; // Personal Access Token
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GitHubFileService(String owner, String repo, String branch, String pat) {
        this.owner = owner;
        this.repo = repo;
        this.branch = branch;
        this.pat = pat;
    }

    public File getFileFromGithub(String filePath) throws IOException, InterruptedException {
        String apiUrl = "https://api.github.com/repos/" + owner + "/" + repo + "/contents/" + filePath + "?ref=" + branch;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(apiUrl))
            .header("Authorization", "token " + pat)
            .header("Accept", "application/vnd.github+json")
            .GET()
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JsonNode json = objectMapper.readTree(response.body());
            String contentBase64 = json.get("content").asText().replaceAll("\n", "");
            byte[] decodedBytes = Base64.getDecoder().decode(contentBase64);

            // Create a temporary file
            File tempFile = Files.createTempFile("github_", "_" + new File(filePath).getName()).toFile();
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(decodedBytes);
            }

            return tempFile;
        } else if (response.statusCode() == 404) {
            System.out.println("File not found in repository: " + filePath);
            return null;
        } else {
            throw new RuntimeException("GitHub API error: " + response.statusCode() + " - " + response.body());
        }
    }
}
