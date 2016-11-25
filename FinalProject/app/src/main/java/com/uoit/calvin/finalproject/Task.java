package com.uoit.calvin.finalproject;

public class Task {

    private long id;
    private String title;
    private boolean selected = false;

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

    @Override
    public String toString() {
        return title;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return this.selected;
    }


}
