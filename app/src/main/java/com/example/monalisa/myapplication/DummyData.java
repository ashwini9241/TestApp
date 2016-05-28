package com.example.monalisa.myapplication;

/**
 * Created by monalisa on 27/05/16.
 */
public class DummyData {
    String title;
    String description;
    String imageUrl;

    public DummyData(String title, String description, String imageUrl) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }


}
