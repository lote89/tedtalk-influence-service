package com.io.ted.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class TedTalk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String speaker;
    private Long views;
    private Long likes;

   
    private LocalDate date;
    private String link;

   
    private Integer year;
    private String url;

    public TedTalk() {}

    public TedTalk(String title, String speaker, Integer year, Long views, Long likes, String url) {
        this.title = title;
        this.speaker = speaker;
        this.year = year;
        this.views = views;
        this.likes = likes;
        this.url = url;
    }
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getSpeaker() { return speaker; }
    public Long getViews() { return views; }
    public Long getLikes() { return likes; }

    public LocalDate getDate() { return date; }
    public String getLink() { return link; }

    public Integer getYear() { return year; }
    public String getUrl() { return url; }

    
    public void setTitle(String title) { this.title = title; }
    public void setSpeaker(String speaker) { this.speaker = speaker; }
    public void setViews(Long views) { this.views = views; }
    public void setLikes(Long likes) { this.likes = likes; }

    public void setDate(LocalDate date) {
        this.date = date;
        if (date != null) {
            this.year = date.getYear(); // keep year in sync
        }
    }

    public void setLink(String link) {
        this.link = link;
        this.url = link; 
    }

    public void setYear(Integer year) { this.year = year; }
    public void setUrl(String url) { this.url = url; }
}
