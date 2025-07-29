package com.timkelly.springsearch.service;

import com.timkelly.springsearch.config.NotesFolderProperties;
import com.timkelly.springsearch.model.Note;
import com.timkelly.springsearch.repository.NoteRepository;
import com.timkelly.springsearch.util.MarkdownNoteParser;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class NoteImportService {

    private final NotesFolderProperties props;
    private final MarkdownNoteParser parser;
    private final NoteRepository repository;

    public NoteImportService(NotesFolderProperties props, MarkdownNoteParser parser, NoteRepository repository) {
        this.props = props;
        this.parser = parser;
        this.repository = repository;
    }

    public void importNotes() throws IOException {

        List<Path> files = Files.walk(Paths.get(props.getPath()))
                .filter(p -> p.toString().endsWith(".md"))
                .toList();

        for (Path path : files) {
            String content = Files.readString(path);
            Note newNote = parser.parse(content);

            repository.findByTitle(newNote.getTitle())
                    .ifPresentOrElse(existing -> {
                        boolean changed =
                                !Objects.equals(existing.getContent(), newNote.getContent()) ||
                                        !Arrays.equals(existing.getTags(), newNote.getTags()) ||
                                        !Objects.equals(existing.getCreatedAt(), newNote.getCreatedAt());

                        if (changed) {
                            existing.setContent(newNote.getContent());
                            existing.setTags(newNote.getTags());
                            existing.setCreatedAt(newNote.getCreatedAt());
                            repository.save(existing);
                            System.out.println("Updated: " + existing.getTitle());
                        } else {
                            System.out.println("Skipped (no changes): " + existing.getTitle());
                        }
                    }, () -> {
                        repository.save(newNote);
                        System.out.println("Inserted: " + newNote.getTitle());
                    });
        }
    }

}
