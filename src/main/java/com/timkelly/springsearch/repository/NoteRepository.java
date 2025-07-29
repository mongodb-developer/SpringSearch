package com.timkelly.springsearch.repository;

import com.timkelly.springsearch.model.Note;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NoteRepository extends MongoRepository<Note, String> {
    Optional<Note> findByTitle(String title);
}
