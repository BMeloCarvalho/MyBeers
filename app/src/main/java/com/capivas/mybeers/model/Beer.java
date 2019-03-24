package com.capivas.mybeers.model;

public class Beer {
    private Long id;
    private String name;
    private String tagline;
    private String description;
    private String image_url;

    public Beer(Long id, String name, String tagline, String description, String image_url) {
        this.id = id;
        this.name = name;
        this.tagline = tagline;
        this.description = description;
        this.image_url = image_url;
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

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}