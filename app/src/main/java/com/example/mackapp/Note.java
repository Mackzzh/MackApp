package com.example.mackapp;

public class Note {
    private Long id;
    private String content;
    private String time;
    private int tag;

    public Note(){

    }

    public Note(String content,String time,int tag){
        this.content = content;
        this.time = time;
        this.tag = tag;
    }

    public Long getId() {
        return id;
    }

    public int getTag() {
        return tag;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public void setTag(int tag) {
        this.tag = tag;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
