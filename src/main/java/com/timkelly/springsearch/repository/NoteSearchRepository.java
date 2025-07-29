package com.timkelly.springsearch.repository;

import com.timkelly.springsearch.model.Note;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NoteSearchRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public NoteSearchRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<Note> search(String query) {
        AggregationOperation searchStage = _ -> new Document("$search",
                new Document("index", "default")
                        .append("text", new Document("query", query)
                                .append("path", List.of("title", "content", "tags")))
        );

        Aggregation aggregation = Aggregation.newAggregation(searchStage);
        return mongoTemplate.aggregate(aggregation, "notes", Note.class).getMappedResults();
    }

    public List<Note> searchBoostedByTitle(String query) {
        AggregationOperation searchStage = _ -> new Document("$search",
                new Document("index", "default")
                        .append("compound", new Document("should", List.of(
                                new Document("text", new Document("query", query)
                                        .append("path", "title")
                                        .append("score", new Document("boost", new Document("value", 5)))), // Boost title matches

                                new Document("text", new Document("query", query)
                                        .append("path", "content")),

                                new Document("text", new Document("query", query)
                                        .append("path", "tags"))
                        )))
        );

        AggregationOperation sortStage = Aggregation.sort(Sort.by(Sort.Order.desc("score")));

        Aggregation agg = Aggregation.newAggregation(searchStage, sortStage);

        return mongoTemplate.aggregate(agg, "notes", Note.class).getMappedResults();
    }

    public List<Note> searchBoostedTitleAndTags(String query) {
        AggregationOperation searchStage = _ -> new Document("$search",
                new Document("index", "default")
                        .append("compound", new Document("should", List.of(
                                new Document("text", new Document("query", query)
                                        .append("path", "title")
                                        .append("score", new Document("boost", new Document("value", 5)))),
                                new Document("text", new Document("query", query)
                                        .append("path", "tags")
                                        .append("score", new Document("boost", new Document("value", 3)))),
                                new Document("text", new Document("query", query)
                                        .append("path", "content"))
                        )))
        );

        Aggregation agg = Aggregation.newAggregation(searchStage);

        return mongoTemplate.aggregate(agg, "notes", Note.class).getMappedResults();
    }

    public List<Note> searchAutocompleteAndFuzzy(String query) {
        AggregationOperation searchStage = _ -> new Document("$search",
                new Document("index", "default")
                        .append("compound", new Document("should", List.of(
                                new Document("autocomplete", new Document()
                                        .append("query", query)
                                        .append("path", "title")
                                ),
                                new Document("text", new Document()
                                        .append("query", query)
                                        .append("path", "title")
                                        .append("fuzzy", new Document("maxEdits", 2))
                                )
                        )))
        );

        AggregationOperation limitStage = Aggregation.limit(10);

        Aggregation aggregation = Aggregation.newAggregation(searchStage, limitStage);

        return mongoTemplate.aggregate(aggregation, "notes", Note.class).getMappedResults();
    }

    public List<Note> searchAutocompleteFuzzyBoosted(String query) {
        AggregationOperation searchStage = _ -> new Document("$search",
                new Document("index", "default")
                        .append("compound", new Document("should", List.of(
                                // Autocomplete on title (highest boost)
                                new Document("autocomplete", new Document()
                                        .append("query", query)
                                        .append("path", "title")
                                        .append("score", new Document("boost", new Document("value", 6)))
                                ),
                                // Fuzzy text search on title
                                new Document("text", new Document()
                                        .append("query", query)
                                        .append("path", "title")
                                        .append("fuzzy", new Document("maxEdits", 2))
                                        .append("score", new Document("boost", new Document("value", 5)))
                                ),
                                // Fuzzy text search on tags
                                new Document("text", new Document()
                                        .append("query", query)
                                        .append("path", "tags")
                                        .append("fuzzy", new Document("maxEdits", 1))
                                        .append("score", new Document("boost", new Document("value", 3)))
                                ),
                                // Fuzzy text search on content (no boost = default weight)
                                new Document("text", new Document()
                                        .append("query", query)
                                        .append("path", "content")
                                        .append("fuzzy", new Document("maxEdits", 1))
                                )
                        )))
        );

        AggregationOperation sortByScore = Aggregation.sort(Sort.by(Sort.Order.desc("score")));
        AggregationOperation limitStage = Aggregation.limit(10);

        Aggregation aggregation = Aggregation.newAggregation(searchStage, sortByScore, limitStage);

        return mongoTemplate.aggregate(aggregation, "notes", Note.class).getMappedResults();
    }

}

