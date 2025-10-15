package com.nakivo.testgen.controller;

import com.nakivo.testgen.model.TestCaseRequest;
import com.nakivo.testgen.service.TestGeneratorService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/api")
public class TestGeneratorController {

    private TestGeneratorService generatorService;

    TestGeneratorController(TestGeneratorService generatorService) {
        this.generatorService = generatorService;
    }

    @PostMapping("/generate")
    public ResponseEntity<FileSystemResource> generate(@RequestBody TestCaseRequest request) throws Exception {
        File file = generatorService.generateTestFile(
                request.getSummary(),
                request.getDescription(),
                request.getAuthor()
        );

        FileSystemResource resource = new FileSystemResource(file);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @PostMapping(value = "/generate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Resource> generateTest(@RequestParam("file") MultipartFile file) {
        try {
            // Save uploaded file to temp folder
            Path tempInput = Files.createTempFile("input-", file.getOriginalFilename());
            file.transferTo(tempInput.toFile());

            // Run generation logic
            String outputFilePath = generatorService.generateTestFromFile(tempInput.toString());

            // Return result file
            File outputFile = new File(outputFilePath);
            Resource resource = new FileSystemResource(outputFile);

            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + outputFile.getName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(null);
        }
    }
}
