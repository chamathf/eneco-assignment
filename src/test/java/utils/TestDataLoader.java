package utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.Map;

// This Method use for Loads test data from a JSON file located in the "testdata" resources folder.Json file The JSON file is parsed into a Map<String, Object>.
public class TestDataLoader {
    public static Map<String, Object> loadTestData(String fileName) {
        try {
            InputStream is = TestDataLoader.class.getClassLoader().getResourceAsStream("testdata/" + fileName);
            if (is == null) {
                throw new RuntimeException("Test data file not found: " + fileName);
            }
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(is, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load test data: " + fileName, e);
        }
    }
}
