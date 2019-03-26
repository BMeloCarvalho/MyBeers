package com.capivas.mybeers.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Beer implements Serializable {
    private Long id;
    private String name;
    private String tagline;
    private String description;
    @SerializedName("image_url")
    private String imageLocation;
    private boolean isFavorite = false;

    public Beer() {

    }

    public Beer(Long id, String name, String tagline, String description, String image_url) {
        this.id = id;
        this.name = name;
        this.tagline = tagline;
        this.description = description;
        this.imageLocation = image_url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }

    public boolean isFavorite() {
        return this.isFavorite;
    }

    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }
}