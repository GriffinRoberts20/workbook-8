package com.pluralsight.Models;

public class Film {
    private int filmId;
    private String title;
    private String description;
    private String releaseYear;
    private String length;
    private String rating;

    public Film(int filmId, String title, String description, String releaseYear, String length) {
        this.filmId = filmId;
        this.title = title;
        this.description = description;
        this.releaseYear = releaseYear;
        this.length = length;
    }

    public Film(int filmId, String title, String releaseYear, String rating) {
        this.filmId = filmId;
        this.title = title;
        this.releaseYear = releaseYear;
        this.rating=rating;
    }

    public int getFilmId() {
        return filmId;
    }

    public void setFilmId(int filmId) {
        this.filmId = filmId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public void printBasic(){
        System.out.println(this.filmId+" ".repeat(6-Integer.toString(this.filmId).length())
                +this.title+" ".repeat(30-this.title.length())
                +this.releaseYear+" ".repeat(3)+this.rating);
    }
}
