package com.extreme.data;

public enum Genre {
    ACTION("Action"),
    DRAMA("Drama"),
    SCIENCE_FICTION("Science Fiction"),
    COMEDY("Comedy");

    private String displayName;

    Genre(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
