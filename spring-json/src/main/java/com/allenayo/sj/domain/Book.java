package com.allenayo.sj.domain;

import java.util.List;

public class Book {
    private Long id;
    private String name;
    private String author;
    private String lang;
    private double price;
    private int totalPages;
    private List<Chapter> chapters;

    public Book() {
    } // 不能省略，解析JSON请求数据的时候需要

    public Book(Long id, String name, String author, String lang, double price, int totalPages, List<Chapter> chapters) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.lang = lang;
        this.price = price;
        this.totalPages = totalPages;
        this.chapters = chapters;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }
}
