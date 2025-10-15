package com.nakivo.testgen.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

@Service
public class TestGeneratorService {

    private Configuration freemarkerConfig;

    public TestGeneratorService(Configuration freemarkerConfig) {
        freemarkerConfig = freemarkerConfig;
    }

    public File generateTestFile(String summary, String description, String author) throws Exception {
        Template template = freemarkerConfig.getTemplate("TestCaseTemplate.ftl");

        Map<String, Object> data = new HashMap<>();
        data.put("summary", summary);
        data.put("description", description);
        data.put("author", author == null ? "System" : author);

        String safeName = summary.replaceAll("[^a-zA-Z0-9]", "");
        File output = Files.createTempFile("Generated_" + safeName + "_", ".java").toFile();

        try (Writer writer = new FileWriter(output)) {
            template.process(data, writer);
        }

        return output;
    }

    public String generateTestFromFile(String inputFile) {
        try {
            System.out.println("Reading input from: " + inputFile);
            Map<String, Object> testData = parseInputFile(inputFile);
            String outputFile = generateTestFile(testData);
            return outputFile;
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate test: " + e.getMessage(), e);
        }
    }

    private Map<String, Object> parseInputFile(String filePath) throws Exception {
        // Replace with your existing logic
        System.out.println("Parsing input file: " + filePath);
        return Map.of("example", "data");
    }

    private String generateTestFile(Map<String, Object> data) throws Exception {
        // Replace with your existing logic to write file
        String outputFile = "/tmp/generated_test.txt";
        System.out.println("Generated test file at: " + outputFile);
        return outputFile;
    }
}
