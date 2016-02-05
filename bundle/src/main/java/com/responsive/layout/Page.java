package com.responsive.layout;

/**
 * Created by daniil.sheidak on 02.02.2016.
 */
public class Page {
    private String title;
    private String path;

    public Page(String title, String path) {
        this.title = title;
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public String getPath() {
        return path;
    }
}
