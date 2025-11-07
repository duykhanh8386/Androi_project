package com.example.studymate.ui.home.model;

public class ClassItem {
    private final long id;
    private final String name;

    public ClassItem(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() { return id; }
    public String getName() { return name; }
}
