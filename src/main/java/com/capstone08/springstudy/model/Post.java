package com.capstone08.springstudy.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id ;         // 번호
    private String nick;        // 글쓴이
    private String subject;     // 제목
    private String content;     // 내용
    private String date;          // 날짜
    private int hit;            // 조회

    public Post() {
        this.hit = 0;
    }

    public Post(String nick, String subject, String content, String date, int hit) {
        this.nick = nick;
        this.subject = subject;
        this.content = content;
        this.date = date;
        this.hit = hit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getHit() {
        return hit;
    }

    public void setHit(int hit) {
        this.hit = hit;
    }

    public static class PostBuilder {
        private int id;
        private String nick;
        private String subject;
        private String content;
        private String date;
        private int hit;

        public PostBuilder(int id){
            this.id = id;
        }

        public PostBuilder() {}

        public PostBuilder withId(int id){
            this.id = id;
            return this;
        }

        public PostBuilder withNick(String nick){
            this.nick = nick;
            return this;
        }

        public PostBuilder withSubject(String subject){
            this.subject = subject;
            return this;
        }

        public PostBuilder withContent(String content){
            this.content = content;
            return this;
        }

        public PostBuilder withDate(String date){
            this.date = date;
            return this;
        }

        public PostBuilder withHit(int hit){
            this.hit = hit;
            return this;
        }

        public Post build(){
            Post post = new Post();
            post.setId(id);
            post.setNick(nick);
            post.setSubject(subject);
            post.setContent(content);
            post.setDate(date);
            post.setHit(hit);
            return post;
        }
    }
}