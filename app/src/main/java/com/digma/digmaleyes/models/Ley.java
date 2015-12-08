package com.digma.digmaleyes.models;

/**
 * Created by janidham on 03/12/15.
 */
public class Ley {

    private int id;
    private String name, url, tags;

    public Ley(int id, String name, String url, String tags) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.tags = tags;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getTags() {
        return tags;
    }

}
