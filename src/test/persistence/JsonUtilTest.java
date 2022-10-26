package persistence;

import org.json.JSONArray;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class JsonUtilTest {

    @Test
    void testReadFileNonExistentFile() {
        try {
            JsonUtil.readFile("./data/no-such-file.json");
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReadFileEmptyArray() {
        try {
            String content = JsonUtil.readFile("./data/empty.json");
            assertEquals("[]", content);
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReadFileSamplePolynomials() {
        try {
            String content = JsonUtil.readFile("./data/sample-polynomials.json");
            assertEquals("[    \"x^2\",    \"x^3 + 6x^2 + 11x + 6\",    \"x^3 + -11/4x^2 + -27/2x + 45/4\",    \"-x^2 + 2x + 1\"]", content);
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testGetArraySamplePolynomials() {
        try {
            JSONArray array = JsonUtil.getArray("./data/sample-polynomials.json");
            assertEquals(4, array.length());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testWriteInvalidFile() {
        try {
            JSONArray array = new JSONArray();
            JsonUtil.writeFile("./data/my\0illegal:filename.json", array);
            fail("FileNotFoundException was expected");
        } catch (FileNotFoundException e) {
            // pass
        }
    }

    @Test
    void testWriteNullInput() {
        try {
            JsonUtil.writeFile("./data/my-null-content.json", null);

            fail("NullPointerException was expected");
        } catch (NullPointerException e) {
            // pass
        } catch (FileNotFoundException e) {
            fail("FileNotFoundException was NOT expected");
        }
    }

    @Test
    void testWriterGeneralWorkroom() {
        try {
            JSONArray array = JsonUtil.getArray("./data/sample-polynomials.json");
            JsonUtil.writeFile("./data/sample-polynomials-write.json", array);

            JSONArray arrayCopy = JsonUtil.getArray("./data/sample-polynomials-write.json");
            assertEquals(array.toString(), arrayCopy.toString());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    // only needed for coverage; approved by TA
    @Test
    public void testStaticConstructor() {
        JsonUtil jsonUtil = new JsonUtil();
        assertNotNull(jsonUtil);
    }
}
