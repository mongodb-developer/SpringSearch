package com.timkelly.springsearch.util;
import com.timkelly.springsearch.model.Note;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.*;

@Component
public class MarkdownNoteParser {

    private static final Pattern FRONTMATTER_PATTERN = Pattern.compile("^---\\s*\\n(.*?)\\n---\\s*\\n", Pattern.DOTALL);
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public Note parse(String rawMarkdown) {
        Matcher matcher = FRONTMATTER_PATTERN.matcher(rawMarkdown);

        Map<String, Object> metadata = new HashMap<>();
        if (matcher.find()) {
            String frontmatter = matcher.group(1);
            rawMarkdown = rawMarkdown.substring(matcher.end());
            Yaml yaml = new Yaml();
            metadata = yaml.load(frontmatter);
        }

        String title = extractTitle(rawMarkdown);
        String content = rawMarkdown.trim();

        String[] tags = Optional.ofNullable(metadata.get("tags"))
                .map(t -> ((List<?>) t).stream().map(Object::toString).toArray(String[]::new))
                .orElse(new String[0]);

        Date createdAt = Optional.ofNullable(metadata.get("created"))
                .map(Object::toString)
                .map(this::parseDate)
                .orElse(null);

        return new Note(title, content, tags, createdAt);
    }

    private String extractTitle(String markdown) {
        Scanner scanner = new Scanner(markdown);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.startsWith("# ")) {
                return line.substring(2).trim();
            }
        }
        return "Untitled";
    }

    private Date parseDate(String dateStr) {
        try {
            return DATE_FORMAT.parse(dateStr);
        } catch (ParseException e) {
            System.err.println("Failed to parse date: " + dateStr);
            return null;
        }
    }
}
