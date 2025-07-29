package com.timkelly.springsearch.controller;

import com.timkelly.springsearch.model.Note;
import com.timkelly.springsearch.repository.NoteSearchRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteSearchController {

    private final NoteSearchRepository searchRepository;

    public NoteSearchController(NoteSearchRepository searchRepository) {
        this.searchRepository = searchRepository;
    }

    @GetMapping("/search")
    public List<Note> search(@RequestParam String q) {
        return searchRepository.search(q);
    }

    @GetMapping("/search/boost-title")
    public List<Note> searchBoostedByTitle(@RequestParam String q) {
        return searchRepository.searchBoostedByTitle(q);
    }

    @GetMapping("/search/boost-title-tags")
    public List<Note> searchBoostedTitleAndTags(@RequestParam String q) {
        return searchRepository.searchBoostedTitleAndTags(q);
    }

    @GetMapping("/search/autocomplete-fuzzy")
    public List<Note> searchAutocompleteAndFuzzy(@RequestParam String q) {
        return searchRepository.searchAutocompleteAndFuzzy(q);
    }

    @GetMapping("/search/autocomplete-fuzzy-boosted")
    public List<Note> searchAutocompleteFuzzyBoosted(@RequestParam String q) {
        return searchRepository.searchAutocompleteFuzzyBoosted(q);
    }
}

