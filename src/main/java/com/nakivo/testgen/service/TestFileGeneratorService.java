package com.nakivo.testgen.service;


import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class TestFileGeneratorService {
    private static final String TEMPLATE_DIR = "templates";
    private static final String OUTPUT_DIR = "src/test/java/com/nakivo/tests/manual";

    public Map<String, String> generateFromText(String textInput) throws IOException, TemplateException {
        Map<String, Object> data = parseInputText(textInput);
        return generateTestContent(data);
    }

    private Map<String, Object> parseInputText(String text) throws IOException {
        Map<String, Object> map = new HashMap<>();
        List<String> steps = new ArrayList<>();

        List<String> lines = Arrays.asList(text.split("\\r?\\n"));
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) continue;

            if (line.toLowerCase().startsWith("title:")) {
                map.put("title", line.substring(6).trim());
            } else if (line.toLowerCase().startsWith("groups:")) {
                map.put("groups", normalizeQuotedList(line.substring(7).trim()));
            } else if (line.toLowerCase().startsWith("id:")) {
                map.put("id", line.substring(3).trim());
            } else if (line.toLowerCase().startsWith("category:")) {
                map.put("category", line.substring(9).trim());
            } else if (line.toLowerCase().startsWith("feature:")) {
                map.put("feature", line.substring(8).trim());
            } else if (line.toLowerCase().startsWith("step")) {
                steps.add(line.replaceFirst("(?i)step\\s*\\d*[:\\-]\\s*", "").trim());
            }
        }

        map.put("steps", steps);
        if (!map.containsKey("title") || !map.containsKey("groups") || !map.containsKey("id")) {
            throw new IllegalArgumentException("Input text missing required fields (id/title/groups).");
        }
        return map;
    }

    private Map<String, String> generateTestContent(Map<String, Object> data)
        throws IOException, TemplateException {

        Map<String, String> result = new HashMap<>();

        String title = (String) data.get("title");
        String groups = (String) data.get("groups");
        String id = (String) data.get("id");
        String category = (String) data.getOrDefault("category", "VMwareBackup");
        String feature = (String) data.getOrDefault("feature", "VMWAREBACKUP");
        @SuppressWarnings("unchecked")
        List<String> steps = (List<String>) data.get("steps");

        // Setup FreeMarker
        Configuration cfg = new Configuration(new Version("2.3.29"));
        cfg.setClassLoaderForTemplateLoading(getClass().getClassLoader(), TEMPLATE_DIR);
        cfg.setDefaultEncoding("UTF-8");

        Template methodTemplate = cfg.getTemplate("method_template.ftl");

        // Prepare step data
        List<Map<String, String>> stepData = new ArrayList<>();
        for (String s : steps) {
            Map<String, String> st = new HashMap<>();
            st.put("text", s);
            st.put("methodCall", mapToMethod(s));
            stepData.add(st);
        }

        // Create a method using template
        Map<String, Object> methodCtx = new HashMap<>();
        methodCtx.put("id", id);
        methodCtx.put("groups", groups);
        methodCtx.put("firstGroup", groups.split(",")[0].trim());
        methodCtx.put("description", title);
        methodCtx.put("feature", feature);
        methodCtx.put("steps", stepData);

        StringWriter methodOut = new StringWriter();
        methodTemplate.process(methodCtx, methodOut);
        String methodContent = methodOut.toString();

        // Create a new class using template
        Template classTemplate = cfg.getTemplate("class_template.ftl");

        File dir = new File(OUTPUT_DIR);
        String className = category + "ManualTest";
        File file = new File(dir, className + ".java");
        Map<String, Object> classCtx = new HashMap<>();
        classCtx.put("packageName", "com.nakivo.tests.manual");
        classCtx.put("className", className);
        classCtx.put("category", category);
        classCtx.put("testMethods", Arrays.asList(methodContent));

        StringWriter classOut = new StringWriter();
        classTemplate.process(classCtx, classOut);

        return Map.of("path", file.getPath().replace("\\", "/"), "content", classOut.toString());
    }

    // --- New: CSV path + loader replacing the hardcoded map ---
    private static final String STEP_MAPPINGS_CSV_PATH = "step-mappings.csv";
    private final Map<String, String> STEP_MAPPINGS = loadStepMappings();

    // --- CSV Loader Implementation ---
    private Map<String, String> loadStepMappings() {
        InputStream csvStream = getClass().getClassLoader().getResourceAsStream("step-mappings.csv");
        if (csvStream == null) {
            throw new IllegalStateException("Step mappings CSV not found at: " + STEP_MAPPINGS_CSV_PATH);
        }
        Map<String, String> map = new LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(csvStream, StandardCharsets.UTF_8))) {
            String line;
            int lineNum = 0;
            while ((line = br.readLine()) != null) {
                lineNum++;
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                // Basic CSV split (first comma separates key and method). Supports quoted key/value.
                String key;
                String value;

                int commaPos = findFirstCommaOutsideQuotes(line);
                if (commaPos < 0) {
                    throw new IllegalStateException("Invalid CSV format (missing comma) at line " + lineNum);
                }
                key = line.substring(0, commaPos).trim();
                value = line.substring(commaPos + 1).trim();

                key = unquote(key).toLowerCase(Locale.ROOT).trim();
                value = unquote(value).trim().replace("\"\"", "\""); // unescape quotes

                if (key.isEmpty() || value.isEmpty()) {
                    throw new IllegalStateException("Empty key/value at line " + lineNum);
                }
                if (map.containsKey(key)) {
                    System.out.println("[WARN] Duplicate key overwritten: " + key + " (line " + lineNum + ")");
                }
                map.put(key, value.endsWith(";") ? value : value + ";");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read step mappings CSV: " + e.getMessage(), e);
        }
        if (map.isEmpty()) {
            throw new IllegalStateException("Step mappings CSV is empty.");
        }
        System.out.println("[INFO] Loaded " + map.size() + " step mappings from CSV.");
        return Collections.unmodifiableMap(map);
    }

    private static int findFirstCommaOutsideQuotes(String line) {
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') inQuotes = !inQuotes;
            else if (c == ',' && !inQuotes) return i;
        }
        return -1;
    }

    private static String unquote(String s) {
        s = s.trim();
        if (s.startsWith("\"") && s.endsWith("\"") && s.length() >= 2) {
            return s.substring(1, s.length() - 1);
        }
        return s;
    }

    private String mapToMethod(String step) {
        String stepDesc = step.replaceFirst("Step \\d+:\\s*", "").trim().toLowerCase();
        String bestKey = null;
        double bestScore = 0.0;

        for (String key : STEP_MAPPINGS.keySet()) {
            double score = tokenOverlap(key, stepDesc);
            if (score > bestScore) {
                bestScore = score;
                bestKey = key;
            }
        }

        if (bestScore >= 0.5) {
            return STEP_MAPPINGS.get(bestKey);
        } else {
            throw new RuntimeException("Cannot detect method mapping for step: " + step);
        }
    }

    private static double tokenOverlap(String a, String b) {
        Set<String> sa = new HashSet<String>(Arrays.asList(a.split("\\s+")));
        Set<String> sb = new HashSet<String>(Arrays.asList(b.split("\\s+")));
        int common = 0;
        for (String s : sa) {
            if (sb.contains(s)) common++;
        }
        return (2.0 * common) / (sa.size() + sb.size());
    }

    public static String normalizeQuotedList(String input) {
        if (input == null || input.isBlank()) return "";
        return Arrays.stream(input.split(","))
            .map(String::trim)
            .map(s -> s.replaceAll("['\"]", ""))  // remove existing quotes
            .filter(s -> !s.isEmpty())            // remove empty entries
            .map(s -> "\"" + s + "\"")            // wrap with double quotes
            .collect(Collectors.joining(", "));
    }

}
