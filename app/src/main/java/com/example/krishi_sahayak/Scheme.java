package com.example.krishi_sahayak;

public class Scheme {
    private String name;
    private String description;
    private String link;
    private String video; // field name matches Firestore

    public Scheme() {
        // Needed for Firebase
    }

    public Scheme(String name, String description, String link, String video) {
        this.name = name;
        this.description = description;
        this.link = link;
        this.video = video;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getLink() { return link; }
    public String getVideo() { return video; }
}
