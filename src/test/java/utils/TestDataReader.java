package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.FrameworkConstants;
import java.io.File;

public final class TestDataReader {
    private static final ObjectMapper M = new ObjectMapper();
    private TestDataReader() {}
    public static JsonNode read(String fileName) {
        try { return M.readTree(new File(FrameworkConstants.TESTDATA_DIR + fileName)); }
        catch (Exception e) { throw new RuntimeException("Cannot read " + fileName, e); }
    }
}