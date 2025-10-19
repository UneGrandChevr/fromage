package com.example.fromage;

public class ItemLayout {
    private int imageResId;
    private String title;
    private String subtitle;

    public ItemLayout(int imageResId, String title, String subtitle) {
        this.imageResId = imageResId;
        this.title = title;
        this.subtitle = subtitle;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }
}
