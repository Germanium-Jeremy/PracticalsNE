package com.app.javaapp.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String description;
    private boolean completed;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @JsonIgnore
    private LocalDateTime updatedAt;

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.completed = false;
    }

    public Task(String title, String description, User user) {
        this.title = title;
        this.description = description;
        this.completed = false;
        this.user = user;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
        this.updatedAt = LocalDateTime.now();
    }
}
