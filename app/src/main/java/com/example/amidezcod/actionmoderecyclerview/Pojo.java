package com.example.amidezcod.actionmoderecyclerview;

/**
 * Created by amidezcod on 24/7/17.
 */

public class Pojo {
    private String text;
    private int id;

    public Pojo(String text, int id) {
        this.text = text;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
