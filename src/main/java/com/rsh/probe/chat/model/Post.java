package com.rsh.probe.chat.model;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name="posts")
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.AUTO) Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss") LocalDateTime createdAt;
    @ManyToOne @JoinColumn(name="user_id") User user; // many posts may belong to one user
    String text;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        if(this.text != null) {
            return "Post{" +
                    "id=" + id +
                    ", createdAt=" + createdAt +
                    ", text='" + text == null ? "null" : text.substring(0, Math.min(10, text.length())) + ".".repeat(text.length() > 10 ? 3 : 0) +  "'" +
                    ", user=" + user +
                    '}';
        } else {
            return "Post{" +
                    "id=" + id +
                    ", createdAt=" + createdAt +
                    ", text=null" +
                    ", user=" + user +
                    '}';
        }
    }
}
