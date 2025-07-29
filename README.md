# SpringSearch – Obsidian Vault Search API

SpringSearch turns your [Obsidian](https://obsidian.md/) vault into a fully searchable knowledge base, powered by **Spring Boot** and **MongoDB Atlas Search**.  
It supports full-text queries, autocomplete, fuzzy matching, and custom boosting for prioritizing your most relevant notes — all exposed via a simple REST API.

## Features
- **Full-text search:** Query across `title`, `tags`, and `content` fields.
- **Autocomplete:** Type-ahead search for instant suggestions.
- **Fuzzy matching:** Tolerates typos and misspellings in queries.
- **Custom boosting:** Prioritize matches in specific fields like `title` or `tags`.
- **Obsidian integration:** Automatically parses Markdown files (with frontmatter) into MongoDB documents.
- **REST API:** Simple endpoints for searching notes.

## Prerequisites
- **Java 24+** (or update `pom.xml` to your installed version)
- **Maven 3.9+**
- **MongoDB Atlas** (free M0 cluster is sufficient)
- An existing **Obsidian Vault** containing Markdown notes with optional frontmatter.

## Getting Started

### 1. Clone the repository
```bash
git clone https://github.com/mongodb-developer/SpringSearch.git
cd SpringSearch
````

### 2. Configure MongoDB

Edit the `src/main/resources/application.properties` file:

```properties
spring.data.mongodb.uri=${MONGODB_URI}
spring.data.mongodb.database=obsidian
notes.folder.path=/path/to/your/vault
```

Make sure your MongoDB Atlas cluster has a [Search Index](https://www.mongodb.com/docs/atlas/atlas-search/index-definitions/) with this configuration:

```json
{
  "mappings": {
    "dynamic": false,
    "fields": {
      "title": [
        { "type": "string" },
        { "type": "autocomplete" }
      ],
      "tags": { "type": "string" },
      "content": { "type": "string" }
    }
  }
}
```

### 3. Run the app

```bash
MONGODB_URI="your-mongodb-uri" mvn spring-boot:run
```

## API Endpoints

**Basic search:**

```
GET /api/notes/search?q=<query>
```

**Boosted by title:**

```
GET /api/notes/search/boost-title?q=<query>
```

**Autocomplete + fuzzy matching:**

```
GET /api/notes/search/autocomplete-fuzzy?q=<query>
```

**All-in-one (boosting + autocomplete + fuzzy):**

```
GET /api/notes/search/autocomplete-fuzzy-boosted?q=<query>
```

## Example Query

```bash
curl "http://localhost:8080/api/notes/search?q=Spring%20Boot"
```

