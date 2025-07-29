package com.timkelly.springsearch.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "notes.folder")
public class NotesFolderProperties {
    private String path;

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
}
