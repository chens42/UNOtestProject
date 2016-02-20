package com.unoapp;


public class DetailObject {
    private String id;
    private String image;
    private String type;

    public DetailObject(String id, String image, String type) {
        this.id = id;
        this.image = image;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getType() {
        return type;
    }
}
