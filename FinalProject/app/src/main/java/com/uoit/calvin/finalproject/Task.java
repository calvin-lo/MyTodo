package com.uoit.calvin.finalproject;

public class Task {

    private long id;
    private String title;
    private boolean selected = false;
    private String timestamp;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String tag) {
        this.title = tag;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return title;
    }


}
