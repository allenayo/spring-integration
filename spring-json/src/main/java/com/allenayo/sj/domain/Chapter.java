package com.allenayo.sj.domain;

public class Chapter {
    private Long id;
    private String title;
    private String content;
    private int words;

    public Chapter() {
    }

    public Chapter(Long id, String title, String content, int words) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.words = words;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getWords() {
        return words;
    }

    public void setWords(int words) {
        this.words = words;
    }

}
