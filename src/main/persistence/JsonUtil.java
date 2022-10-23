package persistence;

import org.json.JSONArray;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// the class for Json utilities
// util file based on the JsonSerializationDemo project
public class JsonUtil {

    // EFFECTS: reads json text file as json array
    public static JSONArray getArray(String source) throws IOException {
        String content = readFile(source);
        return new JSONArray(content);
    }

    // EFFECTS: reads source file as string and returns it
    private static String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }

        return contentBuilder.toString();
    }

    public static void writeFile(String source, JSONArray content) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(source);
        writer.write(content.toString(4));
        writer.close();
    }
}
