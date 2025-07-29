package com.timkelly.springsearch.runner;

import com.timkelly.springsearch.service.NoteImportService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class NoteImporterRunner implements CommandLineRunner {

    private final NoteImportService service;

    public NoteImporterRunner(NoteImportService service) {
        this.service = service;
    }

    @Override
    public void run(String... args) throws Exception {
        service.importNotes();
        System.out.println("Notes imported to MongoDB");
    }
}
